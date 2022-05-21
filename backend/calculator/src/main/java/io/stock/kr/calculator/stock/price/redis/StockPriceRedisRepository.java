package io.stock.kr.calculator.stock.price.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockPriceRedisRepository extends CrudRepository<StockPriceRedis, String> {
    List<StockPriceRedis> findAll();
    List<StockPriceRedis> findAllByTicker(String ticker);
    int countAllByTicker(String ticker);
}
