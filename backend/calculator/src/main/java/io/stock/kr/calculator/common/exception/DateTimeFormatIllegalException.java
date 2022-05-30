package io.stock.kr.calculator.common.exception;

import io.stock.kr.calculator.common.exception.type.DateExceptionType;

public class DateTimeFormatIllegalException extends RuntimeException{
    public DateTimeFormatIllegalException(DateExceptionType dateExceptionType){
        super(dateExceptionType.getDescription());
    }
}
