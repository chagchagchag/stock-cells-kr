package io.stock.evaluation.reactive_data.ticker.meta.cache;

import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;

public enum SearchTickerType {
    BY_TICKER("BY_TICKER"){
        @Override
        public String expectedKey(TickerMetaItem tickerMetaItem) {
            return tickerMetaItem.getTicker();
        }
    },
    BY_COMPANY_NAME("BY_COMPANY_NAME"){
        @Override
        public String expectedKey(TickerMetaItem tickerMetaItem) {
            return tickerMetaItem.getCompanyName();
        }
    };

    private final String typeName;

    SearchTickerType(String typeName){
        this.typeName = typeName;
    }

    public abstract String expectedKey(TickerMetaItem tickerMetaItem);
}
