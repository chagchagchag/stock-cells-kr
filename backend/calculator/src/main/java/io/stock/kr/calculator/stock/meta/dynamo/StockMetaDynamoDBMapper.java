package io.stock.kr.calculator.stock.meta.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.stock.kr.calculator.dynamo.StockMeta;
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

    public void batchWriteStockMetaList(List<StockMeta> stockMetaList){
        dynamoDBMapper.batchWrite(stockMetaList, new ArrayList<>());
    }
}
