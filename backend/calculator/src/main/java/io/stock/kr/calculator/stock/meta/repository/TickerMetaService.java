package io.stock.kr.calculator.stock.meta.repository;

import io.stock.kr.calculator.stock.meta.crawling.StockMetaCrawlingDartService;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import io.stock.kr.calculator.stock.meta.repository.redis.TickerMetaRedis;
import io.stock.kr.calculator.stock.meta.repository.redis.TickerMetaRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TickerMetaService {
    private final StockMetaCrawlingDartService dartService;
    private final TickerMetaRedisRepository tickerMetaRedisRepository;

    public void updateTickerListUsingDartData(){
        List<StockMetaDto> tickerList = dartService.selectKrStockList("CORPCODE.xml");

        List<TickerMetaRedis> tickerMetaRedisList = tickerList.stream()
                .map(stockMetaDto -> {
                    return TickerMetaRedis.builder()
                            .ticker(stockMetaDto.getTicker())
                            .companyName(stockMetaDto.getCompanyName())
                            .build();
                })
                .collect(Collectors.toList());

        tickerMetaRedisRepository.saveAll(tickerMetaRedisList);
    }
}
