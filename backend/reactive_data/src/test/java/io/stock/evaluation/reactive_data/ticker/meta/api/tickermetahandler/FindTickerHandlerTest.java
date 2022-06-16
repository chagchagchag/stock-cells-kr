package io.stock.evaluation.reactive_data.ticker.meta.api.tickermetahandler;

import io.stock.evaluation.reactive_data.ticker.meta.api.TickerMetaHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.function.Predicate;

public class FindTickerHandlerTest {

    TickerMetaHandler tickerMetaHandler;

    @BeforeEach
    public void initLocal(){
        tickerMetaHandler = new TickerMetaHandler();
    }

    @Test
    public void TICKER가_queryParam으로_정상적으로_전달되었을경우(){
        ServerRequest mock = Mockito.mock(ServerRequest.class);

        Mockito.when(mock.queryParam("ticker"))
                .thenReturn(Optional.of("A005930"));

        Mono<ServerResponse> response = tickerMetaHandler.findTicker(mock);

        Predicate<ServerResponse> is2xxSuccessful = r -> r.statusCode().is2xxSuccessful();

        StepVerifier.create(response)
                .expectNextMatches(is2xxSuccessful)
                .verifyComplete();
    }

    @Test
    public void TICKER가_queryParam으로_전달되지_않았을_경우(){
        ServerRequest mock = Mockito.mock(ServerRequest.class);

        Mockito.when(mock.queryParam("t"))
                .thenReturn(Optional.of("A005930"));

        Mono<ServerResponse> response = tickerMetaHandler.findTicker(mock);

        Predicate<ServerResponse> is4xxClientError = r -> r.statusCode().is4xxClientError();

        StepVerifier.create(response)
                .expectNextMatches(is4xxClientError)
                .verifyComplete();
    }
}
