package io.stock.evaluation.reactive_data.ticker.meta.dto;

import lombok.Builder;

public class TickerMetaItem {
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
