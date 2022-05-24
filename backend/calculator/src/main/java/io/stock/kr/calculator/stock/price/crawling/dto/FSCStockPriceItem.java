package io.stock.kr.calculator.stock.price.crawling.dto;

import io.stock.kr.calculator.stock.price.StockPriceDto;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDocument;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDocumentId;
import io.stock.kr.calculator.stock.price.repository.redis.StockPriceRedis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    public PriceDayDocument toPriceDayDocument(){
        PriceDayDocumentId priceDayDocumentId = PriceDayDocumentId.builder()
                .ticker(this.getSrtnCd())
                .tradeDt(LocalDateTime.of(LocalDate.parse(this.basDt, DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(15, 30, 0)))
                .build();

        return PriceDayDocument.builder()
                .priceDayDocumentId(priceDayDocumentId)
                .open(new BigDecimal(this.getMkp()))
                .high(new BigDecimal(this.getHipr()))
                .low(new BigDecimal(this.getLopr()))
                .close(new BigDecimal(this.getClpr()))
                .build();
    }

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
