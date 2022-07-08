package io.stock.evaluation.reactive_data.price.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class PriceRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PriceHandler priceHandler){
        return RouterFunctions
                .route().GET(
                        "/stock/price",
                        RequestPredicates.queryParam("ticker", v -> true),
                        priceHandler::getPriceBasicValuation
                ).build();
//                .andRoute() // ...
    }
}
