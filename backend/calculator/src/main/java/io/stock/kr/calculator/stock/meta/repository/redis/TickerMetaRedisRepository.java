package io.stock.kr.calculator.stock.meta.repository.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TickerMetaRedisRepository extends CrudRepository<TickerMetaRedis, String>{
    List<TickerMetaRedis> findAll();
}
