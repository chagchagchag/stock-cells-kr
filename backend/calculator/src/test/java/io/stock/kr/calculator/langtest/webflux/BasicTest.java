package io.stock.kr.calculator.langtest.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BasicTest {

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
