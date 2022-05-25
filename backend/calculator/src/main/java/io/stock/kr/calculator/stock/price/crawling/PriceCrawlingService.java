package io.stock.kr.calculator.stock.price.crawling;

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
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PriceCrawlingService {

    private final PriceDayDynamoDBMapper priceDayDynamoDBMapper;

    public PriceCrawlingService(PriceDayDynamoDBMapper priceDayDynamoDBMapper){
        this.priceDayDynamoDBMapper = priceDayDynamoDBMapper;
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
                    System.out.println(uri.getHost() + ", " + uri.getPath() + ", " + uri.getRawPath());
                    System.out.println(uri.toString());
                    return uri;
                })
                .retrieve()
                .bodyToMono(FSCStockPriceResponse.class)
                .block();
    }

    public void insertAndSaveStockData(String serviceKey, LocalDate startDate, LocalDate endDate, long offset, long limit){
        final WebClient webClient = newWebClient(FSCAPIType.STOCK_PRICE_API.getBaseUrl());
        String encodedKey = encodeServiceKey(serviceKey);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        FSCRequestParameters requestParameters = FSCRequestParameters.builder()
                .webClient(webClient).encodedKey(encodedKey)
                .startDate(startDate.format(formatter)).endDate(startDate.plusMonths(1).minusDays(1).format(formatter))
                .build();

        FSCStockPriceResponse dataCountRequest = requestAllStockPrice(requestParameters, offset, limit);

        for(; startDate.isBefore(endDate); startDate = startDate.plusMonths(1)){
            String startDayOfMonth = startDate.format(formatter);
            LocalDate target = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.plusMonths(1).minusDays(1).getDayOfMonth());
            String endDayOfMonth = target.format(formatter);

            FSCRequestParameters fscRequestParameters = FSCRequestParameters.builder()
                    .webClient(webClient)
                    .startDate(startDayOfMonth).endDate(endDayOfMonth)
                    .encodedKey(encodedKey)
                    .build();

            // 아래 부분을 더 테스트에 편리한 구조로 변경해야 하는데, 아직은 시간이 더 없어서, Mockito 및 Stub 을 이용한 테스트케이스 작성시에 리팩토링 예정
            Optional.ofNullable(dataCountRequest.getResponse().getBody().getTotalCount())
                    .ifPresent(totalCount-> iteratePagingApiAndBatchWrite(totalCount, fscRequestParameters, limit));
        }
    }

    public void iteratePagingApiAndBatchWrite(Long totalCount, FSCRequestParameters fscRequestParameters, long limit){
        Consumer<DataPortalPage> consumer = d -> requestApiAndBatchWrite(fscRequestParameters, d, limit);
        PagingUtil.PageUnit pageUnit = PagingUtil.pageUnit(totalCount, 10);// 10 개의 구간으로 나눠서 진행하겠다. (한달평균 5만5천개일 경우 5500개씩 API 다운로드)
        PagingUtil.iterateApiConsumer(pageUnit.getLimit(), 10, totalCount, consumer);
    }

    public void requestApiAndBatchWrite(FSCRequestParameters parameters, DataPortalPage d, long limit){
        FSCStockPriceResponse r1 = requestAllStockPrice(parameters, d.getStartIndex(), limit);
        log.info("API RESPONSE SUCCESS. CURRENT PAGE = " + d.getStartIndex());
        Long cost = batchWritePriceDayList(r1.getResponse().getBody().getItems().getItem());
        loggingCost(cost, "INSERT COMPLETE. ");
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

    public Consumer<DataPortalPage> newApiRequestAndInsertConsumer(FSCRequestParameters parameters, Long limit){
        return d -> {
            FSCStockPriceResponse r1 = requestAllStockPrice(parameters, d.getStartIndex(), limit);
            log.info("API RESPONSE SUCCESS. CURRENT PAGE = " + d.getStartIndex());
            Long cost = batchWritePriceDayList(r1.getResponse().getBody().getItems().getItem());
            loggingCost(cost, "INSERT COMPLETE. ");
        };
    }

    public void loggingCost(Long cost, String message){
        StringBuilder builder = new StringBuilder();
        builder.append(message).append(cost).append(" nano second (").append(cost / 1000000).append(" ms)");
        log.info(builder.toString());
    }
}
