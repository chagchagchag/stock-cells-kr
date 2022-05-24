package io.stock.kr.calculator.stock.price.repository.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriceDayRedisRepository extends CrudRepository<PriceDayRedis, String> {
    List<PriceDayRedis> findAll();
}
