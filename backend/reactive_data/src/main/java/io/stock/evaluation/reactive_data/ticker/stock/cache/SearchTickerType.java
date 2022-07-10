package io.stock.evaluation.reactive_data.ticker.meta.cache;

import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerStockDto;

public enum SearchTickerType {
    BY_TICKER("BY_TICKER"){
        @Override
        public String expectedKey(TickerStockDto tickerStockDto) {
            return tickerStockDto.getTicker();
        }
    },
    BY_COMPANY_NAME("BY_COMPANY_NAME"){
        @Override
        public String expectedKey(TickerStockDto tickerStockDto) {
            return tickerStockDto.getCompanyName();
        }
    };

    private final String typeName;

    SearchTickerType(String typeName){
        this.typeName = typeName;
    }

    public abstract String expectedKey(TickerStockDto tickerStockDto);
}
