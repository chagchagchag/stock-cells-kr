package io.stock.kr.calculator.stock.price.crawling.response;

public enum FscResultType {
    RESULT_EXIST(Boolean.TRUE, "ITEM_EXIST"),
    EMPTY(Boolean.FALSE, "DATA IS NOT EXIST"),
    API_ERROR(Boolean.FALSE, "API PARAMETER ERROR");

    private final Boolean result;
    private final String message;

    FscResultType(Boolean result, String message){
        this.result = result;
        this.message = message;
    }
}
