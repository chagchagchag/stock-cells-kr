package io.stock.kr.calculator.common.exception.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DateTimeExceptionType implements BaseExceptionType {
    DATETIME_NULL(4001, "날짜 데이터가 비어있습니다.", HttpStatus.BAD_REQUEST),
    DATETIME_FORMAT_INVALID(4002, "날짜데이터 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST);

    private final int resultCode;
    private final String description;
    private final HttpStatus httpStatus;

    DateTimeExceptionType(
        int resultCode, String description, HttpStatus httpStatus
    ){
        this.resultCode = resultCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
