package io.stock.kr.calculator.request.fsc;

import lombok.Getter;

@Getter
public enum FSCAPIType {
    STOCK_PRICE_API(
        "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService",
        "/1160100/service/GetStockSecuritiesInfoService",
        "/getStockPriceInfo"
    );

    private final String baseUrl;
    private final String basePath;
    private final String endPoint;

    FSCAPIType(String baseUrl, String basePath, String endPoint){
        this.baseUrl = baseUrl;
        this.basePath = basePath;
        this.endPoint = endPoint;
    }

    public String webClientEndpoint(){
        return new StringBuilder().append(basePath).append(endPoint).toString();
    }
}
