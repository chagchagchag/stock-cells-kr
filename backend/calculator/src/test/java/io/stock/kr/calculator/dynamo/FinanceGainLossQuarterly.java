package io.stock.kr.calculator.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.stock.kr.calculator.common.LocalDateTimeConverter;
import lombok.*;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@ToString
@EnableScan
@DynamoDBTable(tableName = "FinanceGainLossQuarter")
@NoArgsConstructor
public class FinanceGainLossQuarterly {

    @Id
    @Getter @Setter
    @DynamoDBHashKey(attributeName = "Ticker")
//    @DynamoDBIndexHashKey
    private String ticker;

    @Getter @Setter
    @DynamoDBAttribute
//    @DynamoDBRangeKey
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime priceDate;

    @Builder
    public FinanceGainLossQuarterly(String ticker, LocalDateTime priceDate){
//    public FinanceGainLossQuarterly(String ticker){
        this.ticker = ticker;
        this.priceDate = priceDate;
    }
}
