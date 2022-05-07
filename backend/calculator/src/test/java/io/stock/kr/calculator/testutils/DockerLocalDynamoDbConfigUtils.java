package io.stock.kr.calculator.testutils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class DockerLocalDynamoDbConfigUtils {
    public record LocalDynamoDbEnvironment(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper){}

    public static LocalDynamoDbEnvironment newLocalDynamoDbEnvironment(){
        AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:5555",
                "ap-northeast-2"
        );

        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(
                        "ACCESS KEY",
                        "SECRET KEY"
                )
        );

        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion("ap-northeast-2")
                .withCredentials(credentialsProvider)
                .build();

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT);

        return new LocalDynamoDbEnvironment(amazonDynamoDB, dynamoDBMapper);
    }
}
