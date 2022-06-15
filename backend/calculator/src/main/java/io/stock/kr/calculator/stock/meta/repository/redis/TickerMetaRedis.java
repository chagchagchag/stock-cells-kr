package io.stock.kr.calculator.stock.meta.repository.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@RedisHash("ticker-")
@AllArgsConstructor
@Builder
public class TickerMetaRedis {
    @Id
    private String ticker;      // 종목코드
    private String companyName; // 종목명
}
