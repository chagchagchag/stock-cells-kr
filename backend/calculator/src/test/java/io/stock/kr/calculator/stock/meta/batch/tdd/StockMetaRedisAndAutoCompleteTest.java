package io.stock.kr.calculator.stock.meta.batch.tdd;

import io.stock.kr.calculator.stock.meta.crawling.StockMetaCrawlingDartService;
import io.stock.kr.calculator.stock.meta.crawling.dto.StockMetaDto;
import io.stock.kr.calculator.stock.meta.repository.redis.TickerMetaRedis;
import io.stock.kr.calculator.stock.meta.repository.redis.TickerMetaRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test-docker")
public class StockMetaRedisAndAutoCompleteTest {

    @Autowired
    private StockMetaCrawlingDartService dartService;

    @Autowired
    private TickerMetaRedisRepository redisRepository;

    @BeforeEach
    public void init(){

    }

    /**
     * 1) 로컬 도커 레디스에 데이터 저장기능 검증
     * 2) 저장된 데이터를 기반으로 검색어 자동완성 검증
     * 3) 서비스로 분리
     * 4) (6/16) Batch 환경설정, Batch 코드 구현, Batch 테스트 코드 구현, 터미널에서 Batch 코드 잡 구동
     * 5) (6/17) 미비작업
     */

    @Test
    public void TEST_READ_DART_TICKER_LIST(){
        List<StockMetaDto> tickerList = dartService.selectKrStockList("CORPCODE.xml");
        assertThat(tickerList).isNotEmpty();

        List<TickerMetaRedis> tickerMetaRedisList = tickerList.stream()
                .map(stockMetaDto -> {
                    return TickerMetaRedis.builder()
                            .ticker(stockMetaDto.getTicker())
                            .companyName(stockMetaDto.getCompanyName())
                            .build();
                })
                .collect(Collectors.toList());

        redisRepository.saveAll(tickerMetaRedisList);
        assertThat(tickerMetaRedisList.size()).isEqualTo(redisRepository.findAll().size());
    }
}
