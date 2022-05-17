package io.stock.kr.calculator.common.types;

import lombok.Getter;

public enum CrawlingVendorType {
    FNGUIDE("FNGUIDE"),
    DART("DART"),
    KRX("KRX");

    @Getter
    private String vendorTypeName;

    CrawlingVendorType(String vendorTypeName){
        this.vendorTypeName = vendorTypeName;
    }
}
