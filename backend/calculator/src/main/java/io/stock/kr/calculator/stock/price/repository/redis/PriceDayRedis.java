package io.stock.kr.calculator.stock.price.repository.redis;

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
