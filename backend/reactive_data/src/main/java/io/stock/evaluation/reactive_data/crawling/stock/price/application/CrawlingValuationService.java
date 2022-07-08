package io.stock.evaluation.reactive_data.crawling.stock.price.application;

import io.stock.evaluation.reactive_data.crawling.stock.price.dto.CrawlingData;
import io.stock.evaluation.reactive_data.crawling.stock.price.type.CrawlingDataType;
import io.stock.evaluation.reactive_data.crawling.types.NaverFinanceParameterType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

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

    public Mono<CrawlingData> getPriceBasicValuationData(String ticker){
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl(ticker);
        final CrawlingData.CrawlingDataBuilder dataBuilder = new CrawlingData.CrawlingDataBuilder();

        return getDocument(targetUrl)
                .flatMapMany(document ->
                        Flux.just(
                                document.select("em[id='_per']"),
                                document.select("em[id='_eps']"),
                                document.select("em[id='_pbr']"),
                                document.select("em[id='_dvr']"),
                                document.select("em[id='_market_sum']")
                        )
                )
                .map(elements -> {
                    String type = elements.attr("id").substring(1);
                    String value = elements.text();
                    dataBuilder.bindParameter(type, value);
                    return dataBuilder;
                })
                .last()
                .map(builder -> builder.build());
    }
}
