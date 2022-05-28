package io.stock.kr.calculator.stock.price.crawling;

import io.stock.kr.calculator.common.date.DateRange;
import io.stock.kr.calculator.common.date.MonthRange;
import io.stock.kr.calculator.request.api.data_portal.DataPortalPage;
import io.stock.kr.calculator.request.fsc.FSCAPIType;
import io.stock.kr.calculator.request.fsc.FSCParameters;
import io.stock.kr.calculator.request.fsc.FSCRequestParameters;
import io.stock.kr.calculator.stock.price.crawling.dto.FSCStockPriceItem;
import io.stock.kr.calculator.stock.price.crawling.dto.FSCStockPriceResponse;
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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public void insertAndSaveStockData(String serviceKey, LocalDate startDate, LocalDate endDate, int partitionSize){
        final WebClient webClient = newWebClient(FSCAPIType.STOCK_PRICE_API.getBaseUrl());
        final String encodedKey = encodeServiceKey(serviceKey);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        FSCRequestParameters requestParameters = ofRequestParameters(webClient, encodedKey, startDate.format(formatter), endDate.format(formatter));

        Optional.ofNullable(requestAllStockPrice(requestParameters, 0, 10).getResponse().getBody().getTotalCount())
                .ifPresent(totalCount -> {
                    new MonthRange(startDate, endDate)
                            .stream()
                            .forEach(dt -> {
                                String startDayOfMonth = dt.format(formatter);
                                String endDayOfMonth = dt.plusMonths(1).minusDays(1).format(formatter);
                                FSCRequestParameters eachRequest = ofRequestParameters(webClient, encodedKey, startDayOfMonth, endDayOfMonth);
                                // start ~ end 까지의 전체 상장종목을 페이징 기반으로 순회한다.
                                // totalCount : start ~ end 까지의 전체 개별 데이터 건수
                                // limit : 몇 개 단위로 묶어서 페이징을 할지 결정
                                log.info("totalCount = " + totalCount + ", " + "partitionSize = " + partitionSize);
                                pagedRequestAndWrite(totalCount, partitionSize, eachRequest);
                            });
                });
    }

    /**
     * @param totalPageCnt      페이지의 갯수
     * @param partitionSize     파티션 사이즈
     * @param requestParam      Request Parameter
     */
    public void pagedRequestAndWrite(long totalPageCnt, int partitionSize, FSCRequestParameters requestParam){
        // partitionSize 만큼의 구간으로 나눠서 진행하겠다. (한달평균 5만5천개일 경우 5500개씩 API 다운로드)
        PagingUtil.PageUnit pageUnit = PagingUtil.pageUnit(totalPageCnt, partitionSize);

        LongStream.range(1, partitionSize+1)
                .forEach(offset -> {
                    FSCStockPriceResponse r = requestAllStockPrice(requestParam, offset, pageUnit.getLimit());
                    log.info("API RESPONSE SUCCESS. CURRENT PAGE = " + offset);
                    Long cost = batchWritePriceDayList(r.getResponse().getBody().getItems().getItem());
                    loggingCost(cost, "INSERT COMPLETE. ");
                });

        if(pageUnit.getLimit() * (partitionSize+1) > totalPageCnt){
            FSCStockPriceResponse r = requestAllStockPrice(requestParam, partitionSize+1, pageUnit.getLimit());
            Long cost = batchWritePriceDayList(r.getResponse().getBody().getItems().getItem());
        }
    }

    public FSCStockPriceResponse requestAllStockPrice(FSCRequestParameters parameters, long offset, long limit){
        return parameters.getWebClient().get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                            .scheme("http")
                            .host("apis.data.go.kr")
                            .path(FSCAPIType.STOCK_PRICE_API.webClientEndpoint())
                            .queryParam(FSCParameters.NUM_OF_ROWS.getParameterName(), String.valueOf(limit))
                            .queryParam(FSCParameters.PAGE_NO.getParameterName(), String.valueOf(offset))
                            .queryParam(FSCParameters.RESULT_TYPE.getParameterName(), "json")
                            .queryParam(FSCParameters.BEGIN_BAS_DT.getParameterName(), parameters.getStartDate())
                            .queryParam(FSCParameters.END_BAS_DT.getParameterName(), parameters.getEndDate())
                            .queryParam(FSCParameters.SERVICE_KEY.getParameterName(), parameters.getEncodedKey())
                            .build();

                    log.info(uri.getHost() + ", " + uri.getPath() + ", " + uri.getRawPath());
                    log.info(uri.toString());
                    return uri;
                })
                .retrieve()
                .bodyToMono(FSCStockPriceResponse.class)
                .block();
    }

    public Long batchWritePriceDayList(List<FSCStockPriceItem> stockPriceList){
        List<PriceDayDocument> priceDayDocumentList = stockPriceList.stream()
                .map(fscStockPriceItem -> fscStockPriceItem.toPriceDayDocument())
//                                    .peek(priceDayDocument -> System.out.println(priceDayDocument.getTicker()))
                .collect(Collectors.toList());

        Long insertStart = System.nanoTime();
        priceDayDynamoDBMapper.batchWritePriceDayList(priceDayDocumentList);
        Long diff = System.nanoTime() - insertStart;
        return diff;
    }

    public String encodeServiceKey(String serivceKey){
        String encodedKey = null;
        try {
            encodedKey = URLEncoder.encode(serivceKey, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedKey;
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
}
