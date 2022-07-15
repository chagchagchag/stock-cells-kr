package io.stock.evaluation.reactive_data.crawling.stock.price.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Parameter{
    String type;
    String value;

    public Parameter(String type, String value){
        this.type = type;
        this.value = value;
    }
}
