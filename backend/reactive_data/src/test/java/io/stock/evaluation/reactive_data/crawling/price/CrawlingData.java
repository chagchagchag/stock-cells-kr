package io.stock.evaluation.reactive_data.crawling.price;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CrawlingData {
    private final String per;
    private final String pbr;
    private final String eps;
    private final String marketSum;
    private final String dvr;

    public CrawlingData(CrawlingDataBuilder builder){
        this.per = builder.per;
        this.pbr = builder.pbr;
        this.eps = builder.eps;
        this.marketSum = builder.marketSum;
        this.dvr = builder.dvr;
    }

    public static class CrawlingDataBuilder{
        private String per;
        private String pbr;
        private String eps;
        private String marketSum;
        private String dvr;

        public CrawlingDataBuilder(){}

        public CrawlingDataBuilder builder(){
            return new CrawlingDataBuilder();
        }

        public CrawlingDataBuilder per(String per){
            this.per = per;
            return this;
        }

        public CrawlingDataBuilder pbr(String pbr){
            this.pbr = pbr;
            return this;
        }

        public CrawlingDataBuilder eps(String eps){
            this.eps = eps;
            return this;
        }

        public CrawlingDataBuilder marketSum(String marketSum){
            this.marketSum = marketSum;
            return this;
        }

        public CrawlingDataBuilder dvr(String dvr){
            this.dvr = dvr;
            return this;
        }

        public CrawlingData build(){
            return new CrawlingData(this);
        }
    }
}
