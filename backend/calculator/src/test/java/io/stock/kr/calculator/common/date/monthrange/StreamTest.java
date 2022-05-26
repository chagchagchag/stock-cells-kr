package io.stock.kr.calculator.common.date.monthrange;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamTest {

    @Test
    public void TEST_월_순회테스트(){
        LocalDate start = LocalDate.of(2019,1,1);
        LocalDate end = LocalDate.of(2021,1,1);
        List<LocalDate> list = Stream.iterate(start, dt -> dt.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(start, end)+1)
                .collect(Collectors.toList());

        assertThat(list.get(list.size()-1))
                .isEqualTo(end);
    }
}
