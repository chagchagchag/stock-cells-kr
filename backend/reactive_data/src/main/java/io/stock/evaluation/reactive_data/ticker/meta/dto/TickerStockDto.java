package io.stock.evaluation.reactive_data.ticker.meta.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Ticker 는 주식에 대한 Ticker 가 있고, 지수에 대한 Ticker 역시 존재한다.
 * 추후 지수 등에 대한 데이터를 크롤링하는 경우도 생각해볼 수 있기에 TickerStockDto 로 캐시 데이터에 대한 이름으로 지정
 */
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TickerStockDto implements Serializable {
    private String ticker;
    private String companyName;
    private String tickerCode; // vendor specific code

    @Builder
    public TickerStockDto(String ticker, String companyName, String tickerCode){
        this.ticker = ticker;
        this.companyName = companyName;
        this.tickerCode = tickerCode;
    }
}
