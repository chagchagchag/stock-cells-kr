package io.stock.kr.calculator.config.connections;

import io.stock.kr.calculator.stock.price.repository.redis.StockPriceRedis;
import io.stock.kr.calculator.stock.price.repository.redis.StockPriceRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test-live")
public class LiveRedisConnectTest {
    @Autowired
    private StockPriceRedisRepository repository;

    @Test
    public void test(){
        StockPriceRedis data = StockPriceRedis.builder()
                .ticker("MSFT")
                .open("11111")
                .high("22222")
                .low("111")
                .close("22222")
                .build();

        repository.save(data);

        repository.findById(data.getTicker());

        repository.count();

    }

}
