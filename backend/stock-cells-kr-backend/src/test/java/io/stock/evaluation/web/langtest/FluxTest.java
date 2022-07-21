package io.stock.evaluation.web.langtest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class FluxTest {

    @Test
    public void FLUX_BLOCK_TEST1(){
        Flux<String> just = Flux.just("1", "2", "3", "4", "5");
        just.subscribe(str -> System.out.println(str)).dispose();
    }

}
