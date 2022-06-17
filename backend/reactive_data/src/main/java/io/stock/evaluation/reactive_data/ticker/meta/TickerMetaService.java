package io.stock.evaluation.reactive_data.ticker.meta;

import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TickerMetaService {

    private final ReactiveRedisOperations<String, String> redisOperations;

    public TickerMetaService(
        ReactiveRedisOperations<String, String> redisOperations
    ){
        this.redisOperations = redisOperations;
    }

    // 종목코드(=Ticker), 종목명, DART CODE 셋중 하나로만 입력해도 종목코드(=Ticker)를 찾아서 리턴한다.
    public Flux<TickerMetaItem> findTickerByAny(String key){
        // TODO::구현 예정
        return redisOperations
                .opsForValue().get(key)
                .flatMapMany(this::findTickerMetaItemByTicker);
    }

    // ticker 기반으로 TickerMetaItem 을 조회한다.
    public Flux<TickerMetaItem> findTickerMetaItemByTicker(String ticker){
        // TODO::구현 예정
        // Router, Handler 구현이 완료 된후, 세부 모델을 확정지을 예정.
        return Flux.empty();
    }
}
