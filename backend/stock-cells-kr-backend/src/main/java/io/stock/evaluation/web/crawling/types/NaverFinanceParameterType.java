package io.stock.evaluation.web.crawling.types;

import lombok.Getter;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.function.Function;

@Getter
public enum NaverFinanceParameterType {
    TICKER_SEARCH(
            "NAVER_FINANCE",
            "http",
            "finance.naver.com",
            "/item/main.naver"){};

    private final String typeName;
    private final String scheme;
    private final String host;
    private final String basePath;

    NaverFinanceParameterType(String typeName, String scheme, String host, String basePath){
        this.typeName = typeName;
        this.scheme = scheme;
        this.host = host;
        this.basePath = basePath;
    }

    public String stockSearchUrl(String ticker){
        return new StringBuilder("https://")
                .append(getHost())
                .append(getBasePath())
                .append("?")
                .append("code").append("=").append(ticker)
                .toString();
    }

    // 응? 이거 왜만들었었지???
    public Function<UriBuilder, URI> stockSearchUrlBuilder(String ticker){
        return uriBuilder -> uriBuilder
                    .scheme(getScheme())
                    .host(getHost())
                    .path(getBasePath())
                    .queryParam("code", ticker)
                    .build();
    }

}
