package io.stock.evaluation.reactive_data.price.api.pricehandler;

import io.stock.evaluation.reactive_data.crawling.stock.price.application.CrawlingValuationService;
import io.stock.evaluation.reactive_data.price.api.PriceApiHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;

@ExtendWith(MockitoExtension.class)
public class GetPriceBasicValuationTest {

    CrawlingValuationService service = new CrawlingValuationService();
    PriceApiHandler handler = new PriceApiHandler(service);

    @Test
    @DisplayName("티커_파라미터가_정상적으로_전달될경우_200_OK_를_리턴해야_한다")
    public void 티커_파라미터가_정상적으로_전달될경우_200_OK_를_리턴해야_한다(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .queryParam("ticker", "005930")
                .build();

        Mono<ServerResponse> response = handler.getPriceBasicValuation(request);

        Predicate<ServerResponse> is2xxSuccessful = r -> r.statusCode().is2xxSuccessful();

        StepVerifier.create(response)
                .expectNextMatches(is2xxSuccessful)
                .verifyComplete();
    }

    @Test
    @DisplayName("티커_파라미터가_비정상적일_경우_4xx_를_리턴해야_한다")
    public void 티커_파라미터가_비정상적일_경우_4xx_를_리턴해야_한다(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .queryParam("--", "005930")
                .build();

        Mono<ServerResponse> response = handler.getPriceBasicValuation(request);

        Predicate<ServerResponse> is4xxClientError = r -> r.statusCode().is4xxClientError();

        StepVerifier.create(response)
                .expectNextMatches(is4xxClientError)
                .verifyComplete();
    }

    @Test
    @DisplayName("존재하지_않는_티커_파라미터를_전달할_경우_4xx_를_리턴해야_한다")
    public void 존재하지_않는_티커_파라미터를_전달할_경우_4xx_를_리턴해야_한다(){
        ServerRequest request = MockServerRequest.builder()
                .method(HttpMethod.GET)
                .queryParam("--", "005930")
                .build();

        Mono<ServerResponse> response = handler.getPriceBasicValuation(request);

        Predicate<ServerResponse> is4xxClientError = r -> r.statusCode().is4xxClientError();

        StepVerifier.create(response)
                .expectNextMatches(is4xxClientError)
                .verifyComplete();
    }
}
