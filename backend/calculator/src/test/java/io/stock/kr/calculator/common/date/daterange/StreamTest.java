package io.stock.kr.calculator.common.date.daterange;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class StreamTest {

    @Test
    public void startDt에서부터_endDt까지의_날짜를_만들어낸다(){
        LocalDate startDt = LocalDate.of(2022, 1, 1);
        LocalDate endDt = LocalDate.of(2022, 2, 1);

        List<LocalDate> list = Stream.iterate(startDt, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDt, endDt) + 1)
                .collect(Collectors.toList());

        assertThat(list.get(list.size()-1)).isEqualTo(endDt);
    }
}
