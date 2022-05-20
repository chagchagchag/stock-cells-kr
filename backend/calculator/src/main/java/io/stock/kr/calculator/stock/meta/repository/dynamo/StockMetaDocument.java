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
