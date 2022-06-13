package io.stock.kr.calculator.stock.price.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.stock.kr.calculator.common.number.PriceSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@ToString
public class StockPriceResponse {
    private final String ticker;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    @JsonProperty("open")
    @JsonSerialize(using = PriceSerializer.class)
    private final BigDecimal open;

    @JsonProperty("high")
    @JsonSerialize(using = PriceSerializer.class)
    private final BigDecimal high;

    @JsonProperty("low")
    @JsonSerialize(using = PriceSerializer.class)
    private final BigDecimal low;

    @JsonProperty("close")
    @JsonSerialize(using = PriceSerializer.class)
    private final BigDecimal close;

    @Builder
    public StockPriceResponse(
        String ticker, LocalDate date,
        BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close
    ){
        this.ticker = ticker;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }
}
