package io.stock.kr.calculator.common.date;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.stream.Stream;

@Getter
public class MonthRange implements Iterable<LocalDate>{
    private final LocalDate startDate;
    private final LocalDate endDate;

    @Builder
    public MonthRange(
        LocalDate startDate, LocalDate endDate
    ){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return stream().iterator();
    }

    public Stream<LocalDate> stream(){
        return Stream.iterate(startDate, d -> d.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(startDate, endDate));
    }
}
