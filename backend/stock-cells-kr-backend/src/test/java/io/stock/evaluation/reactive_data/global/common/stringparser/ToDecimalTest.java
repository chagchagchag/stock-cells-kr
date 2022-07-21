package io.stock.evaluation.reactive_data.global.common.stringparser;

import io.stock.evaluation.reactive_data.global.common.StringParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 테스트 데이터
 * {
 *     "per": "9.21",
 *     "pbr": "1.30",
 *     "eps": "6,372",
 *     "marketSum": "350조 4,262",
 *     "dvr": "2.46"
 * }
 */
public class ToDecimalTest {
    @Test
    public void 소수점_숫자문자열_테스트(){
        String given = "9.21";
        Optional<BigDecimal> converted = StringParser.toDecimal().apply(given);
        assertThat(converted).isNotEmpty();
        assertThat(converted.get()).isEqualTo(new BigDecimal(given));
    }

    @Test
    public void 컴마_숫자문자열_테스트1(){
        String given = "6,732";
        Optional<BigDecimal> converted = StringParser.toDecimal().apply(given);
        assertThat(converted).isNotEmpty();
        assertThat(converted.get()).isEqualTo(new BigDecimal("6732"));
    }

    @Test
    public void 컴마_숫자문자열_테스트2(){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        String pattern ="#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        String given = "6,732";
        try {
            BigDecimal converted = (BigDecimal) decimalFormat.parse(given);
            assertThat(converted).isEqualTo(new BigDecimal(6732));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
