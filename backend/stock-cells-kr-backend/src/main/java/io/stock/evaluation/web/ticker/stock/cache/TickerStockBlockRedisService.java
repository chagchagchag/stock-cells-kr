package io.stock.evaluation.web.ticker.stock.cache;

import io.stock.evaluation.web.ticker.stock.dto.KeyPair;
import io.stock.evaluation.web.ticker.stock.dto.TickerStockDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class TickerStockBlockRedisService {

    private final RedisTemplate<String, String> tickerMetaAutoCompleteBlockTemplate;
    private final RedisTemplate<String, TickerStockDto> tickerMetaMapBlockTemplate;

    public TickerStockBlockRedisService(
        RedisTemplate<String, String> tickerMetaAutoCompleteBlockTemplate,
        RedisTemplate<String, TickerStockDto> tickerMetaMapBlockTemplate
    ){
        this.tickerMetaAutoCompleteBlockTemplate = tickerMetaAutoCompleteBlockTemplate;
        this.tickerMetaMapBlockTemplate = tickerMetaMapBlockTemplate;
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

    public List<KeyPair> saveAllCompleteTickersBlock (List<TickerStockDto> tickerList){
        Objects.requireNonNull(tickerList);

        return tickerList.stream()
                .map(keyPairFunc)
                .map(keyPair -> {
                    Boolean result = tickerMetaAutoCompleteBlockTemplate
                            .opsForZSet()
                            .add(keyPair.key(), keyPair.value(), 1);
                    if(Boolean.TRUE.equals(result)) return keyPair;
                    else return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<KeyPair> saveAllPartialWordTickersBlock (List<TickerStockDto> tickerList){
        Objects.requireNonNull(tickerList);

        return tickerList.stream()
                .flatMap(tickerStockDto -> keyPairListFunc.apply(tickerStockDto).stream())
                .map(keyPair -> {
                    Boolean result = tickerMetaAutoCompleteBlockTemplate.opsForZSet().add(keyPair.key(), keyPair.value(), 0);
                    if(result) return keyPair;
                    else return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 1) <companyName : tickerStockDto>
     * 2) <ticker : tickerStockDto>
     */
    public List<KeyPair> saveAllTickerStockBlock (List<TickerStockDto> tickerStockList){
        return tickerStockList
                .stream()
                .flatMap(tickerStockDto ->
                    Stream.of(
                            TickerSearchKeyGenerator.newGeneratorForGenerate(SearchTickerType.BY_COMPANY_NAME, tickerStockDto),
                            TickerSearchKeyGenerator.newGeneratorForGenerate(SearchTickerType.BY_TICKER, tickerStockDto)
                    )
                )
                .map(searchKeyGenerator -> {
                    tickerMetaMapBlockTemplate.opsForValue()
                            .set(searchKeyGenerator.generateKey(), searchKeyGenerator.getTickerStockDto());
                    return keyPairFunc.apply(searchKeyGenerator.getTickerStockDto());
                })
                .collect(Collectors.toList());
    }
}
