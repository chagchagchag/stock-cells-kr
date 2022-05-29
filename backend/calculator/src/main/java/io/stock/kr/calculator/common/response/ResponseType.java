package io.stock.kr.calculator.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseType {
    DATE_NULL(4001, "날짜 타입이 비어있는 요청입니다.", HttpStatus.BAD_REQUEST),
    DATE_FORMAT_INVALID(4002, "날짜 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String description;
    private final HttpStatus httpStatusCode;

    ResponseType(int code, String description, HttpStatus httpStatusCode){
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }
}
