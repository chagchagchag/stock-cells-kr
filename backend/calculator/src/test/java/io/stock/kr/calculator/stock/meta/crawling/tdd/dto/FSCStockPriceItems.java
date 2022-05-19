package io.stock.kr.calculator.stock.meta.crawling.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FSCStockPriceItems {
    private List<FSCStockPriceItem> item;
}
