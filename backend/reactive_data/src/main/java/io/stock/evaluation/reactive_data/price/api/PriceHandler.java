package io.stock.evaluation.reactive_data.price.api;

import io.stock.evaluation.reactive_data.crawling.stock.price.application.CrawlingValuationService;
import io.stock.evaluation.reactive_data.crawling.stock.price.dto.CrawlingData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class PriceHandler {
    private final CrawlingValuationService crawlingValuationService;

    public PriceHandler(CrawlingValuationService crawlingValuationService){
        this.crawlingValuationService = crawlingValuationService;
    }

    public Mono<ServerResponse> getPriceBasicValuation (ServerRequest serverRequest){
        return serverRequest.queryParam("ticker")
                .map(ticker -> {
                    CrawlingData cdata = crawlingValuationService.getPriceBasicValuationData(ticker).block();
                    return ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(cdata))
                            .switchIfEmpty(notFound().build());
                })
                .orElse(notFound().build());
    }
}
