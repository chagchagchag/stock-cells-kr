package io.stock.kr.calculator.finance.tdd;

import static org.assertj.core.api.Assertions.*;

import java.util.*;

import io.stock.kr.calculator.finance.gainloss.crawler.GainLossCrawlerService;
import io.stock.kr.calculator.finance.gainloss.crawler.dto.GainLossValue;
import org.junit.jupiter.api.Test;

public class FinanceCrawlingFnGuideTest {
	@Test
	public void 연간_손익계산서조회_삼성전자(){
		GainLossCrawlerService service = new GainLossCrawlerService();
		List<GainLossValue> gainLossDataYearly = service.findGainLossDataYearly("A005930");
		assertThat(gainLossDataYearly).isNotEmpty();
	}

	@Test
	public void 분기_손익계산서조회_삼성전자(){
		GainLossCrawlerService service = new GainLossCrawlerService();
		List<GainLossValue> gainLossDataQuarterly = service.findGainLossDataQuarterly("A005930");
		assertThat(gainLossDataQuarterly).isNotEmpty();
	}

}
