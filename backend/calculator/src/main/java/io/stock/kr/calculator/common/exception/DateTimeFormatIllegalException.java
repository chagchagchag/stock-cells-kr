package io.stock.kr.calculator.common.exception;

import io.stock.kr.calculator.common.exception.type.DateTimeExceptionType;

public class DateTimeFormatIllegalException extends RuntimeException{
    public DateTimeFormatIllegalException(DateTimeExceptionType dateTimeExceptionType){
        super(dateTimeExceptionType.getDescription());
    }
}
