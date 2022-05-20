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
