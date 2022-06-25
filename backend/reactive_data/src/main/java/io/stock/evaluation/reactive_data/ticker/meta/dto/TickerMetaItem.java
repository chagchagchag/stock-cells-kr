package io.stock.evaluation.reactive_data.ticker.meta.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TickerMetaItem implements Serializable {
    private String ticker;
    private String companyName;
    private String tickerCode; // vendor specific code

    @Builder
    public TickerMetaItem(String ticker, String companyName, String tickerCode){
        this.ticker = ticker;
        this.companyName = companyName;
        this.tickerCode = tickerCode;
    }
}
