package io.stock.evaluation.reactive_data.tdd;


import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import io.stock.evaluation.reactive_data.ticker.meta.external.DartDataConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test-docker")
@SpringBootTest
public class DartDataToTickerMetaTest {

    @Test
    public void TEST_DART_TICKER_PROCESSOR(){
        DartDataConverter converter = new DartDataConverter();
        Flux<TickerMetaItem> tickerFlux = converter.processTickers();

        tickerFlux.hasElements()
                .subscribe(check -> assertThat(check).isTrue());
    }
}
