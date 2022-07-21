package io.stock.evaluation.web.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.*;

@Configuration
@Profile("live")
@EnableDynamoDBRepositories(
    basePackages = {
            "io.stock.evaluation.reactive_data"
    },
    includeFilters = {
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = "io.stock.evaluation.reactive_data.*.repository.dynamo.*Repository")
    }
)
public class DynamoDbConfig {
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
                        "-",
                        "-"
                )
        );

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();

        return amazonDynamoDB;
    }
}
