package io.stock.evaluation.reactive_data.tdd;


import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import io.stock.evaluation.reactive_data.ticker.meta.external.DartDataConverter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

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
                                .set("SEARCH-TICKER-"+tickerMetaItem.getTicker(), tickerMetaItem).block()
        );

        Long count = tickerMetaMapOps.keys("SEARCH-TICKER-*").count().block();
        assertThat(count).isNotZero();
    }


    // 검색어 추천을 위해 아래와 같은 종류의 키/밸류를 넣어준다.
    // - ticker(종목코드)  : TickerMetaItem
    // - 종목명            : TickerMetaItem
    @Test
    public void TEST_REDIS_PUT_TICKER_LIST2(){
        DartDataConverter converter = new DartDataConverter();
        Flux<TickerMetaItem> tickerMetaItemFlux = converter.processTickers();

        // 데이터가 모두 저장되는지 여부를 테스트해야 하기에 .block() 을 사용
        tickerMetaItemFlux.subscribe(
                tickerMetaItem ->{
                    tickerMetaMapOps
                            .opsForZSet()
                            .add("SEARCH-STOCK-"+tickerMetaItem.getTicker(), tickerMetaItem, 0).block();

                    tickerMetaMapOps
                            .opsForValue()
                            .set("SEARCH-STOCK-"+tickerMetaItem.getCompanyName(), tickerMetaItem, 0).block();
                }
        );

        Long count = tickerMetaMapOps.keys("SEARCH-STOCK-*").count().block();
        assertThat(count).isNotZero();
    }

    @Disabled
    @Test
    public void TEST_REDIS_AUTO_COMPLETE_SIMPLE(){
        DartDataConverter converter = new DartDataConverter();
        Flux<TickerMetaItem> tickerMetaItemFlux = converter.processTickers();

        tickerMetaItemFlux.subscribe(
                tickerMetaItem -> {
                    tickerMetaMapOps
                            .opsForHash()
                            .put("AUTO-COMPLETE", tickerMetaItem.getTicker(), tickerMetaItem).block();
                }
        );

//        Long count = tickerMetaMapOps.keys("AUTO-COMPLETE-*").count().block();
        long count = tickerMetaMapOps.opsForHash().entries("AUTO-COMPLETE").count().block();
        assertThat(count).isNotZero();
    }
}
