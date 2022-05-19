package io.stock.kr.calculator.stock.meta.crawling.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FSCStockPriceResponseHeader {
    private String resultCode;
    private String resultMsg;
}
