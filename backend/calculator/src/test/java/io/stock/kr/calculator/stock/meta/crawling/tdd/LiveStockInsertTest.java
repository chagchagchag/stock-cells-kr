package io.stock.kr.calculator.stock.meta.crawling.tdd;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDocument;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDocumentId;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDynamoDBMapper;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * docker 기반 테스트로 분리 예정
 */
@SpringBootTest
//@ActiveProfiles("test-docker")
@ActiveProfiles("live")
public class LiveStockInsertTest {

    @Autowired
    PriceDayDynamoDBMapper priceDayDynamoDBMapper;

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @Autowired
    DynamoDBMapper dynamoDBMapper;

    @Autowired
    PriceDayRepository repository;

    public void createTableRequest(){
        try{
            CreateTableRequest createTableRequest = dynamoDBMapper
                    .generateCreateTableRequest(PriceDayDocument.class, DynamoDBMapperConfig.DEFAULT)
                    .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

            createTableRequest.getGlobalSecondaryIndexes().forEach(idx -> {
                idx.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
                    .withProjection(new Projection().withProjectionType("ALL"));
            });

            TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Disabled
    @Test
    public void saveStock(){
//        createTableRequest();

        PriceDayDocumentId tslaId = PriceDayDocumentId.builder()
                .ticker("TSLA")
                .tradeDt(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)))
                .build();

        PriceDayDocument tsla = PriceDayDocument.builder()
                .priceDayDocumentId(tslaId)
                .open(new BigDecimal(222))
                .high(new BigDecimal(111))
                .low(new BigDecimal(111))
                .close(new BigDecimal(222))
                .build();

        PriceDayDocumentId aaplId = PriceDayDocumentId.builder()
                .ticker("AAPL")
                .tradeDt(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)))
                .build();

        PriceDayDocument aapl = PriceDayDocument.builder()
                .priceDayDocumentId(aaplId)
                .open(new BigDecimal(222))
                .high(new BigDecimal(111))
                .low(new BigDecimal(111))
                .close(new BigDecimal(222))
                .build();

        try{
//            repository.save(tsla);
            priceDayDynamoDBMapper.batchWritePriceDayList(List.of(tsla, aapl));
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getCause() + ", " + e.getMessage());
        }
    }

    @Test
    public void 이미_존재하는_모든_데이터_삭제_1(){
        createTableRequest();
        priceDayDynamoDBMapper.batchDelete(repository.findAll());
    }

}
