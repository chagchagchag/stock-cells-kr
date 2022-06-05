package io.stock.kr.calculator.common.date;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public enum DateFormatType {
    DASH_yyyyMMdd("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    yyyyMMdd("yyyyMMdd", DateTimeFormatter.ofPattern("yyyyMMdd")),
    MMddyyyy("MMddyyyy", DateTimeFormatter.ofPattern("MMddyyyy"));

    private final String formatString;
    private final DateTimeFormatter formatter;

    DateFormatType(String formatString, DateTimeFormatter formatter){
        this.formatString = formatString;
        this.formatter = formatter;
    }

    public LocalDate parse(String dateString){
        return LocalDate.parse(dateString, this.formatter);
    }

    public String format(LocalDate localDate){
        return localDate.format(this.formatter);
    }
}
