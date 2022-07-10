package io.stock.evaluation.reactive_data.ticker.meta.cache;

import io.stock.evaluation.reactive_data.ticker.meta.dto.KeyPair;
import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerStockDto;
import io.stock.evaluation.reactive_data.ticker.meta.external.DartDataLoader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Range.Bound;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TickerStockRedisService {
    private final ReactiveRedisOperations<String, String> tickerMetaAutoCompleteRedisOps;
    private final ReactiveRedisOperations<String, TickerStockDto> tickerMetaMapOps;
    private final Pattern pattern = Pattern.compile(".*\\§§§");

    public TickerStockRedisService(
        @Qualifier("tickerAutoCompleteRedisOperation")
        ReactiveRedisOperations<String, String> tickerMetaAutoCompleteRedisOps,
        ReactiveRedisOperations<String, TickerStockDto> tickerMetaMapOps
    ){
        this.tickerMetaAutoCompleteRedisOps = tickerMetaAutoCompleteRedisOps;
        this.tickerMetaMapOps = tickerMetaMapOps;
    }

    public Flux<TickerStockDto> tickerMetaItemFlux(){
        DartDataLoader converter = new DartDataLoader();
        return converter.processTickers();
    }

    Function<TickerStockDto, KeyPair> keyPairFunc = stockDto ->{
        final String companyName = stockDto.getCompanyName();
        final TickerAutoCompleteSearchKeyGenerator generator = new TickerAutoCompleteSearchKeyGenerator(companyName);
        final String key = generator.generateKey();
        return new KeyPair(key, companyName + "§§§");
    };

    Function<TickerStockDto, List<KeyPair>> keyPairListFunc = stockDto -> {
        final String companyName = stockDto.getCompanyName();
        final TickerAutoCompleteSearchKeyGenerator generator = new TickerAutoCompleteSearchKeyGenerator(companyName);
        final String key = generator.generateKey();

        return IntStream.range(1, companyName.length())
                .boxed()
                .map(i -> {
                    return new KeyPair(key, companyName.substring(0, i));
                })
                .collect(Collectors.toList());
    };

    public Flux<Mono<KeyPair>> saveAllCompleteTickers(Flux<TickerStockDto> tickerFlux){
        return tickerFlux
                .map(keyPairFunc)
                .map(keyPair -> {
                    return tickerMetaAutoCompleteRedisOps
                            .opsForZSet()
                            .add(keyPair.key(), keyPair.value(), 1)
                            .map(b -> keyPair);
                });
    }

    public Flux<Mono<KeyPair>> saveAllPartialWordTickers(Flux<TickerStockDto> tickerFlux){
        return tickerFlux
                .map(keyPairListFunc)
                .flatMap(keyPairs -> Flux.fromIterable(keyPairs))
                .map(keyPair -> tickerMetaAutoCompleteRedisOps.opsForZSet().add(keyPair.key(), keyPair.value(), 0).map(b -> keyPair));
    }

    /**
     * 1) <companyName : tickerStockDto>
     * 2) <ticker : tickerStockDto>
     */
    public Flux<Mono<TickerSearchKeyGenerator>> saveAllTickerStock(Flux<TickerStockDto> tickerStockFlux){
        return tickerStockFlux
                .flatMap(tickerStockDto ->
                        Flux.just(
                                TickerSearchKeyGenerator.newGeneratorForGenerate(SearchTickerType.BY_COMPANY_NAME, tickerStockDto),
                                TickerSearchKeyGenerator.newGeneratorForGenerate(SearchTickerType.BY_TICKER, tickerStockDto)
                        )
                )
                .map(searchKeyGenerator -> tickerMetaMapOps.opsForValue().set(searchKeyGenerator.generateKey(), searchKeyGenerator.getTickerStockDto()).map(b -> searchKeyGenerator));
    }

    public Flux<TickerStockDto> searchCompanyNames(String companyName, Double min, Double max, int offset, int count){
        final String keyword = companyName.trim();
        int len = keyword.length();

        TickerAutoCompleteSearchKeyGenerator generator = new TickerAutoCompleteSearchKeyGenerator(companyName);
        final String key = generator.searchKey();

        // 참고 : https://www.programcreek.com/java-api-examples/?api=org.springframework.data.domain.Range
        final Bound<Double> lowerBound = Bound.inclusive(min);
        final Bound<Double> upperBound = Bound.inclusive(max);

        Range<Double> range = Range.of(lowerBound, upperBound);
        RedisZSetCommands.Limit limit = new RedisZSetCommands.Limit();

        limit.offset(offset);
        limit.count(count);

        return Flux.range(len, count)
                .flatMap(i -> {
                    return tickerMetaAutoCompleteRedisOps
                            .opsForZSet()
                            .reverseRangeByScoreWithScores(key + i, range, limit)
                            .filter(stringTypedTuple -> {
                                final String value = stringTypedTuple.getValue().trim();
                                int minLen = Math.min(value.length(), keyword.length());
                                if (pattern.matcher(value).matches() && value.startsWith(keyword.substring(0, minLen))) {
                                    return true;
                                }
                                return false;
                            })
                            .map(stringTypedTuple -> stringTypedTuple.getValue().replace("§§§", ""));
                })
                .flatMap(this::searchTickerMetaItem);
    }

    /**
     * 회사명으로 검색해도 TickerMetaItem, ticker(회사코드)로 검색해도 TickerMetaItem 이 리턴된다.
     * @param query {companyName | ticker}
     * @return TickerMetaItem
     */
    public Mono<TickerStockDto> searchTickerMetaItem(String query){
//        SearchTickerKeyBuilder searchTickerKeyBuilder = new SearchTickerKeyBuilder(SearchTickerType.BY_COMPANY_NAME);
        TickerSearchKeyGenerator tickerSearchKeyGenerator = TickerSearchKeyGenerator.newGeneratorForSearch(SearchTickerType.BY_COMPANY_NAME);
        String searchKey = tickerSearchKeyGenerator.searchKey(query);
        return tickerMetaMapOps.opsForValue().get(searchKey);
    }

}
