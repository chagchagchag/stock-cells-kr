package io.stock.evaluation.reactive_data.ticker.meta.cache;

import lombok.Getter;

@Getter
public class AutoCompleteTickerKeyBuilder {
    final String prefix = "AUTO-COMPLETE";
    final String separator = "###";
    final String finisher = "§§§";

    final String companyName;
    final StringBuilder builder = new StringBuilder();

    public AutoCompleteTickerKeyBuilder(String companyName){
        this.companyName = companyName;
    }

    public String generateKey(){
        builder.append(prefix).append(separator)
                .append(companyName.substring(0,1)).append(separator)
                .append(companyName.length());
        return builder.toString();
    }

    public String searchKey(){
        StringBuilder builder = new StringBuilder();
        builder.append(prefix).append(separator)
                .append(companyName.substring(0,1)).append(separator);
        return builder.toString();
    }
}
