package io.stock.kr.calculator.common.exception.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DateExceptionType implements BaseExceptionType {
    DATE_NULL(4001, "날짜 타입이 비어있는 요청입니다.", HttpStatus.BAD_REQUEST),
    DATE_FORMAT_INVALID(4002, "날짜 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST);

    private final int resultCode;
    private final String description;
    private final HttpStatus httpStatus;

    DateExceptionType(
        int resultCode, String description, HttpStatus httpStatus
    ){
        this.resultCode = resultCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatusCode(){
        return this.httpStatus.value();
    }
}
