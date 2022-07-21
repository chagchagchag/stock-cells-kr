package io.stock.evaluation.web.crawling.stock.price.dto;

import io.stock.evaluation.web.crawling.stock.price.type.CrawlingDataType;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class CrawlingData {
    private final BigDecimal per;
    private final BigDecimal pbr;
    private final BigDecimal eps;
    private final String marketSum;
    private final BigDecimal dvr;
    private final BigDecimal price;

    public CrawlingData(CrawlingDataBuilder builder){
        this.per = builder.per;
        this.pbr = builder.pbr;
        this.eps = builder.eps;
        this.marketSum = builder.marketSum;
        this.dvr = builder.dvr;
        this.price = builder.price;
    }

    public static class CrawlingDataBuilder{
        private BigDecimal per;
        private BigDecimal pbr;
        private BigDecimal eps;
        private String marketSum;
        private BigDecimal dvr;
        private BigDecimal price;

        public CrawlingDataBuilder(){}

        public CrawlingDataBuilder builder(){
            return new CrawlingDataBuilder();
        }

        public CrawlingDataBuilder bindParameter(String paramName, String value){
            CrawlingDataType.typeOf(paramName).bindParameter(this, value);
            return this;
        }

        public CrawlingDataBuilder per(BigDecimal per){
            this.per = per;
            return this;
        }

        public CrawlingDataBuilder pbr(BigDecimal pbr){
            this.pbr = pbr;
            return this;
        }

        public CrawlingDataBuilder eps(BigDecimal eps){
            this.eps = eps;
            return this;
        }

        public CrawlingDataBuilder marketSum(String marketSum){
            this.marketSum = marketSum;
            return this;
        }

        public CrawlingDataBuilder dvr(BigDecimal dvr){
            this.dvr = dvr;
            return this;
        }

        public CrawlingDataBuilder price(BigDecimal price){
            this.price = price;
            return this;
        }

        public CrawlingData build(){
            return new CrawlingData(this);
        }
    }
}
