package io.stock.evaluation.web.price.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class PriceApiRouter {

    @Bean
    public RouterFunction<ServerResponse> stockPriceByTickerRouter(PriceApiHandler priceApiHandler){
        return RouterFunctions
                .route().GET(
                        "/stock/price",
                        RequestPredicates.queryParam("ticker", v -> true),
                        priceApiHandler::getPriceBasicValuation
                ).build();
//                .andRoute() // ...
    }
}
