package io.stock.evaluation.reactive_data.ticker.meta.cache;

import lombok.Getter;

@Getter
public class TickerAutoCompleteSearchKeyGenerator {
//    final String prefix = "AUTO-COMPLETE";
    final String prefix = TickerCachePrefixType.AUTO_COMPLETE.getCachePrefixTypeName();
    final String separator = "###";
    final String finisher = "§§§";

    final String companyName;

    public TickerAutoCompleteSearchKeyGenerator(String companyName){
        this.companyName = companyName;
    }

    /**
     * @return "AUTO-COMPLETE###삼###4
     */
    public String generateKey(){
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(separator)
                .append(companyName.substring(0,1)).append(separator)
                .append(companyName.length());
        return builder.toString();
    }

    /**
     * @return "AUTO-COMPLETE###삼###
     */
    public String searchKey(){
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(separator)
                .append(companyName.substring(0,1)).append(separator);
        return builder.toString();
    }
}
