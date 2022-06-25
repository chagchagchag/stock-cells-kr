package io.stock.evaluation.reactive_data.tdd;

import io.stock.evaluation.reactive_data.ticker.meta.cache.TickerCachePrefixType;
import io.stock.evaluation.reactive_data.ticker.meta.cache.TickerMetaRedisService;
import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import io.stock.evaluation.reactive_data.ticker.meta.external.DartDataConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
        TickerMetaRedisService tickerMetaRedisService = new TickerMetaRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);
        tickerMetaRedisService.saveAllCompanyNamesToRedis();

//        Long count = tickerMetaMapOps.keys("SEARCH-TICKER-*").count().block();
        Long count = tickerMetaMapOps.keys(TickerCachePrefixType.SEARCH_TICKER.getCachePrefixTypeName() + "*").count().block();
        assertThat(count).isNotZero();
    }

    @Test
    public void TEST_REDIS_SEARCH_COMPANY_TEST(){
        TickerMetaRedisService tickerMetaRedisService = new TickerMetaRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);
        Mono<TickerMetaItem> data = tickerMetaRedisService.searchTickerMetaItem("삼성전자");

        StepVerifier.create(data)
                .expectNextMatches(tickerMetaItem -> tickerMetaItem.getCompanyName().equals("삼성전자"))
                .verifyComplete();
    }

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
