package io.stock.evaluation.reactive_data.ticker.stock.api;

import io.stock.evaluation.reactive_data.ticker.stock.application.TickerStockService;
import io.stock.evaluation.reactive_data.ticker.stock.dto.TickerStockDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TickerStockApiHandler {

    private final TickerStockService tickerStockService;

    public TickerStockApiHandler(TickerStockService tickerStockService){
        this.tickerStockService = tickerStockService;
    }

    // 페이징 기반으로 전환하는게 맞지만, 처음 써보는 중이라.....
    public Mono<ServerResponse> findAllTicker(ServerRequest request){
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(emptyListResponse()))
            .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> findTicker(ServerRequest request){
        return request.queryParam("ticker")
                .map(ticker -> {
                    return ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(
                                    BodyInserters.fromValue(emptyResponseById(ticker))
                            )
                            .switchIfEmpty(notFound().build());
                })
                .orElse(notFound().build());
    }

    public TickerStockDto emptyResponseById(String ticker){
        return sampleTickerItem(ticker);
    }

    public TickerStockDto sampleTickerItem(String ticker){
        return TickerStockDto.builder()
                .ticker(ticker)
                .tickerCode("KR"+ticker)
                .companyName("삼성전자")
                .build();
    }

    public List<TickerStockDto> emptyListResponse(){
        return List.of(emptyResponse());
    }

    public TickerStockDto emptyResponse(){
        return TickerStockDto.builder()
                .ticker("ABC")
                .tickerCode("KR-ABC")
                .companyName("삼성전자")
                .build();
    }
}
