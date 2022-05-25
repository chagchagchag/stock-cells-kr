package io.stock.kr.calculator.request.fsc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Builder
@AllArgsConstructor
public class FSCRequestParameters {
    private WebClient webClient;
    private String encodedKey;
    private String startDate;
    private String endDate;
    public FSCRequestParameters(){}
}
