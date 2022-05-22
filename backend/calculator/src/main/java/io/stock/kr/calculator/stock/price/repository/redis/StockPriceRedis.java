package io.stock.kr.calculator.stock.price.repository.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@RedisHash("stock")
@AllArgsConstructor
@Builder
public class StockPriceRedis {
    private String tradeDt;

    @Id
    private String ticker;
    private String isinCd;
    private String itmsNm;
    private String mrktCtg;
    private String vs;

    private String open;
    private String high;
    private String low;
    private String close;

    public StockPriceRedis(){}
}
