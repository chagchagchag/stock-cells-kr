package io.stock.kr.calculator.stock.meta.crawling.tdd;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.stock.kr.calculator.stock.meta.repository.dynamo.StockMetaDocument;
import io.stock.kr.calculator.stock.meta.repository.dynamo.StockMetaRepository;
import io.stock.kr.calculator.stock.meta.crawling.StockMetaCrawlingDartService;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 기능 완료 후 test-docker 에서만 동작하도록 테스트케이스 변경 및 세부 테스트 게이스 분리 예정
 */
@SpringBootTest
@ActiveProfiles("live")
public class LiveSaveStockMeataSaveTest {
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

    @Disabled
    @Test
    public void DART_제공_상장주식_리스트_파일_조회(){
        List<StockMetaDto> stockList = service.selectKrStockList("CORPCODE.xml");
        System.out.println(stockList.size());
    }

    @Disabled
    @Test
    public void 이미_존재하는_데이터_삭제_1(){
        service.batchDeleteStockList(repository.findAll());
    }

    @Disabled
    @Test
    public void 이미_존재하는_데이터_삭제_2(){
        service.batchDeleteStockListAll();
    }

    @Disabled
    @Test
    public void 크롤링되어있는_xml_파일을_읽어들여서_디비에_저장하는_작업을_수행(){
        List<StockMetaDto> stockList = service.selectKrStockList("CORPCODE.xml");

        service.batchWriteStockList(stockList);

        List<StockMetaDocument> list = repository.findAll();

        assertThat(list).isNotEmpty();
    }

}
