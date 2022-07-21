package io.stock.evaluation.reactive_data.tdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-docker")
@SpringBootTest
public class RedisPutGetSaveTest {

    @Autowired
    @Qualifier("tickerMapReactiveRedisOperation")
    private ReactiveRedisOperations<String, String> tickerMapReactiveRedisOps;



    @Test
    public void test(){
        tickerMapReactiveRedisOps.opsForValue()
                .set("삼성전자", "005930")
                .subscribe();

        tickerMapReactiveRedisOps.opsForValue()
                .set("005930", "005930")
                .subscribe();

        tickerMapReactiveRedisOps.opsForValue()
                .set("00126380", "005930")
                .subscribe();

        tickerMapReactiveRedisOps.keys("*")
                .subscribe(System.out::println);
    }
}
