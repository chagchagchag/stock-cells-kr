package io.stock.evaluation.reactive_data.tdd;


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
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
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

    @Getter
    class AutoCompleteTickerKeyBuilder {
        final String prefix = "AUTO-COMPLETE";
        final String separator = "###";
        final String finisher = "$$$";

        final String companyName;
        final StringBuilder builder = new StringBuilder();

        public AutoCompleteTickerKeyBuilder(String companyName){
            this.companyName = companyName;
        }

        public String generateKey(){
            builder.append(prefix).append(separator)
                    .append(companyName.substring(0,1)).append(separator)
                    .append(companyName.length());
            return builder.toString();
        }

        public String searchKey(){
            StringBuilder builder = new StringBuilder();
            builder.append(prefix).append(separator)
                    .append(companyName.substring(0,1)).append(separator);
            return builder.toString();
        }
    }

    @Test
    public void TEST_SAVE_TICKERS_TO_REDIS(){
        tickerMetaItems()
                .subscribe(tickerMetaItem -> {
                    final String companyName = tickerMetaItem.getCompanyName();
                    AutoCompleteTickerKeyBuilder builder = new AutoCompleteTickerKeyBuilder(companyName);
                    String key = builder.generateKey();

                    tickerMetaAutoCompleteOps.opsForZSet()
                            .add(key, tickerMetaItem.getCompanyName() + "§§§", 1).block();

                    for(int i=1; i<companyName.length(); i++){
                        tickerMetaAutoCompleteOps.opsForZSet()
                                .add(key, companyName.substring(0,i), 0).block();
                    }
                });
    }

    @Test
    public void TEST_AUTO_COMPLETE_TEST(){
//        String companyName = "삼";
        String companyName = "삼성전자";
        Double min = 0D;
        Double max = 5D;
        int count = 30;

        String keyword = companyName.trim();
        int len = keyword.length();

        AutoCompleteTickerKeyBuilder builder = new AutoCompleteTickerKeyBuilder(companyName);
        final String key = builder.searchKey();
        System.out.println(key);

        // 참고 : https://www.programcreek.com/java-api-examples/?api=org.springframework.data.domain.Range
        final Bound<Double> lowerBound = Bound.inclusive(min);
        final Bound<Double> upperBound = Bound.inclusive(max);
        Range<Double> range = Range.of(lowerBound, upperBound);
        Limit limit = new Limit();
        limit.offset(0);
        limit.count(count);

        Pattern pattern = Pattern.compile(".*\\§§§");

        List<String> results = new ArrayList<>();
        for(int i=len; i<count; i++){
            if(results.size() == count) break;
            Flux<ZSetOperations.TypedTuple<String>> typedTupleFlux = tickerMetaAutoCompleteOps
                    .opsForZSet()
                    .reverseRangeByScoreWithScores(key+i, range, limit);

            typedTupleFlux.subscribe(tuple -> {
                if(results.size() < count){
                    String value = tuple.getValue().trim();
                    int minLen = Math.min(value.length(), keyword.length());
                    if(pattern.matcher(value).matches() && value.startsWith(keyword.substring(0, minLen))){
                        System.out.println(value);
                        results.add(value.replace("§§§", ""));
                    }
                }
            });

        }
    }


}
