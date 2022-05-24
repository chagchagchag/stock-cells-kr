package io.stock.kr.calculator.stock.price.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PriceDayDynamoDBMapper {
    private final DynamoDBMapper dynamoDBMapper;

    public PriceDayDynamoDBMapper(DynamoDBMapper dynamoDBMapper){
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public void batchWritePriceDayList(List<PriceDayDocument> priceDayDocumentList){
//        DynamoDBMapperConfig.SaveBehavior.PUT
//        DynamoDBMapperConfig.builder().withSaveBehavior()
        dynamoDBMapper.batchWrite(priceDayDocumentList, new ArrayList<>());
    }

    public void batchDelete(List<PriceDayDocument> priceDayDocumentList){
        dynamoDBMapper.batchDelete(priceDayDocumentList);
    }
}
