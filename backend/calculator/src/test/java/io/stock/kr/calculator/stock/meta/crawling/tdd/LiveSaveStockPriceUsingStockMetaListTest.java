package io.stock.kr.calculator.stock.meta.crawling.tdd;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.stock.kr.calculator.stock.meta.crawling.StockMetaCrawlingDartService;
import io.stock.kr.calculator.stock.meta.crawling.tdd.dto.FSCStockPriceResponse;
import io.stock.kr.calculator.stock.meta.repository.dynamo.StockMetaRepository;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDocument;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDynamoDBMapper;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayRepository;
import io.stock.kr.calculator.util.PagingUtil;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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

/**
 * 기능 완료 후 test-docker 에서만 동작하도록 테스트케이스 변경 및 세부 테스트 게이스 분리 예정
 */
@SpringBootTest
//@ActiveProfiles("test-docker")
@ActiveProfiles("live")
public class LiveSaveStockPriceUsingStockMetaListTest {

    Logger logger = LoggerFactory.getLogger(LiveSaveStockPriceUsingStockMetaListTest.class);

    @Autowired
    StockMetaRepository repository;

    @Autowired
    StockMetaCrawlingDartService service;

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @Autowired
    DynamoDBMapper dynamoDBMapper;

    @Autowired
    PriceDayDynamoDBMapper priceDayDynamoDBMapper;

    @Autowired
    PriceDayRepository priceDayRepository;

    final String NOT_ENCODED_SERVICE_KEY = "==";
    final String BASE_URL = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService";
    final String BASE_PATH = "/1160100/service/GetStockSecuritiesInfoService";

    @BeforeEach
    public void beforeTest(){
//        deleteTableIfExist();
//        createTableRequest();
    }

    @AfterEach
    public void afterTest(){
//        deleteTableIfExist();
    }

    public void deleteTableIfExist(){
        DeleteTableRequest deleteTableRequest = dynamoDBMapper
                .generateDeleteTableRequest(PriceDayDocument.class, DynamoDBMapperConfig.DEFAULT);

        TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);
    }

    public void createTableRequest(){
        CreateTableRequest createTableRequest = dynamoDBMapper
                .generateCreateTableRequest(PriceDayDocument.class, DynamoDBMapperConfig.DEFAULT)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
    }

    @Getter
    enum FSCParameters{
        NUM_OF_ROWS("numOfRows"),
        PAGE_NO("pageNo"),
        RESULT_TYPE("resultType"),
        BEGIN_BAS_DT("beginBasDt"),
        END_BAS_DT("endBasDt"),
        LIKE_SRTN_CD("likeSrtnCd"),
        SERVICE_KEY("serviceKey");

        private final String parameterName;

        FSCParameters(String parameterName){
            this.parameterName = parameterName;
        }

    }

    public FSCStockPriceResponse requestAllStockPrice(WebClient webClient, String serviceKey, String beginBasDt, String endBasDt, long offset, long limit){
        return webClient.get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                            .scheme("http")
                            .host("apis.data.go.kr")
                            .path(BASE_PATH + "/getStockPriceInfo")
                            .queryParam(FSCParameters.NUM_OF_ROWS.getParameterName(), String.valueOf(limit))
                            .queryParam(FSCParameters.PAGE_NO.getParameterName(), String.valueOf(offset))
                            .queryParam(FSCParameters.RESULT_TYPE.getParameterName(), "json")
                            .queryParam(FSCParameters.BEGIN_BAS_DT.getParameterName(), beginBasDt)
                            .queryParam(FSCParameters.END_BAS_DT.getParameterName(), endBasDt)
                            .queryParam(FSCParameters.SERVICE_KEY.getParameterName(), serviceKey)
                            .build();
                    System.out.println(uri.getHost() + ", " + uri.getPath() + ", " + uri.getRawPath());
                    System.out.println(uri.toString());
                    return uri;
                })
                .retrieve()
                .bodyToMono(FSCStockPriceResponse.class)
                .block();
    }

    @Test
    public void 금융결제원_종가데이터를_DynamoDB에_INSERT한다(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        try {
            String encodedKey = URLEncoder.encode(NOT_ENCODED_SERVICE_KEY, StandardCharsets.UTF_8.name());
            System.out.println("encodedKey = " + encodedKey);

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

            FSCStockPriceResponse fscStockPriceResponse = requestAllStockPrice(webClient, encodedKey, "20220401", "20220430", 1L,100L);

//            LocalDate startDate = LocalDate.of(2019,1,1);
//            LocalDate endDate = LocalDate.of(2022,4,1);
//            DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
//
//            for(; startDate.isBefore(endDate); startDate = startDate.plusMonths(1)){
//
//            }

            Optional.ofNullable(fscStockPriceResponse.getResponse().getBody().getTotalCount())
                    .ifPresent(totalCount -> {
                        PagingUtil.PageUnit pageUnit = PagingUtil.pageUnit(totalCount, 10);// 10 개의 구간으로 나눠서 진행하겠다. (한달평균 5만5천개일 경우 5500개씩 API 다운로드)
                        PagingUtil.iterateApiConsumer(pageUnit.getLimit(), 10, totalCount, d -> {
                            FSCStockPriceResponse r1 = requestAllStockPrice(webClient, encodedKey, "20220501", "20220524", d.getStartIndex(), pageUnit.getLimit());

                            List<PriceDayDocument> priceDayDocumentList = r1.getResponse().getBody().getItems().getItem().stream()
                                    .map(fscStockPriceItem -> fscStockPriceItem.toPriceDayDocument())
//                                    .peek(priceDayDocument -> System.out.println(priceDayDocument.getTicker()))
                                    .collect(Collectors.toList());

                            logger.info("API RESPONSE SUCCESS. CURRENT PAGE = " + d.getStartIndex());

                            Long insertStart = System.nanoTime();
                            priceDayDynamoDBMapper.batchWritePriceDayList(priceDayDocumentList);

                            sleep(2000);
                            Long diff = System.nanoTime() - insertStart;

                            StringBuilder builder = new StringBuilder();
                            builder.append("INSERT COMPLETE. ").append(diff).append(" nano second (").append(diff / 1000000).append(" ms)");
                            logger.info(builder.toString());
                        });

                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sleep(long milli){
        try{
            Thread.sleep(milli);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void TEST_월_순회로직(){
        LocalDate startDate = LocalDate.of(2019,1,1);
        LocalDate endDate = LocalDate.of(2022,4,1);
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

        for(; startDate.isBefore(endDate); startDate = startDate.plusMonths(1)){
            System.out.println(">>>");
            System.out.println(startDate);
            LocalDate target = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.plusMonths(1).minusDays(1).getDayOfMonth());
            System.out.println(target);
            System.out.println();
        }
    }
}
