package io.stock.evaluation.web.ticker.stock.cache;

import io.stock.evaluation.web.ticker.stock.dto.TickerStockDto;

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
