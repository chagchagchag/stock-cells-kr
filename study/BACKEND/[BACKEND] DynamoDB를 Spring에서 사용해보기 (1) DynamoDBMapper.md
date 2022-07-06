# DynamoDB를 Spring에서 사용해보기 (1) DynamoDBMapper



# 의존성 추가

```groovy
	// data-jpa
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	
	// dynamodb
	implementation 'com.amazonaws:aws-java-sdk-dynamodb:1.12.224'
	implementation 'io.github.boostchicken:spring-data-dynamodb:5.2.5'
```

<br>

# DynamoDB Configuration

## DynamoDBConfig

> 라이브에서는 dynamodb의 region 서버에 Credential, Secret Key 를 기반으로 로그인하는 코드를 작성한다.

<br>

먼저 상용 DB 접속 설정 코드다. 요약하자면 이렇다.<br>

- DynamoDB 접속정보를 바인딩하는 용도의 `AmazonDynamoDB` 객체를 생성해서 빈으로 등록해둔다.
- DynamoDBMapper 객체를 생성해서 Bean으로 등록해둔다.
  - 접속정보 세팅을 위해 DynamoDBMapper 클래스의 생성자는 AmazonDynamoDB 객체를 인자로 받는다. 즉, DynamoDBMapper 객체 생성과 동시에 접속정보 객체인 AmazonDynamoDB 를 내부적으로 바인딩한다.
  - DynamoDBMapper 는 aws 에서 DynamoDB 접속을 위해 만들어둔 라이브러리이다.
  - JDBCTemplate 과 유사해 보이는 메서드들이 많이 보이는 라이브러리다.
  - 자세한 내용은 추후 DynamoDB 관련된 예제와 짤막한 개념들을 정리하면서 정리하게 될듯.

<br>

```java
package io.stock.kr.calculator.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.*;

@Profile("live")
@Configuration
@EnableDynamoDBRepositories(
        basePackages = {
                "io.stock.kr.calculator"
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "io.stock.kr.calculator.*.repository.dynamo.*Repository"),
        }
)
public class DynamoDBConfig {
    @Profile({"live"})
    @Bean
    @Primary
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB){
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Profile("live")
    @Bean
    public AmazonDynamoDB amazonDynamoDB(){
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        "---",
                        "---"
                )
        );

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();

        return amazonDynamoDB;
    }
}
```

<br>

## application.yml

라이브 설정에서 지정해준 접속정보는 아래와 같다.

```yaml
spring:
  profiles:
    active: live
---
spring:
  config:
    activate:
      on-profile: live
  redis:
    host: --
    port: 6379
amazon:
  dynamodb:
    endpoint: "https://dynamodb.ap-northeast-2.amazonaws.com:8000"
    region: "ap-northeast-2"
  aws:
    accessKey: "key"
    secretKey: "key"
```

<br>

## TestDynamoDBConfig

테스트 용도의 DynamoDB 설정코드도 한벌 작성해두자. 한번 만들어두면 요긴하게 잘 쓰인다.

```java
package io.stock.kr.calculator.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.*;

@Configuration
@Profile({"test-docker", "test-in-memory", "test-live"})
@EnableDynamoDBRepositories(
        basePackages = {
                "io.stock.kr.calculator"
        },
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "io.stock.kr.calculator.*.repository.dynamo.*Repository"),
        }
)
public class TestDynamoDbConfig {
        @Profile({"test-docker", "test-in-memory", "test-live"})
        @Bean
        @Primary
        public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB){
                return new DynamoDBMapper(amazonDynamoDB);
        }

        @Profile({"test-docker", "test-live"})
        @Bean
        public AmazonDynamoDB amazonDynamoDB(){
                AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                                "ACCESS KEY",
                                "SECRET KEY"
                        )
                );

                EndpointConfiguration endpointConfiguration = new EndpointConfiguration(
                        "http://localhost:8000",
                        Regions.AP_NORTHEAST_2.getName()
                );

                AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                        .withCredentials(credentialsProvider)
                        .withEndpointConfiguration(endpointConfiguration)
                        .build();

                return amazonDynamoDB;
        }

        @Profile("test-in-memory")
        @Bean
        public AmazonDynamoDB inMemoryAmazonDB(){
                return null;
        }
}
```

<br>

# 모델클래스

테스트용으로 사용할 Document 를 정의해주자. 

```java
package io.stock.kr.calculator.stock.meta.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import io.stock.kr.calculator.common.types.CrawlingVendorType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.annotation.Id;

@ToString
@EnableScan
@DynamoDBTable(tableName = "StockMeta")
public class StockMetaDocument {
    public StockMetaDocument(){}

    @Id
    @Getter @Setter
    @DynamoDBHashKey(attributeName = "ticker")
    private String ticker;

    @Getter @Setter
    @DynamoDBAttribute
    private String companyName;

    @Getter @Setter
    @DynamoDBAttribute
    private String vendorCode;

    @Getter @Setter
    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "vendorType")
    private CrawlingVendorType vendorType;

    @Builder
    public StockMetaDocument(String ticker, String companyName, String vendorCode, CrawlingVendorType vendorType){
        this.ticker = ticker;
        this.companyName = companyName;
        this.vendorCode = vendorCode;
        this.vendorType = vendorType;
    }
}
```

<br>

# DynamoDBMapper 구현

세부적인 구현을 작성하고 컴포넌트로 등록해두었다.

```java
package io.stock.kr.calculator.stock.meta.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockMetaDynamoDBMapper {
    private final DynamoDBMapper dynamoDBMapper;

    public StockMetaDynamoDBMapper(
            DynamoDBMapper dynamoDBMapper
    ){
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void batchWriteStockMetaList(List<StockMetaDocument> stockMetaDocumentList){
        dynamoDBMapper.batchWrite(stockMetaDocumentList, new ArrayList<>());
    }

    public void batchDelete(List<StockMetaDocument> stockMetaDocumentList){
        dynamoDBMapper.batchDelete(stockMetaDocumentList);
    }
}
```

<br>

# 테스트 코드

```java
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
@ActiveProfiles("test-docker")
public class TestDockerSaveStockMeataSaveTest {
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
                .generateDeleteTableRequest(StockMetaDocument.class, DynamoDBMapperConfig.DEFAULT);

        TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);
    }

    public void createTableRequest(){
        CreateTableRequest createTableRequest = dynamoDBMapper
                .generateCreateTableRequest(StockMetaDocument.class, DynamoDBMapperConfig.DEFAULT)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
    }

		// ...

    
    @Test
    public void 크롤링되어있는_xml_파일을_읽어들여서_디비에_저장하는_작업을_수행(){
        List<StockMetaDto> stockList = service.selectKrStockList("CORPCODE.xml");

        service.batchWriteStockList(stockList);

        List<StockMetaDocument> list = repository.findAll();

        assertThat(list).isNotEmpty();
    }

}
```

