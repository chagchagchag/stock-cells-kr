package io.stock.kr.calculator.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseType {
    OK(2000, "정상적인 호출", HttpStatus.OK);

    private final int resultCode;
    private final String description;
    private final HttpStatus httpStatusCode;

    ResponseType(int resultCode, String description, HttpStatus httpStatusCode){
        this.resultCode = resultCode;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }
}
