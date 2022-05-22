package io.stock.kr.calculator.stock.meta.crawling.tdd;

import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDocument;
import io.stock.kr.calculator.stock.price.repository.dynamo.PriceDayDynamoDBMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * docker 기반 테스트로 분리 예정
 */
@SpringBootTest
@ActiveProfiles("live")
public class LiveStockInsertTest {

    @Autowired
    PriceDayDynamoDBMapper priceDayDynamoDBMapper;

    @Test
    public void saveStock(){
        PriceDayDocument msft = PriceDayDocument.builder()
                .ticker("TSLA")
                .tradeDt(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)))
                .open(new BigDecimal(222))
                .high(new BigDecimal(111))
                .low(new BigDecimal(111))
                .close(new BigDecimal(222))
                .build();

        PriceDayDocument aapl = PriceDayDocument.builder()
                .ticker("AAPL")
                .tradeDt(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0)))
                .open(new BigDecimal(222))
                .high(new BigDecimal(111))
                .low(new BigDecimal(111))
                .close(new BigDecimal(222))
                .build();

        priceDayDynamoDBMapper.batchWritePriceDayList(List.of(msft, aapl));
    }

}
