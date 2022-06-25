package io.stock.evaluation.reactive_data.tdd;


import io.stock.evaluation.reactive_data.ticker.meta.cache.AutoCompleteTickerKeyBuilder;
import io.stock.evaluation.reactive_data.ticker.meta.cache.TickerMetaRedisService;
import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import io.stock.evaluation.reactive_data.ticker.meta.external.DartDataConverter;
import lombok.Getter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Range.Bound;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test-docker")
@SpringBootTest
public class DartDataToTickerMetaTest {

    @Autowired
    ReactiveRedisOperations<String, TickerMetaItem> tickerMetaMapOps;

    @Autowired
    @Qualifier("tickerAutoCompleteRedisOperation")
    ReactiveRedisOperations<String, String> tickerMetaAutoCompleteOps;

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

        ScanOptions scanOption = ScanOptions.scanOptions().match("A*").build();
        tickerMetaMapOps.opsForHash().scan("AUTO-COMPLETE", scanOption).subscribe(entry -> System.out.println(entry.getValue()));
    }

    public Flux<TickerMetaItem> tickerMetaItems(){
        DartDataConverter converter = new DartDataConverter();
        return converter.processTickers();
    }

    final String DELIMITER = "###";

    @Test
    public void test_zset(){
        tickerMetaAutoCompleteOps.opsForZSet().add("TEST-1", "삼", 0).block();
        tickerMetaAutoCompleteOps.opsForZSet().add("TEST-1", "삼성", 0).block();
        tickerMetaAutoCompleteOps.opsForZSet().add("TEST-1", "삼성전", 0).block();
        tickerMetaAutoCompleteOps.opsForZSet().add("TEST-1", "삼성전자", 0).block();
    }

    @Test
    public void TEST_SAVE_TICKERS_TO_REDIS(){
        TickerMetaRedisService tickerMetaRedisService = new TickerMetaRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);
        tickerMetaRedisService.saveAllTickersToRedis();
    }

    @Test
    public void TEST_AUTO_COMPLETE_TEST(){
//        String companyName = "삼";
        String companyName = "삼성전자";
        Double min = 0D;
        Double max = 5D;
        int offset = 0;
        int count = 30;

        TickerMetaRedisService tickerMetaRedisService = new TickerMetaRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);
        Flux<String> searchFlux = tickerMetaRedisService.searchCompanyNames(companyName, min, max, offset, count);
        searchFlux.subscribe(str -> System.out.println(str));
    }

}
