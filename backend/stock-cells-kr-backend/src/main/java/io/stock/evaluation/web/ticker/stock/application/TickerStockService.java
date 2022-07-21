package io.stock.evaluation.web.ticker.stock.application;

import io.stock.evaluation.web.ticker.stock.dto.TickerStockDto;
import io.stock.evaluation.web.ticker.stock.external.DartDataLoader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TickerStockService {

    private final ReactiveRedisOperations<String, TickerStockDto> tickerMetaMap;
    private final DartDataLoader dartDataLoader;

    public TickerStockService(
        @Qualifier("tickerMetaMapReactiveRedisOperation") ReactiveRedisOperations<String, TickerStockDto> tickerMetaMap,
        DartDataLoader dartDataconverter
    ){
        this.tickerMetaMap = tickerMetaMap;
        this.dartDataLoader = dartDataconverter;
    }

    // 종목코드(=Ticker), 종목명, DART CODE 셋중 하나로만 입력해도 종목코드(=Ticker)를 찾아서 리턴한다.
    public Flux<TickerStockDto> findTickerByAny(String key){
        // TODO::구현 예정
//        return tickerMetaMap
//                .opsForValue().get(key)
//                .flatMapMany(this::findTickerMetaItemByTicker);
        return Flux.empty();
    }

    // ticker 기반으로 TickerMetaItem 을 조회한다.
    public Flux<TickerStockDto> findTickerMetaItemByTicker(String ticker){
        // TODO::구현 예정
        // Router, Handler 구현이 완료 된후, 세부 모델을 확정지을 예정.
        return Flux.empty();
    }

    public void loadTickers(){
        Flux<TickerStockDto> tickerMetaItemFlux = dartDataLoader.processTickers();
        tickerMetaItemFlux
                .subscribe(tickerMetaItem -> tickerMetaMap.opsForValue().set("TICKER-"+tickerMetaItem.getTicker(), tickerMetaItem));
    }
}
