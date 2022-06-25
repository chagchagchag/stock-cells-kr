package io.stock.evaluation.reactive_data.ticker.meta.cache;

import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import lombok.Getter;

@Getter
public class SearchTickerKeyBuilder {
    private final StringBuilder builder = new StringBuilder();
    private final String prefix = TickerCachePrefixType.SEARCH_TICKER.getCachePrefixTypeName();
    private final String SEPARATOR = "###";

    private final TickerMetaItem tickerMetaItem;
    private final SearchTickerType searchTickerType;

    public SearchTickerKeyBuilder(
            SearchTickerType searchTickerType
    ){
        this.searchTickerType = searchTickerType;
        this.tickerMetaItem = null;
    }

    public SearchTickerKeyBuilder(
            SearchTickerType searchTickerType,
            TickerMetaItem tickerMetaItem
    ){
        this.searchTickerType = searchTickerType;
        this.tickerMetaItem = tickerMetaItem;
    }

    public static SearchTickerKeyBuilder newSearchKeyBuilder(SearchTickerType searchTickerType){
        return new SearchTickerKeyBuilder(searchTickerType);
    }

    public static SearchTickerKeyBuilder newGenerateKeyBuilder(SearchTickerType searchTickerType, TickerMetaItem tickerMetaItem){
        return new SearchTickerKeyBuilder(searchTickerType, tickerMetaItem);
    }

    public String generateKey(){
        return builder
                .append(prefix).append(SEPARATOR)
                .append(searchTickerType.expectedKey(tickerMetaItem)).toString();
    }

    public String searchKey(String query){
        return builder
                .append(prefix).append(SEPARATOR)
                .append(query).toString();
    }
}
