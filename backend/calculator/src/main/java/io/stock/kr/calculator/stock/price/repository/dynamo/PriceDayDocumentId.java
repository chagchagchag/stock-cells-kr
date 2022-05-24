package io.stock.kr.calculator.stock.price.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import io.stock.kr.calculator.common.LocalDateTimeConverter;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceDayDocumentId implements Serializable {
    @DynamoDBHashKey(attributeName = "ticker")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "PriceDayTickerTradeDtGSI")
    private String ticker;

    @DynamoDBRangeKey(attributeName = "tradeDt")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "PriceDayTickerTradeDtGSI")
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime tradeDt;
}
