package io.stock.kr.calculator.common.exception;

import io.stock.kr.calculator.common.exception.type.DateExceptionType;

public class DateFormatIllegalException extends RuntimeException{
    public DateFormatIllegalException(DateExceptionType exceptionType){
        super(exceptionType.getDescription());
    }

    public DateFormatIllegalException(DateExceptionType exceptionType, Throwable cause){
        super(exceptionType.getDescription(), cause);
    }
}
