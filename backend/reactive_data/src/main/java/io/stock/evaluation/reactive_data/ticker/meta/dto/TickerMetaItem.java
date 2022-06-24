package io.stock.evaluation.reactive_data.ticker.meta.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class TickerMetaItem implements Serializable {
    private final String ticker;
    private final String companyName;
    private final String tickerCode; // vendor specific code

    @Builder
    public TickerMetaItem(String ticker, String companyName, String tickerCode){
        this.ticker = ticker;
        this.companyName = companyName;
        this.tickerCode = tickerCode;
    }
}
