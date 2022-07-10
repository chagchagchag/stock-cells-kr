package io.stock.evaluation.reactive_data.ticker.meta.cache;

import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerStockDto;
import lombok.Getter;

@Getter
public class TickerSearchKeyGenerator {
    private final StringBuilder builder = new StringBuilder();
    private final String prefix = TickerCachePrefixType.SEARCH_TICKER.getCachePrefixTypeName();
    private final String SEPARATOR = "###";

    private final TickerStockDto tickerStockDto;
    private final SearchTickerType searchTickerType;

    public TickerSearchKeyGenerator(
            SearchTickerType searchTickerType
    ){
        this.searchTickerType = searchTickerType;
        this.tickerStockDto = null;
    }

    public TickerSearchKeyGenerator(
            SearchTickerType searchTickerType,
            TickerStockDto tickerStockDto
    ){
        this.searchTickerType = searchTickerType;
        this.tickerStockDto = tickerStockDto;
    }

    public static TickerSearchKeyGenerator newGeneratorForSearch(SearchTickerType searchTickerType){
        return new TickerSearchKeyGenerator(searchTickerType);
    }

    public static TickerSearchKeyGenerator newGeneratorForGenerate(SearchTickerType searchTickerType, TickerStockDto tickerStockDto){
        return new TickerSearchKeyGenerator(searchTickerType, tickerStockDto);
    }

    public String generateKey(){
        return builder
                .append(prefix).append(SEPARATOR)
                .append(searchTickerType.expectedKey(tickerStockDto)).toString();
    }

    public String searchKey(String query){
        return builder
                .append(prefix).append(SEPARATOR)
                .append(query).toString();
    }
}
