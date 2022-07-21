package io.stock.evaluation.web.tdd;

import io.stock.evaluation.web.ticker.stock.cache.TickerCachePrefixType;
import io.stock.evaluation.web.ticker.stock.cache.TickerStockRedisService;
import io.stock.evaluation.web.ticker.stock.dto.TickerStockDto;
import io.stock.evaluation.web.ticker.stock.external.DartDataLoader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test-docker")
@SpringBootTest
public class DartDataToTickerStockTest {

    @Autowired
    ReactiveRedisOperations<String, TickerStockDto> tickerMetaMapOps;

    @Autowired
    @Qualifier("tickerAutoCompleteRedisOperation")
    ReactiveRedisOperations<String, String> tickerMetaAutoCompleteOps;

    @Test
    public void TEST_DART_TICKER_PROCESSOR(){
        DartDataLoader converter = new DartDataLoader();
        Flux<TickerStockDto> tickerFlux = converter.processTickers();

        tickerFlux.hasElements()
                .subscribe(check -> assertThat(check).isTrue());
    }

    @Disabled
    @Test
    public void TEST_REDIS_PUT_TICKER_LIST(){
        TickerStockRedisService tickerStockRedisService = new TickerStockRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);
        tickerStockRedisService.saveAllTickerStock(Flux.empty());

//        Long count = tickerMetaMapOps.keys("SEARCH-TICKER-*").count().block();
        Long count = tickerMetaMapOps.keys(TickerCachePrefixType.SEARCH_TICKER.getCachePrefixTypeName() + "*").count().block();
        assertThat(count).isNotZero();
    }

    @Test
    public void TEST_REDIS_SEARCH_COMPANY_TEST(){
        TickerStockRedisService tickerStockRedisService = new TickerStockRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);
        Mono<TickerStockDto> data = tickerStockRedisService.searchTickerMetaItem("삼성전자");

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

    public void saveTickerStocks(){
        TickerStockRedisService tickerStockRedisService = new TickerStockRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);

        Flux<TickerStockDto> tickerStockFlux = tickerStockRedisService.tickerMetaItemFlux();

        tickerStockRedisService
                .saveAllCompleteTickers(tickerStockFlux)
                .subscribe(keyPairMono -> keyPairMono.block());

        tickerStockRedisService
                .saveAllPartialWordTickers(tickerStockFlux)
                .subscribe(keyPairMono -> keyPairMono.block());

        tickerStockRedisService
                .saveAllTickerStock(tickerStockFlux)
                .subscribe(generator -> generator.block());
    }

    @Test
    public void TEST_SAVE_TICKERS(){
        saveTickerStocks();
    }

    @Test
    public void TEST_SAVE_TICKERS_AND_SEARCH(){
//        saveTickerStocks();

        TickerStockRedisService tickerStockRedisService = new TickerStockRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);

        String companyName = "삼성전";
        Double min = 0D;
        Double max = 5D;
        int offset = 0;
        int count = 30;

        tickerStockRedisService.searchCompanyNames(companyName, min, max, offset, count)
                .subscribe(str -> {
                    System.out.println(str);
                });
//        searchFlux.subscribe(str -> System.out.println(">>>>>" + str));
    }

    @Test
    public void TEST_AUTO_COMPLETE_TEST(){
//        String companyName = "삼";
//        String companyName = "삼성전자";
        String companyName = "삼성전";
        Double min = 0D;
        Double max = 5D;
        int offset = 0;
        int count = 30;
//
        TickerStockRedisService tickerStockRedisService = new TickerStockRedisService(tickerMetaAutoCompleteOps, tickerMetaMapOps);
        Flux<TickerStockDto> tickerStockFlux = tickerStockRedisService.searchCompanyNames(companyName, min, max, offset, count);

        // TODO 아래 내용 정리 필요
        // https://stackoverflow.com/questions/57221204/retrieve-all-flux-elements-in-stepverifier
        StepVerifier.create(tickerStockFlux)
//                .thenConsumeWhile(tickerStockDto -> true)
                .thenConsumeWhile(tickerStockDto -> {
                    if(Optional.ofNullable(tickerStockDto).isEmpty()) return false;
                    if(tickerStockDto.getTicker().isEmpty()) return false;
                    if(tickerStockDto.getCompanyName().isEmpty()) return false;
//                    if(!tickerStockDto.getCompanyName().startsWith(companyName)) return false;
                    System.out.println(">>> " + tickerStockDto.getCompanyName());
                    return true;
                })
                .verifyComplete();
    }

}
