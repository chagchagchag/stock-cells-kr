package io.stock.kr.calculator.stock.price.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.stock.kr.calculator.common.LocalDateTimeConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@DynamoDBTable(tableName = "PriceDayKr")
public class PriceDayDocument {
    @Id
    @DynamoDBAttribute
    @DynamoDBHashKey
    @DynamoDBIndexHashKey(globalSecondaryIndexNames = "tickerTradeDtGSI")
    String ticker;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "tickerTradeDtGSI")
    LocalDateTime tradeDt;

    @DynamoDBAttribute
    private BigDecimal open;

    @DynamoDBAttribute
    private BigDecimal high;

    @DynamoDBAttribute
    private BigDecimal low;

    @DynamoDBAttribute
    private BigDecimal close;

    public PriceDayDocument(){}

    @Builder
    public PriceDayDocument(
            String ticker, LocalDateTime tradeDt,
            BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close
    ){
        this.ticker = ticker;
        this.tradeDt = tradeDt;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

}