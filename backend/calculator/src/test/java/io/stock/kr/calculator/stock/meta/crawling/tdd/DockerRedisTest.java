package io.stock.kr.calculator.stock.meta.crawling.tdd;

import io.stock.kr.calculator.stock.price.repository.redis.PriceDayRedis;
import io.stock.kr.calculator.stock.price.repository.redis.PriceDayRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

@ActiveProfiles("test-docker")
@SpringBootTest
public class DockerRedisTest {

    @Autowired
    PriceDayRedisRepository repository;

    @Test
    public void TEST(){
        PriceDayRedis priceDay = PriceDayRedis.builder()
                .ticker("VOO")
                .open(new BigDecimal("111"))
                .high(new BigDecimal("222"))
                .low(new BigDecimal("111"))
                .close(new BigDecimal("222"))
                .build();

        repository.save(priceDay);

        List<PriceDayRedis> result = repository.findAll();
        System.out.println(result);
    }
}
