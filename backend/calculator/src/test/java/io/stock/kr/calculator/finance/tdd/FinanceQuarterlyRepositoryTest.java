package io.stock.kr.calculator.finance.tdd;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.stock.kr.calculator.dynamo.FinanceGainLossQuarterly;
import io.stock.kr.calculator.dynamo.FinanceGainLossQuarterlyRepository;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test-docker")
public class FinanceQuarterlyRepositoryTest {

    @Autowired
    FinanceGainLossQuarterlyRepository repository;

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @Autowired
    DynamoDBMapper dynamoDBMapper;

    @BeforeEach
    public void beforeTest(){
//        DockerLocalDynamoDbConfigUtils.LocalDynamoDbEnvironment localDynamoDbEnvironment = DockerLocalDynamoDbConfigUtils.newLocalDynamoDbEnvironment();
//        amazonDynamoDB = localDynamoDbEnvironment.amazonDynamoDB();
//        dynamoDBMapper = localDynamoDbEnvironment.dynamoDBMapper();
//
//        CreateTableRequest request = dynamoDBMapper
//                .generateCreateTableRequest(FinanceGainLossQuarterly.class, DynamoDBMapperConfig.DEFAULT)
//                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
//
//        TableUtils.createTableIfNotExists(amazonDynamoDB, request);
    }

    @AfterEach
    public void afterTest(){
        DeleteTableRequest deleteTableRequest = dynamoDBMapper
                .generateDeleteTableRequest(FinanceGainLossQuarterly.class, DynamoDBMapperConfig.DEFAULT);

        TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);
    }

    @Test
    public void TEST(){
        DeleteTableRequest deleteTableRequest = dynamoDBMapper
                .generateDeleteTableRequest(FinanceGainLossQuarterly.class, DynamoDBMapperConfig.DEFAULT);

        TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);

        CreateTableRequest request = dynamoDBMapper
                .generateCreateTableRequest(FinanceGainLossQuarterly.class, DynamoDBMapperConfig.DEFAULT)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, request);

        FinanceGainLossQuarterly msft = FinanceGainLossQuarterly.builder()
                .ticker("MSFT")
                .priceDate(LocalDateTime.now())
                .build();

        repository.save(msft);

        BDDAssertions.then(repository.findById(msft.getTicker())).isNotEmpty();

        assertThat(repository.findAllByTicker(msft.getTicker())).hasSize(1);
        assertThat(repository.findAll()).hasSize(1);
    }
}
