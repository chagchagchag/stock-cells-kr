package io.stock.evaluation.reactive_data.global.common.stringparser;

import io.stock.evaluation.reactive_data.global.common.StringParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class KrStringToDecimalTest {

    Pattern pattern = Pattern.compile("\\*'조'");

    @Test
    public void 한국_숫자문자열_테스트(){
        String input = "350조 4,262";
        Optional<BigDecimal> data = StringParser.krStringToDecimal().apply(input);
        assertThat(data).isNotEmpty();
        assertThat(data.get()).isEqualTo(new BigDecimal("350426200000000"));
    }
}
