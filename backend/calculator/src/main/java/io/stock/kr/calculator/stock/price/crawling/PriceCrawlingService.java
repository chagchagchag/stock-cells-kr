package io.stock.kr.calculator.stock.price.crawling;

import io.stock.kr.calculator.common.date.MonthRange;
import io.stock.kr.calculator.request.fsc.FSCAPIType;
import io.stock.kr.calculator.request.fsc.FSCRequestParameters;
import io.stock.kr.calculator.stock.price.crawling.dto.FSCStockPriceItem;
import io.stock.kr.calculator.stock.price.crawling.dto.FSCStockPriceResponse;
import io.stock.kr.calculator.stock.price.response.FscResultType;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDocument;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDynamoDBMapper;
import io.stock.kr.calculator.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * TODO
 *  WebClient 객체 생성, API 요청 기능 을 별도 객체로 분리
 *  목표는 페이징과 API 요청기능을 분리하는 것.
 *  JPA 페이징 기능을 참고해서 적용해보기.
 *
 * 1) Web API 요청 컴포넌트 별도 분리해서, 의존성 주입 후 Mock 객체 기반 테스트 가능하도록 분리
 *      = 가급적 공통화 고려하기보다는 구체클래스 기반으로. 아직 어떤 기능이 공통화될지 알수 없기 때문.
 *      = RequestParameter 객체 생성에 필수적으로 필요한 것은 뭐지? 하는 고민을 객체에 적용해두기
 * 2) 페이징 테스트 기능 구현
 * 3)
 */
@Slf4j
@Service
public class PriceCrawlingService {

    private final PriceDayDynamoDBMapper priceDayDynamoDBMapper;

    public PriceCrawlingService(PriceDayDynamoDBMapper priceDayDynamoDBMapper){
        this.priceDayDynamoDBMapper = priceDayDynamoDBMapper;
    }

    /**
     * @param startDate         시작 날짜
     * @param endDate           끝 날짜 
     * @param partitionSize     파티션 사이즈
     * @return FscResultType
     */
    public FscResultType insertAndSaveStockData(LocalDate startDate, LocalDate endDate, int partitionSize){
        return Optional.ofNullable(
                new FSCAPIRequestProcessor(FSCAPIType.STOCK_PRICE_API)
                    .requestStockPrice(startDate, endDate, 1, 10)
                    .getResponse()
                    .getBody()
                    .getTotalCount())
                .map(totalCount -> {
                    new MonthRange(startDate, endDate)  // start ~ end 까지의 전체 상장종목을 페이징 기반으로 순회한다.
                            .stream()
                            .forEach(dt -> {
                                // totalCount : start ~ end 까지의 전체 개별 데이터 건수
                                // limit : 몇 개 단위로 묶어서 페이징을 할지 결정
                                log.info("totalCount = " + totalCount + ", " + "partitionSize = " + partitionSize);
                                pagedRequestAndWrite(dt, endDayOfMonth(dt), totalCount, partitionSize);
                            });
                    return FscResultType.RESULT_EXIST;
                })
                .orElse(FscResultType.EMPTY);
    }

    /**
     * @param startDate         startDate
     * @param endDate           endDate
     * @param totalPageCnt      페이지의 갯수
     * @param partitionSize     파티션 사이즈
     */
    public void pagedRequestAndWrite(LocalDate startDate, LocalDate endDate, long totalPageCnt, int partitionSize){
        // partitionSize 만큼의 구간으로 나눠서 진행하겠다. (한달평균 5만5천개일 경우 5500개씩 API 다운로드)
        PagingUtil.PageUnit pageUnit = PagingUtil.pageUnit(totalPageCnt, partitionSize);
        final long limit = pageUnit.getLimit();

        LongStream.range(1, partitionSize+1)
                .forEach(offset -> {
                    FSCAPIRequestProcessor service = new FSCAPIRequestProcessor(FSCAPIType.STOCK_PRICE_API);
                    FSCStockPriceResponse r = service.requestStockPrice(startDate, endDate, offset, limit);
                    log.info("API RESPONSE SUCCESS. CURRENT PAGE = " + offset);

                    Long cost = batchWritePriceDayList(r.getResponse().getBody().getItems().getItem());
                    loggingCost(cost, "INSERT COMPLETE. ");
                });

        if(limit * (partitionSize+1) > totalPageCnt){
            FSCAPIRequestProcessor service = new FSCAPIRequestProcessor(FSCAPIType.STOCK_PRICE_API);
            FSCStockPriceResponse r = service.requestStockPrice(startDate, endDate, partitionSize + 1, limit);
            Long cost = batchWritePriceDayList(r.getResponse().getBody().getItems().getItem());
            loggingCost(cost, "(LAST PAGE) INSERT COMPLETE. ");
        }
    }

    public Long batchWritePriceDayList(List<FSCStockPriceItem> stockPriceList){
        List<PriceDayDocument> priceDayDocumentList = stockPriceList.stream()
                .map(fscStockPriceItem -> fscStockPriceItem.toPriceDayDocument())
//                                    .peek(priceDayDocument -> System.out.println(priceDayDocument.getTicker()))
                .collect(Collectors.toList());

        long insertStart = System.nanoTime();
        priceDayDynamoDBMapper.batchWritePriceDayList(priceDayDocumentList);
        return System.nanoTime() - insertStart;
    }

    public void loggingCost(Long cost, String message){
        StringBuilder builder = new StringBuilder();
        builder.append(message).append(cost).append(" nano second (").append(cost / 1000000).append(" ms)");
        log.info(builder.toString());
    }

    public FSCRequestParameters ofRequestParameters(WebClient webClient, String encodedKey, String startDate, String endDate){
        return FSCRequestParameters.builder()
                .webClient(webClient).encodedKey(encodedKey)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public WebClient newWebClient(String BASE_URL){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .baseUrl(BASE_URL)
                .uriBuilderFactory(factory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.47")
                .defaultHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .defaultHeader("accept-encoding", "gzip, deflate, br")
                .defaultHeader("accept-language", "ko,en;q=0.9,en-US;q=0.8")
                .build();

        return webClient;
    }

    public LocalDate endDayOfMonth(LocalDate dt){
        return dt.plusMonths(1).minusDays(1);
    }
}
