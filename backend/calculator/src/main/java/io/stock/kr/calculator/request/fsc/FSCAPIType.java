package io.stock.kr.calculator.request.fsc;

import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
public enum FSCAPIType {
    STOCK_PRICE_API(
        "http",
        "apis.data.go.kr",
        "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService",
        "/1160100/service/GetStockSecuritiesInfoService",
        "/getStockPriceInfo",
        "---"
    );

    private final String scheme;
    private final String host;
    private final String baseUrl;
    private final String basePath;
    private final String endPoint;
    private final String serviceKey;

    FSCAPIType(String scheme, String host, String baseUrl, String basePath, String endPoint, String serviceKey){
        this.scheme = scheme;
        this.host = host;
        this.baseUrl = baseUrl;
        this.basePath = basePath;
        this.endPoint = endPoint;
        this.serviceKey = serviceKey;
    }

    public String webClientEndpoint(){
        return new StringBuilder().append(basePath).append(endPoint).toString();
    }

    public String getEncodedServiceKey(){
        String encodedKey = null;
        try {
            encodedKey = URLEncoder.encode(this.serviceKey, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedKey;
    }
}
