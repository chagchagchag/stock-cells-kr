package io.stock.kr.calculator.common.exception;

import io.stock.kr.calculator.common.exception.type.BaseExceptionType;
import io.stock.kr.calculator.common.response.ResponseType;
import lombok.Builder;
import lombok.Data;

@Data
public class ErrorResponse {
    private int resultCode;
    private String description;

    public ErrorResponse(){}

    @Builder
    public ErrorResponse(int resultCode, String description){
        this.resultCode = resultCode;
        this.description = description;
    }

    public static ErrorResponse of (BaseExceptionType exceptionType){
        return ErrorResponse.builder()
                .resultCode(exceptionType.getResultCode())
                .description(exceptionType.getDescription())
                .build();
    }

    public static ErrorResponse of (ResponseType responseType){
        return ErrorResponse.builder()
                .resultCode(responseType.getResultCode())
                .description(responseType.getDescription())
                .build();
    }
}
