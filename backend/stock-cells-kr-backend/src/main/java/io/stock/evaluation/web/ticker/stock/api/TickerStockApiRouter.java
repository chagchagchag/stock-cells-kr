package io.stock.evaluation.web.ticker.stock.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TickerStockApiRouter {

    @Bean
    public RouterFunction<ServerResponse> tickerStockSearchRouter(TickerStockApiHandler tickerStockApiHandler){
        return RouterFunctions
                .route()
                .GET(
                        "/ticker/stock",
                        v -> true,
                        tickerStockApiHandler::searchTickersByCompanyName
                )
                .build();
    }
}
