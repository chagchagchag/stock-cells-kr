package io.stock.kr.calculator.stock.meta.crawling.dto;

import io.stock.kr.calculator.common.types.CrawlingVendorType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StockMetaDto {
    final private String ticker;
    final private String companyName;
    final private String vendorCode;
    final private CrawlingVendorType vendorType;

    @Builder
    public StockMetaDto(String ticker, String companyName, String vendorCode, CrawlingVendorType vendorType){
        this.ticker = ticker;
        this.companyName = companyName;
        this.vendorCode = vendorCode;
        this.vendorType = vendorType;
    }
}
