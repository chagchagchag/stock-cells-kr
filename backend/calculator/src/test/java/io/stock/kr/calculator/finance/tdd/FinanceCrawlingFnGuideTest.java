package io.stock.kr.calculator.finance.tdd;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.stock.kr.calculator.finaince.gainloss.crawler.GainLossCrawlerService;
import io.stock.kr.calculator.finaince.gainloss.crawler.dto.GainLossValue;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.stock.kr.calculator.finaince.gainloss.crawler.dto.GainLossColumn;
import io.stock.kr.calculator.finaince.gainloss.crawler.dto.GainLossPeriodType;
import io.stock.kr.calculator.finaince.gainloss.crawler.dto.GainLossPeriods;
import io.stock.kr.calculator.request.crawling.html.ElementSelectorPair;
import io.stock.kr.calculator.request.crawling.html.ElementType;
import io.stock.kr.calculator.request.crawling.html.SelectorType;
import io.stock.kr.calculator.request.crawling.html.SpecifierType;
import io.stock.kr.calculator.request.fnguide.FnGuidePageParam;
import io.stock.kr.calculator.request.fnguide.ParameterPair;
import io.stock.kr.calculator.request.fnguide.ParameterType;

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
