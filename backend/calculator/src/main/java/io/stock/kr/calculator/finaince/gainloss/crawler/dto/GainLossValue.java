package io.stock.kr.calculator.finaince.gainloss.crawler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class GainLossValue {
    private GainLossColumn gainLossColumn;
    private BigDecimal firstPrevData;
    private BigDecimal secondPrevData;
    private BigDecimal thirdPrevData;
    private BigDecimal fourthPrevData;

    @Builder
    public GainLossValue(
            GainLossColumn gainLossColumn,
            BigDecimal firstPrevData,
            BigDecimal secondPrevData,
            BigDecimal thirdPrevData,
            BigDecimal fourthPrevData
    ){
        this.gainLossColumn = gainLossColumn;
        this.firstPrevData = firstPrevData;
        this.secondPrevData = secondPrevData;
        this.thirdPrevData = thirdPrevData;
        this.fourthPrevData = fourthPrevData;
    }
}
