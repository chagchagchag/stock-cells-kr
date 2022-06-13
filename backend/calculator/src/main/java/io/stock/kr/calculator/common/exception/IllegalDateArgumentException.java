package io.stock.kr.calculator.common.exception;

import io.stock.kr.calculator.common.exception.type.DateExceptionType;

public class IllegalDateArgumentException extends IllegalArgumentException{
    public IllegalDateArgumentException(DateExceptionType exceptionType){
        super(exceptionType.getDescription());
    }
}
