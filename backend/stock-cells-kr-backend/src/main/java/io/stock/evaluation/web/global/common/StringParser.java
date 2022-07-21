package io.stock.evaluation.web.global.common;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;


public class StringParser {
    public static Function<String, Optional<BigDecimal>> toDecimal(){
        return str -> {
            try {
                Number parse = NumberFormat.getNumberInstance(Locale.US).parse(str);
                return Optional.of(new BigDecimal(parse.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        };
    }

    public static Function<String, Optional<BigDecimal>> krStringToDecimal(){
        return given -> {
            if(given.contains("조")){
                int t = given.indexOf("조");
                StringBuilder test = new StringBuilder(given.substring(0, t)).append(given.substring(t + 2));

                return StringParser.toDecimal().apply(test.toString())
                                .map(decimal -> {
                                    BigDecimal multiply = decimal.multiply(BigDecimal.valueOf(100000000));
                                    return multiply;
                                });
            }
            else{
                return StringParser.krStringToDecimal().apply(given);
            }
        };
    }
}
