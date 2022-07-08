package io.stock.evaluation.reactive_data.crawling.stock.price.type;

import io.stock.evaluation.reactive_data.crawling.stock.price.dto.CrawlingData;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CrawlingDataType {
    PER("per"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.per(value);
        }
    },
    EPS("eps"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.eps(value);
        }
    },
    PBR("pbr"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.pbr(value);
        }
    },
    DVR("dvr"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.dvr(value);
        }
    },
    MARKET_SUM("market_sum"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.marketSum(value);
        }
    };

    private final String dataName;

    CrawlingDataType(String dataName){
        this.dataName = dataName;
    }

    public abstract void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value);

    private static final Map<String, CrawlingDataType> typeMap = new HashMap<>();

    static{
        for(CrawlingDataType type : CrawlingDataType.values()){
            typeMap.putIfAbsent(type.getDataName(), type);
            typeMap.putIfAbsent(type.getDataName().toUpperCase(), type);
        }
    }

    public static CrawlingDataType typeOf(String dataName){
        return typeMap.get(dataName);
    }
}
