package io.stock.kr.calculator.stock.meta.crawling.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FSCStockPriceItem {
    private String baseDt;
    private String strnCd;
    private String isinCd;
    private String itmsNm;
    private String mrktCtg;
    private String vs;
    private Long mkp;   // open
    private Long hipr;  // high
    private Long lopr;  // low
    private Long clpr;  // close
}
