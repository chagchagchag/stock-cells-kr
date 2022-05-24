package io.stock.kr.calculator.stock.price.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.stock.kr.calculator.common.LocalDateTimeConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@EnableScan
@DynamoDBTable(tableName = "PriceDay")
public class PriceDayDocument {
    @Id
    private PriceDayDocumentId priceDayDocumentId;

    @DynamoDBHashKey(attributeName = "ticker")
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "PriceDayTickerTradeDtGSI")
    public String getTicker(){
        return Optional.ofNullable(priceDayDocumentId.getTicker()).orElse("-");
    }

    public void setTicker(String ticker){
        if(Optional.ofNullable(priceDayDocumentId).isEmpty())
            priceDayDocumentId = new PriceDayDocumentId();
        priceDayDocumentId.setTicker(ticker);
    }

    @DynamoDBRangeKey(attributeName = "tradeDt")
//    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "PriceDayTickerTradeDtGSI")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    public LocalDateTime getTradeDt(){
        return priceDayDocumentId.getTradeDt();
    }

    public void setTradeDt(LocalDateTime tradeDt){
        priceDayDocumentId.setTradeDt(tradeDt);
    }

    @DynamoDBAttribute
    @Getter @Setter
    private BigDecimal open;

    @DynamoDBAttribute
    @Getter @Setter
    private BigDecimal high;

    @DynamoDBAttribute
    @Getter @Setter
    private BigDecimal low;

    @DynamoDBAttribute
    @Getter @Setter
    private BigDecimal close;

    public PriceDayDocument(){}

    @Builder
    public PriceDayDocument(
            PriceDayDocumentId priceDayDocumentId,
            BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close
    ){
        this.priceDayDocumentId = priceDayDocumentId;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

}