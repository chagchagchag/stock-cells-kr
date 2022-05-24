# Redis Repository 연동

무슨 일이든지 일단 시작부터 하는게 반이라는 이야기를 체감하는 요즘이다. 정리해둘 것들이 너무나 많다.<br>

<br>

# 참고자료 

- [Spring Data Redis](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis.repositories)

- [[Java + Redis\] Spring Data Redis로 Redis와 연동하기 - RedisRepository 편 (tistory.com)](https://sabarada.tistory.com/106)

<br>

# 의존성 추가

`build.gradle` 

```groovy
dependencies {
	// ...
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
    // ...
}
```

<br>

# 설정코드

간단하다. 별 내용은 없다.<br>

`RedisTemplate` 에 필요한 `ConnectionFactory` 빈을 생성하고, Serializer 를 지정해준다.<br>

라이브러리는 spring data redis 에서 기본으로 채택하기 시작한 lettuce 라이브러리를 사용한다.<br>

<br>

```java
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Profile("test-docker")
@Configuration
public class TestRedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(){
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
    
}
```

<br>

# 모델 추가

PriceDayRedis.java<br>

`@RedisHash` 에 입력하는 value 로 `price-` 를 지정해줬다. 이렇게 하면, Redis에 Persist되는 키의 실제 값은 실제로 아래와 같게 된다.

- `price-` + ticker 

<br>

```java
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@RedisHash("price-")
@NoArgsConstructor
@Builder
public class PriceDayRedis {
    @Id
    private String ticker;

    private LocalDate tradeDt;

    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;

    @Builder
    public PriceDayRedis(
        String ticker, LocalDate tradeDt,
        BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close
    ){
        this.ticker = ticker;
        this.tradeDt = tradeDt;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }
}

```

<br>

# Repository

리포지터리는 별 내용이 없다. 설명은 패스

```java
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriceDayRedisRepository extends CrudRepository<PriceDayRedis, String> {
    List<PriceDayRedis> findAll();
}
```

<br>

# 테스트

```java
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

```

이렇게 하고 나면 실제 출력결과는 아래와 같이 출력된다.

```plain
[PriceDayRedis(ticker=VOO, tradeDt=null, open=111, high=222, low=111, close=222)]
```









