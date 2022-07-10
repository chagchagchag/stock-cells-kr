package io.stock.evaluation.reactive_data.ticker.meta.cache;

import lombok.Getter;

@Getter
public enum TickerCachePrefixType {
    SEARCH_TICKER("SEARCH-TICKER"),
    AUTO_COMPLETE("AUTO-COMPLETE");

    private final String cachePrefixTypeName;

    TickerCachePrefixType(String cachePrefixTypeName){
        this.cachePrefixTypeName = cachePrefixTypeName;
    }
}
