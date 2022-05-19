package io.stock.kr.calculator.stock.meta.crawling.tdd;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.stock.kr.calculator.dynamo.StockMetaDocument;
import io.stock.kr.calculator.dynamo.StockMetaRepository;
import io.stock.kr.calculator.stock.meta.crawling.StockMetaCrawlingDartService;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import io.stock.kr.calculator.stock.meta.crawling.tdd.dto.FSCStockPriceResponse;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

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
        LIKE_STRN_CD("likeStrnCd"),
        SERVICE_KEY("serviceKey");

        private final String parameterName;

        FSCParameters(String parameterName){
            this.parameterName = parameterName;
        }

    }

    @Test
    public void 종목리스트_조회_후_종가데이터를_불러온다_WEBCLIENT(){
        List<StockMetaDto> stockList = service.selectKrStockList("CORPCODE.xml");
        System.out.println(stockList.size());

        // serviceKey 는 공식페이지에서 제공하는 키가 2개 있다.
        // decoding 된 키 : API 통신 등에서 내부 통신에서 사용할 때 사용하는 키
        //  = 만약 라이브러리 내에서 내부적으로 encoding 을 기본적으로 수행하게끔 지원된다면, decoding 된 키를 지정해야 한다.
        //  = WebClient 의 경우는 통신시에 내부적으로(자동으로) URL Encoding 을 수행하기에 decoding 된 키를 사용하게끔 지정했다.
        //  (이걸로 하루 내내 원인을 못찾고 헤맴. 로그 자체도 단순히 SERVICE_KEY_NOT_REGISTERED 가 뜨기에 ServiceKey 가 잘못되었다고 판단해서 키를 다시 발급받는 등의 행동을 해야만 하게 된다.)
        // encoding 된 키 : 공식페이지에서 제공하는 SWAGGER 상에서 HTTP 통신을 할때에는 HTTP URL 형식에 맞게끔 base64 인코딩되어 있는 키
        final String serviceKey = "인코딩하지 않은 순수한 키";
        final String BASE_URL = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService";

        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.47")
                .defaultHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .defaultHeader("accept-encoding", "gzip, deflate, br")
                .defaultHeader("accept-language", "ko,en;q=0.9,en-US;q=0.8")
                .build();

        FSCStockPriceResponse dat = webClient.get()
                .uri(uriBuilder -> {
                    URI uri = uriBuilder
                            .path("/getStockPriceInfo")
                            .queryParam(FSCParameters.NUM_OF_ROWS.getParameterName(), "365")
                            .queryParam(FSCParameters.PAGE_NO.getParameterName(), "1")
                            .queryParam(FSCParameters.RESULT_TYPE.getParameterName(), "json")
                            .queryParam(FSCParameters.BEGIN_BAS_DT.getParameterName(), "20220501")
                            .queryParam(FSCParameters.LIKE_STRN_CD.getParameterName(), "009150")
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

        System.out.println(dat.getResponse().getBody().getItems().getItem());
    }
}
