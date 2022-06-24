package io.stock.evaluation.reactive_data.tdd;


import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import io.stock.evaluation.reactive_data.ticker.meta.external.DartDataConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test-docker")
@SpringBootTest
public class DartDataToTickerMetaTest {

    @Autowired
    ReactiveRedisOperations<String, TickerMetaItem> tickerMetaMapOps;

    @Test
    public void TEST_DART_TICKER_PROCESSOR(){
        DartDataConverter converter = new DartDataConverter();
        Flux<TickerMetaItem> tickerFlux = converter.processTickers();

        tickerFlux.hasElements()
                .subscribe(check -> assertThat(check).isTrue());
    }
    
    
    @Test
    public void TEST_REDIS_PUT_TICKER_LIST(){
        DartDataConverter converter = new DartDataConverter();
        Flux<TickerMetaItem> tickerMetaItemFlux = converter.processTickers();

        // 데이터가 모두 저장되는지 여부를 테스트해야 하기에 .block() 을 사용
        tickerMetaItemFlux.subscribe(
                tickerMetaItem ->
                        tickerMetaMapOps
                                .opsForValue()
                                .set("TICKER-"+tickerMetaItem.getTicker(), tickerMetaItem).block()
        );

        Long count = tickerMetaMapOps.keys("TICKER-*").count().block();
        assertThat(count).isNotZero();
    }
}
