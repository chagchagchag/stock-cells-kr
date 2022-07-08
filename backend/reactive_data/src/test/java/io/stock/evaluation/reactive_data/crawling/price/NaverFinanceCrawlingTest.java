package io.stock.evaluation.reactive_data.crawling.price;

import io.stock.evaluation.reactive_data.crawling.types.NaverFinanceParameterType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class NaverFinanceCrawlingTest {

//    public Mono<Document>
    public Mono<Document> getDocument(String url){
        try{
            return Mono.just(Jsoup.connect(url).get());
        } catch (Exception e){
            e.printStackTrace();
        }
        return Mono.empty();
    }

    @Test
    public void URL_TEST(){
        String url = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl("005930");
        assertThat(url).isEqualTo("https://finance.naver.com/item/main.naver?code=005930");
    }

    @Test
    public void DOCUMENT_TEST(){
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl("005930");

        Mono<Elements> element = getDocument(targetUrl)
                .map(document -> {
                    String selector = "em[id='_per']";
                    return document.select(selector);
                })
                .log();

        StepVerifier.create(element)
                .expectNextMatches(el -> el.attr("id").equals("_per"))
                .verifyComplete();

//        StepVerifier.create()
    }

    @Test
    public void DOCUMENT_TEST_USING_FLATMAP(){
        String targetUrl = NaverFinanceParameterType.TICKER_SEARCH.stockSearchUrl("005930");

        Flux<Elements> eachSelectJobs = getDocument(targetUrl)
                .flatMapMany(document ->
                        Flux.just(
                                document.select("em[id='_per']"),
                                document.select("em[id='_eps']"),
                                document.select("em[id='_pbr']"),
                                document.select("em[id='_dvr']"),
                                document.select("em[id='_market_sum']")
                        )
                );

        eachSelectJobs.subscribe(elements -> {
            String id = elements.attr("id");
            StringBuilder builder = new StringBuilder().append("id = ").append(id).append(", ").append("text = ").append(elements.text());
            System.out.println(builder.toString());
        });

    }

}
