package io.stock.evaluation.web.crawling.stock.price.type;

import io.stock.evaluation.web.crawling.stock.price.dto.CrawlingData;
import io.stock.evaluation.web.global.common.StringParser;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public enum CrawlingDataType {
    PER("per"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.per(toBigDecimal(StringParser.toDecimal().apply(value)));
        }
    },
    EPS("eps"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.eps(toBigDecimal(StringParser.toDecimal().apply(value)));
        }
    },
    PBR("pbr"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.pbr(toBigDecimal(StringParser.toDecimal().apply(value)));
        }
    },
    DVR("dvr"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.dvr(toBigDecimal(StringParser.toDecimal().apply(value)));
        }
    },
    PRICE("price"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.price(toBigDecimal(StringParser.toDecimal().apply(value)));
        }
    },
    MARKET_SUM("market_sum"){
        @Override
        public void bindParameter(CrawlingData.CrawlingDataBuilder builder, String value) {
            builder.marketSum(value);
        }
    };

    public BigDecimal toBigDecimal(Optional<BigDecimal> value){
        if(value.isEmpty()) return BigDecimal.ZERO;
        return value.get();
    }

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
