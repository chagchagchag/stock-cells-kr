package io.stock.evaluation.web.ticker.stock.api;

import io.stock.evaluation.web.ticker.stock.cache.TickerStockRedisService;
import io.stock.evaluation.web.ticker.stock.dto.TickerStockDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TickerStockApiHandler {

    private final TickerStockRedisService redisService;

    public TickerStockApiHandler(TickerStockRedisService redisService){
        this.redisService = redisService;
    }

    public Mono<ServerResponse> searchTickersByCompanyName(ServerRequest request){
        return request.queryParam("companyName")
                .map(companyName -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                redisService.searchCompanyNames(companyName, 0D, 5D, 0, 30),
                                TickerStockDto.class
                        )
                        .switchIfEmpty(notFound().build())
                )
                .orElse(notFound().build());
    }

}
