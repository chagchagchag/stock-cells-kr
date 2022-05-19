package io.stock.kr.calculator.stock.meta.crawling.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 한국금융결제원 주식 종가 데이터 API Response
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FSCStockPriceResponseWrapper {
    FSCStockPriceResponseHeader header;
    FSCStockPriceResponseBody body;
}
