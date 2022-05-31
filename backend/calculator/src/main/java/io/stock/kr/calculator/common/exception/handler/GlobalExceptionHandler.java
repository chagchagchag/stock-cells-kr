package io.stock.kr.calculator.common.exception.handler;

import io.stock.kr.calculator.common.exception.DateTimeFormatIllegalException;
import io.stock.kr.calculator.common.exception.ErrorResponse;
import io.stock.kr.calculator.common.exception.type.DateExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeFormatIllegalException.class)
    protected ErrorResponse handleDateTimeFormatIllegalException(DateTimeFormatIllegalException e){
        log.error(">> GlobalExceptionHandler, handleDateTimeParseException ", e);
        return ErrorResponse.of(DateExceptionType.DATE_FORMAT_INVALID);
    }
}