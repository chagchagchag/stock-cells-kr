package io.stock.kr.calculator.common.date;

import lombok.Builder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.stream.Stream;

public class DateRange implements Iterable<LocalDate>{
    private final LocalDate startDt;
    private final LocalDate endDt;

    @Builder
    public DateRange(LocalDate startDt, LocalDate endDt){
        this.startDt = startDt;
        this.endDt = endDt;
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return stream().iterator();
    }

    public Stream<LocalDate> stream(){
        return Stream.iterate(startDt, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDt, endDt) +1);
    }
}
