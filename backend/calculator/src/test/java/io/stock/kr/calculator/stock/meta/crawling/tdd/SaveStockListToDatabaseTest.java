package io.stock.kr.calculator.stock.meta.crawling.tdd;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.stock.kr.calculator.dynamo.StockMeta;
import io.stock.kr.calculator.dynamo.StockMetaRepository;
import io.stock.kr.calculator.stock.meta.crawling.StockMetaCrawlingDartService;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test-docker")
public class SaveStockListToDatabaseTest {

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
        deleteTableIfExist();
        createTableRequest();
    }

    @AfterEach
    public void afterTest(){
        deleteTableIfExist();
    }

    public void deleteTableIfExist(){
        DeleteTableRequest deleteTableRequest = dynamoDBMapper
                .generateDeleteTableRequest(StockMeta.class, DynamoDBMapperConfig.DEFAULT);

        TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);
    }

    public void createTableRequest(){
        CreateTableRequest createTableRequest = dynamoDBMapper
                .generateCreateTableRequest(StockMeta.class, DynamoDBMapperConfig.DEFAULT)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
    }

    @Test
    public void 크롤링되어있는_xml_파일을_읽어들여서_디비에_저장하는_작업을_수행한다(){
        List<StockMetaDto> stockList = service.selectKrStockList("CORPCODE-TEST.xml");

        List<StockMeta> stocks = stockList.stream()
                .map(stockMetaDto -> {
                    return StockMeta.builder()
                            .ticker(stockMetaDto.getTicker())
                            .companyName(stockMetaDto.getCompanyName())
                            .vendorCode(stockMetaDto.getVendorCode())
                            .vendorType(stockMetaDto.getVendorType())
                            .build();
                })
                .collect(Collectors.toList());

        dynamoDBMapper.batchWrite(stocks, new ArrayList<>());

        Iterable<StockMeta> stockMetaIter = repository.findAll();

        Iterator<StockMeta> iterator = stockMetaIter.iterator();

        while(iterator.hasNext()){
            StockMeta next = iterator.next();
            System.out.println(next);
        }
    }
}
