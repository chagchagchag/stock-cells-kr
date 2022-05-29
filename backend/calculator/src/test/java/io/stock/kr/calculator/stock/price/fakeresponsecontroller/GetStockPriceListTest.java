package io.stock.kr.calculator.stock.price.fakeresponsecontroller;

import io.stock.kr.calculator.common.response.CommonResponse;
import io.stock.kr.calculator.common.response.ResponseType;
import io.stock.kr.calculator.stock.price.FakeResponseController;
import io.stock.kr.calculator.stock.price.response.StockPriceItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class GetStockPriceListTest {

    @Test
    public void TEST_비어있는_기간데이터가_오면_NOT_OK_를_리턴해야한다(){
        FakeResponseController controller = new FakeResponseController();
        Mono<CommonResponse<Flux<StockPriceItem>>> stockPriceList = controller.getStockPriceList(null, null);

        stockPriceList.subscribe(commonResponse -> {
            assertThat(commonResponse.getStatus()).isEqualTo(ResponseType.DATE_NULL.getHttpStatusCode());
            assertThat(commonResponse.getDescription()).isEqualTo(ResponseType.DATE_NULL.getDescription());

            Flux<StockPriceItem> body = commonResponse.getBody();
            StepVerifier.create(body)
                        .expectNextCount(1);
        });
    }

    @Test
    public void TEST_모노_테스트(){
        Mono<String> m = Mono.just("Start");

        StepVerifier.create(m)
                .expectNext("Start")
                .verifyComplete();
    }

    @Test
    public void TEST_플럭스_테스트(){
        Flux<String> flux = Flux.just("Start", "From", "Basic");

        StepVerifier.create(flux)
                .expectNext("Start")
                .expectNext("From")
                .expectNext("Basic")
                .verifyComplete();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();

    }
}
