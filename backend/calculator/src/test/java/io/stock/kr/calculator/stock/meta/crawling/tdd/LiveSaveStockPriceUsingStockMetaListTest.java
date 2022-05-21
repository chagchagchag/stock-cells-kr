package io.stock.kr.calculator.stock.meta.crawling.tdd;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.stock.kr.calculator.stock.meta.crawling.tdd.dto.FSCStockPriceItem;
import io.stock.kr.calculator.stock.meta.repository.dynamo.StockMetaDocument;
import io.stock.kr.calculator.stock.meta.repository.dynamo.StockMetaRepository;
import io.stock.kr.calculator.stock.meta.crawling.StockMetaCrawlingDartService;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import io.stock.kr.calculator.stock.meta.crawling.tdd.dto.FSCStockPriceResponse;
import io.stock.kr.calculator.stock.price.StockPriceDto;
import io.stock.kr.calculator.util.PagingUtil;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * 기능 완료 후 test-docker 에서만 동작하도록 테스트케이스 변경 및 세부 테스트 게이스 분리 예정
 */
@SpringBootTest
@ActiveProfiles("live")
public class LiveSaveStockPriceUsingStockMetaListTest {
    @Autowired
    StockMetaRepository repository;

    @Autowired
    StockMetaCrawlingDartService service;

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @Autowired
    DynamoDBMapper dynamoDBMapper;

    ThreadPoolExecutor redisSaveExecutor = new ThreadPoolExecutor(1, 3, 2L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    ThreadPoolExecutor apiExecutor = new ThreadPoolExecutor(1, 10, 2L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

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
        if(!((ExecutorService) redisSaveExecutor).isShutdown()){
            redisSaveExecutor.shutdown();
        }
        if(!((ExecutorService)apiExecutor).isShutdown()){
            apiExecutor.shutdown();
        }
    }

    public void deleteTableIfExist(){
        DeleteTableRequest deleteTableRequest = dynamoDBMapper
                .generateDeleteTableRequest(StockMetaDocument.class, DynamoDBMapperConfig.DEFAULT);

        TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);
    }

    public void createTableRequest(){
        CreateTableRequest createTableRequest = dynamoDBMapper
                .generateCreateTableRequest(StockMetaDocument.class, DynamoDBMapperConfig.DEFAULT)
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

    // 폐기 검토
    public FSCStockPriceResponse requestStockPrice(WebClient webClient, String serviceKey, String srtnCd, String beginBasDt, String endBasDt){
        return webClient.get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                            .path("/getStockPriceInfo")
                            .queryParam(FSCParameters.NUM_OF_ROWS.getParameterName(), "365")
                            .queryParam(FSCParameters.PAGE_NO.getParameterName(), "1")
                            .queryParam(FSCParameters.RESULT_TYPE.getParameterName(), "json")
                            .queryParam(FSCParameters.BEGIN_BAS_DT.getParameterName(), beginBasDt)
                            .queryParam(FSCParameters.END_BAS_DT.getParameterName(), endBasDt)
                            .queryParam(FSCParameters.LIKE_SRTN_CD.getParameterName(), srtnCd)
                            .queryParam(FSCParameters.SERVICE_KEY.getParameterName(), serviceKey)
                            .build();
                    try {
                        System.out.println(uri.toURL().toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return uri;
                })
                .retrieve()
                .bodyToMono(FSCStockPriceResponse.class)
                .block();
    }

    @Test
    public void 종목리스트_조회_후_종가데이터를_DynamoDB에_INSERT한다(){
        List<StockMetaDto> stockList = service.selectKrStockList("CORPCODE.xml");
        System.out.println(stockList.size());

        try {
            Path path = Paths.get(ClassLoader.getSystemResource("data_portal").toURI());
            if(Files.exists(Paths.get(path.toString(), "202205"))){

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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

            Optional.ofNullable(fscStockPriceResponse.getResponse().getBody().getTotalCount())
                    .ifPresent(totalCount -> {
                        PagingUtil.PageUnit pageUnit = PagingUtil.pageUnit(totalCount, 10);// 10 개의 구간으로 나눠서 진행하겠다. (한달평균 5만5천개일 경우 5500개씩 API 다운로드)
                        PagingUtil.iterateApiConsumer(pageUnit.getLimit(), 10, totalCount, d -> {
                            FSCStockPriceResponse r1 = requestAllStockPrice(webClient, encodedKey, "20220401", "20220430", d.getStartIndex(), pageUnit.getLimit());
                            // dynamoDB BatchWrite... 속도 잘 나오면 그냥 고고싱
//                            System.out.println("size =>> " + r1.getResponse().getBody().getItems().getItem().size());
//                            System.out.println(r1.getResponse().getBody().getItems().getItem());
//                            System.out.println(d.getStartIndex());
                        });
//                        FSCStockPriceResponse fscStockPriceResponse1 = requestAllStockPrice(webClient, encodedKey, "20220401", "20220430", 1L, pageUnit.getLimit());
//                        System.out.println(fscStockPriceResponse1.getResponse().getBody().getItems().getItem().size());
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    // 폐기 검토
    @Disabled
    @Test
    public void 종목리스트_조회_후_종가데이터를_불러온다_WEBCLIENT(){
        List<StockMetaDto> stockList = service.selectKrStockList("CORPCODE.xml");
        System.out.println(stockList.size());

        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.47")
                .defaultHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .defaultHeader("accept-encoding", "gzip, deflate, br")
                .defaultHeader("accept-language", "ko,en;q=0.9,en-US;q=0.8")
                .build();

        List<Future<FSCStockPriceResponse>> futureList = new ArrayList<>();

        subListAccept(stockList, (l)->{
            l.stream()
                .forEach(stockMetaDto -> {
                    CompletableFuture<FSCStockPriceResponse> future = CompletableFuture.supplyAsync(() -> {
                        long start = System.nanoTime();
                        FSCStockPriceResponse fscStockPriceResponse = requestStockPrice(webClient, NOT_ENCODED_SERVICE_KEY, stockMetaDto.getTicker(), "20220401", "20220430");
                        long diff = System.nanoTime() - start;
                        System.out.println("cost : " + (diff / 1000000) + " ms.");

                        if (fscStockPriceResponse.getResponse().getBody().getItems().getItem().size() == 0) return null;

                        List<StockPriceDto> list = fscStockPriceResponse.getResponse().getBody().getItems().getItem()
                                .stream()
                                .map(FSCStockPriceItem::toStockPriceDto)
                                .toList();

                        long redisStart = System.nanoTime();
                        long redisDiff = System.nanoTime() - redisStart;

                        StringBuilder builder = new StringBuilder();
                        builder.append("redisCost : ").append((redisDiff / 1000000)).append(" ms. ( ").append(redisDiff).append(" ) nanoSecond.");
//                        System.out.println("redisCost : " + (redisDiff / 1000000) + " ms. ( " + redisDiff + " ) nanoSecond.");
                        System.out.println(builder.toString());

                        return fscStockPriceResponse;
                    }, apiExecutor);
                    futureList.add(future);
                });
        }, 1700);

        futureList.stream()
                    .forEach(fscStockPriceResponseFuture -> {
                        Optional.ofNullable(fscStockPriceResponseFuture)
                                .ifPresent(future->{
                                    try {
                                        FSCStockPriceResponse fscStockPriceResponse = fscStockPriceResponseFuture.get();
//                                        System.out.println("ticker = " + fcsStockTickerListTemplate.opsForList().size("2022"));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                });
                    });

        System.out.println(">>>>>>");
//        System.out.println(dat.getResponse().getBody().getItems().getItem());
    }

    // 폐기 검토
    public <T>  void subListAccept(List<T> stockList, Consumer<List<T>> consumer, int size){
        PagingUtil.PageUnit pageUnit = PagingUtil.pageUnit(stockList, size);
        PagingUtil.iterateList(pageUnit, stockList, consumer);
    }

}
