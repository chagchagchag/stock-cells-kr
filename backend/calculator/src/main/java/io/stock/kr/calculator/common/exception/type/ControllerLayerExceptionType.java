package io.stock.kr.calculator.common.exception.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ControllerLayerExceptionType implements BaseExceptionType{
    MISSING_SERVLET_REQUEST_PARAMETER(400001, "요청 파라미터가 비어있습니다.", HttpStatus.BAD_REQUEST);

    private final int resultCode;
    private final String description;
    private final HttpStatus httpStatus;

    ControllerLayerExceptionType(int resultCode, String description, HttpStatus httpStatus){
        this.resultCode = resultCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
