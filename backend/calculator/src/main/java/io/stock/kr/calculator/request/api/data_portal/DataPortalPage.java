package io.stock.kr.calculator.request.api.data_portal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataPortalPage {
    private long startIndex;
    private long endIndex;
}
