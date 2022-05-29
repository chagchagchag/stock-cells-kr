package io.stock.kr.calculator.stock.price.response;

import lombok.*;
import reactor.core.publisher.Flux;

@Data
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StockPriceItems {
    Flux<StockPriceItem> priceList;
}
