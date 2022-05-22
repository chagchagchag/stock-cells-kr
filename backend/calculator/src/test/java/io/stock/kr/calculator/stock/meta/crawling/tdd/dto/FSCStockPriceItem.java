package io.stock.kr.calculator.stock.meta.crawling.tdd.dto;

import io.stock.kr.calculator.stock.price.StockPriceDto;
import io.stock.kr.calculator.stock.price.repository.redis.StockPriceRedis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FSCStockPriceItem {
    private String basDt;
    private String srtnCd;
    private String isinCd;
    private String itmsNm;
    private String mrktCtg;
    private String vs;
    private String mkp;   // open
    private String hipr;  // high
    private String lopr;  // low
    private String clpr;  // close

    public StockPriceRedis toStockPriceRedis(){
        return StockPriceRedis.builder()
                .tradeDt(this.getBasDt())
                .ticker(this.getSrtnCd())
                .isinCd(this.getIsinCd())
                .itmsNm(this.getItmsNm())
                .mrktCtg(this.getMrktCtg())
                .vs(this.getVs())
                .open(this.getMkp())
                .high(this.getHipr())
                .low(this.getLopr())
                .close(this.getClpr())
                .build();
    }

    public StockPriceDto toStockPriceDto(){
        return StockPriceDto.builder()
                .basDt(this.getBasDt())
                .srtnCd(this.getSrtnCd())
                .isinCd(this.getIsinCd())
                .itmsNm(this.getItmsNm())
                .mrktCtg(this.getMrktCtg())
                .vs(this.getVs())
                .mkp(new BigDecimal(this.getMkp()))
                .hipr(new BigDecimal(this.getHipr()))
                .lopr(new BigDecimal(this.getLopr()))
                .clpr(new BigDecimal(this.getClpr()))
                .build();
    }
}
