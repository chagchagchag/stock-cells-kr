package io.stock.evaluation.reactive_data.crawling.stock.price.application;

import io.stock.evaluation.reactive_data.crawling.stock.price.dto.CrawlingData;
import io.stock.evaluation.reactive_data.crawling.stock.price.dto.Parameter;
import io.stock.evaluation.reactive_data.crawling.types.NaverFinanceParameterType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class CrawlingValuationService {

    public Mono<Document> getDocument(String url){
        try{
            return Mono.just(Jsoup.connect(url).get());
        } catch (Exception e){
            e.printStackTrace();
        }
        return Mono.empty();
    }

    Function<Elements, Parameter> idBasedParser = (elements) -> {
        String type = elements.attr("id").substring(1);
        String value = elements.text();
        return new Parameter(type, value);
    };

    Function<Elements, Parameter> priceParser = (elements) -> {
        String type = "price";
        String value = elements.eachText().get(0);
        return new Parameter(type, value);
    };

    public Mono<CrawlingData> getPriceBasicValuationData(String ticker){
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl(ticker);
        final CrawlingData.CrawlingDataBuilder dataBuilder = new CrawlingData.CrawlingDataBuilder();

        return getDocument(targetUrl)
                .flatMapMany(document ->
                        Flux.just(
                                idBasedParser.apply(document.select("em[id='_per']")),
                                idBasedParser.apply(document.select("em[id='_eps']")),
                                idBasedParser.apply(document.select("em[id='_pbr']")),
                                idBasedParser.apply(document.select("em[id='_dvr']")),
                                idBasedParser.apply(document.select("em[id='_market_sum']")),
                                priceParser.apply(document.select("p.no_today span:not(.shim)"))
                        )
                )
                .map(parameter -> {
                    dataBuilder.bindParameter(parameter.getType(), parameter.getValue());
                    return dataBuilder;
                })
                .last()
                .map(builder -> builder.build());
    }
}
