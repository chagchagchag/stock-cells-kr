package io.stock.kr.calculator.finance.tdd;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.stock.kr.calculator.finance.gainloss.GainLossColumn;
import io.stock.kr.calculator.finance.gainloss.GainLossData;
import io.stock.kr.calculator.finance.gainloss.GainLossDto;
import io.stock.kr.calculator.finance.gainloss.GainLossPeriodType;
import io.stock.kr.calculator.finance.gainloss.GainLossPeriods;
import io.stock.kr.calculator.finance.gainloss.GainLossResult;
import io.stock.kr.calculator.request.crawling.html.ElementSelectorPair;
import io.stock.kr.calculator.request.crawling.html.ElementType;
import io.stock.kr.calculator.request.crawling.html.SelectorType;
import io.stock.kr.calculator.request.crawling.html.SpecifierType;
import io.stock.kr.calculator.request.fnguide.FnGuidePageParam;
import io.stock.kr.calculator.request.fnguide.ParameterPair;
import io.stock.kr.calculator.request.fnguide.ParameterType;

public class FinanceCrawlingFnGuideTest {
	final StringBuilder urlStringBuilder = new StringBuilder();
	final String baseURL = "http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp";
	final String tablesDivClassSelector = "ul_col2wrap pd_t25";
	final String tableSelector = "ul_co1_c pd_t1";

	public static final String TABLE_DIV_CLASS_SELECTOR 	= "ul_col2wrap pd_t25";
	public static final String TABLE_SELECTOR 			= "ul_co1_c pd_t1";

	final String yearlyGainLossTableSelector = "divSonikY";
	final String quarterlyGainLossTableSelector = "divSonikQ";

	record Param(String pageNoName, Integer pageNoValue, String companyNoName, String companyNoValue){};
	record TimeKeyDataValue(String time, String data){};
	record TableRow(String name, List<TimeKeyDataValue> eachTimeValues){};

	// 1) URL 구하는 기능 공통화 및 각종 파라미터, URL 상수화
	// = 내가 원하는것 ? 최대한 가변적이지 않은 프로그램으로 만들기
	@Test
	@DisplayName("크롤링할 페이지의 url 과 파라미터들을 조합한다.")
	public void TEST_REQUEST_URL(){
		ParameterPair pGb = new ParameterPair(ParameterType.pGb, "1");
		ParameterPair gicode = new ParameterPair(ParameterType.gicode, "A005930");

		FnGuidePageParam fnGuideParam = FnGuidePageParam.builder()
			.pageType(FnGuidePageParam.PageType.FINANCE)
			.parameterPairs(List.of(pGb, gicode))
			.build();

		String testURL = fnGuideParam.buildUrl();
		assertThat(testURL).isEqualTo("http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp?pGb=1&gicode=A005930");
	}

	// 2) FNGUIDE 접속 체크, FNGUIDE 측 파라미터 변경사항 있는지 체크
	@Test
	@DisplayName("FN GUIDE 접속 체크")
	public void TEST_FNGUIDE_CONNECT_SUCCESSFUL(){
		String requestUrl = newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, testParameters1());

		Optional<Document> document = getDocument(requestUrl);
		assertThat(document).isNotEmpty();
	}

	public Optional<Document> getDocument(String url){
		try {
			return Optional.ofNullable(Jsoup.connect(url).get());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public String newFnGuideUrl(FnGuidePageParam.PageType pageType, List<ParameterPair> parameterPairs){
		return FnGuidePageParam.builder()
			.pageType(FnGuidePageParam.PageType.FINANCE)
			.parameterPairs(parameterPairs)
			.build()
			.buildUrl();
	}

	Optional<Elements> findGainLossSection(Document document){
		ElementSelectorPair first = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, TABLE_DIV_CLASS_SELECTOR);
		ElementSelectorPair second = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, TABLE_SELECTOR);

		Elements divGainLossSection = document
			.select(first.ofSelector().toString())
			.select(second.ofSelector().toString());

		return Optional.ofNullable(divGainLossSection);
	}

	public Optional<Elements> findYearlyTableElement(Elements divGainLossSection){
		// 포괄 손익 계산 DIV
		ElementSelectorPair yearlyGainLossDiv = ofElementSelectorPair(ElementType.DIV, SelectorType.ID, SpecifierType.FULL, yearlyGainLossTableSelector);
		// 손익 계산 내에서 table 을 파싱
		ElementSelectorPair yearlyGainLossTable = ofElementSelectorPair(ElementType.TABLE, SelectorType.NONE, SpecifierType.NONE, "table");

		Elements yearlyTableElement = divGainLossSection
			.select(yearlyGainLossDiv.ofSelector().toString())
			.select(yearlyGainLossTable.ofSelector().toString());

		return Optional.ofNullable(yearlyTableElement);
	}

	// 1) 연도 파싱
	public Optional<GainLossPeriods> findYearsList(Elements yearlyTableElement){
		Stream<Element> limit = yearlyTableElement.select("tr").tagName("tr").stream().limit(1);
		Optional<GainLossPeriods> gainLossPeriods = yearlyTableElement.select("tr").tagName("tr").stream().findFirst()
			.map(thtd -> {
				List<String> data = thtd.select("th").tagName("th").eachText().stream().skip(1).limit(4).collect(
					Collectors.toList());

				GainLossPeriods periods = GainLossPeriods.builder()
					.firstPrev(data.get(GainLossPeriodType.FIRST_PREV.getIndexAs()))
					.secondPrev(data.get(GainLossPeriodType.SECOND_PREV.getIndexAs()))
					.thirdPrev(data.get(GainLossPeriodType.THIRD_PREV.getIndexAs()))
					.fourthPrev(data.get(GainLossPeriodType.FOURTH_PREV.getIndexAs()))
					.build();

				return periods;
			});

		return gainLossPeriods;
	}

	// 2) 각 항목 파싱 (매출액, 영업이익, 당기순이익)
	List<GainLossDto> findGainLossList(Elements yearlyTableElement){
		List<GainLossDto> values = yearlyTableElement.select("tr").tagName("tr")
			.stream().skip(1)
			.filter(thtd -> {
				if (thtd.tagName("th").select("div").text().equals("매출액")) return true;
				if (thtd.tagName("th").select("div").text().equals("영업이익")) return true;
				if (thtd.tagName("th").select("div").text().equals("당기순이익")) return true;
				else return false;
			})
			.map(thtd -> {
				String label = thtd.tagName("th").select("div").text();
				String value = thtd.tagName("td").select(".r").text();
				List<BigDecimal> valueList = Arrays.asList(value.split(" ")).subList(0, 4)
					.stream()
					.map(str -> {
						try {
							Number parse = NumberFormat.getNumberInstance(Locale.US).parse(str);
							BigDecimal parsedValue = new BigDecimal(parse.toString());
							return parsedValue;
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return null;
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
				return new GainLossDto(GainLossColumn.krTypeOf(label), valueList);
			})
			.collect(Collectors.toList());

		return values;
	}

	public void someThing(Elements yearlyTableElement){
		// 1) 연도 파싱
		Optional<GainLossPeriods> years = findYearsList(yearlyTableElement);
		years.ifPresent(yearsData-> {
			// 2) 각 항목 파싱 (매출액, 영업이익, 당기순이익)
			List<GainLossDto> gainLossList = findGainLossList(yearlyTableElement);

			// yearsData 와 gainLossList 를 인자로 하는 메서드 호출 구문 (collecResult)
		});

	}

	public void collectResult (List<GainLossPeriods> yearsList, List<GainLossDto> values){
		System.out.println("values = " + values);

		Map<GainLossColumn, Integer> columnIndexMap = IntStream
			.range(0, values.size()).boxed()
			.collect(Collectors.toMap(i -> values.get(i).getType(), Function.identity()));

		System.out.println("columnIndexMap = " + columnIndexMap);

		// 이 부분... GainLossDto 를 사용하는 부분... 자료형을 조금 더 종류를 줄일 방법있을까?
		// 그림 그려보고 중복되는 부분 찾아내기.
		System.out.println("===AAA===");
		GainLossDto totalProfit = values.get(columnIndexMap.get(GainLossColumn.TotalProfit));
		BigDecimal firstTotalProfit = totalProfit.getValues().get(GainLossPeriodType.FIRST_PREV.getIndexAs());
		BigDecimal secondTotalProfit = totalProfit.getValues().get(GainLossPeriodType.SECOND_PREV.getIndexAs());
		BigDecimal thirdTotalProfit = totalProfit.getValues().get(GainLossPeriodType.THIRD_PREV.getIndexAs());
		BigDecimal fourthTotalProfit = totalProfit.getValues().get(GainLossPeriodType.FOURTH_PREV.getIndexAs());

		GainLossDto opProfit = values.get(columnIndexMap.get(GainLossColumn.OperatingProfit));
		BigDecimal firstOpProfit = opProfit.getValues().get(GainLossPeriodType.FIRST_PREV.getIndexAs());
		BigDecimal secondOpProfit = opProfit.getValues().get(GainLossPeriodType.SECOND_PREV.getIndexAs());
		BigDecimal thirdOpProfit = opProfit.getValues().get(GainLossPeriodType.THIRD_PREV.getIndexAs());
		BigDecimal fourthOpProfit = opProfit.getValues().get(GainLossPeriodType.FOURTH_PREV.getIndexAs());

		GainLossDto netIncome = values.get(columnIndexMap.get(GainLossColumn.NetIncome));
		BigDecimal firstNetIncome = netIncome.getValues().get(GainLossPeriodType.FIRST_PREV.getIndexAs());
		BigDecimal secondNetIncome = netIncome.getValues().get(GainLossPeriodType.SECOND_PREV.getIndexAs());
		BigDecimal thirdNetIncome = netIncome.getValues().get(GainLossPeriodType.THIRD_PREV.getIndexAs());
		BigDecimal fourthNetIncome = netIncome.getValues().get(GainLossPeriodType.FOURTH_PREV.getIndexAs());

		GainLossData firstGainLoss = GainLossData.builder()
			.periodValue(yearsList.get(0).getFirstPrev())
			.gainLossPeriodType(GainLossPeriodType.FIRST_PREV)
			.totalProfit(firstTotalProfit)
			.operatingProfit(firstOpProfit)
			.netIncome(firstNetIncome)
			.build();

		GainLossData secondGainLoss = GainLossData.builder()
			.periodValue(yearsList.get(0).getSecondPrev())
			.gainLossPeriodType(GainLossPeriodType.SECOND_PREV)
			.totalProfit(secondTotalProfit)
			.operatingProfit(secondOpProfit)
			.netIncome(secondNetIncome)
			.build();

		GainLossData thirdGainLoss = GainLossData.builder()
			.periodValue(yearsList.get(0).getThirdPrev())
			.gainLossPeriodType(GainLossPeriodType.THIRD_PREV)
			.totalProfit(thirdTotalProfit)
			.operatingProfit(thirdOpProfit)
			.netIncome(thirdNetIncome)
			.build();

		GainLossData fourthGainLoss = GainLossData.builder()
			.periodValue(yearsList.get(0).getFourthPrev())
			.gainLossPeriodType(GainLossPeriodType.FOURTH_PREV)
			.totalProfit(fourthTotalProfit)
			.operatingProfit(fourthOpProfit)
			.netIncome(fourthNetIncome)
			.build();

		GainLossResult d = GainLossResult.builder()
			.firstPrev(firstGainLoss)
			.secondPrev(secondGainLoss)
			.thirdPrev(thirdGainLoss)
			.fourthPrev(fourthGainLoss)
			.build();

		System.out.println("d = " + d);
		System.out.println("===");
	}

	//=TDD완료후이관예정
	// 아직은 메서드 단위, 의미 단위로 분리가 더 필요하다.
	@Test
	@DisplayName("JSoup 셀렉터 기반 조회 - 연간")
	public void TEST_SEARCHING_JSOUP_BASED_YEARLY_DATA(){
		getDocument(newFnGuideUrl(FnGuidePageParam.PageType.FINANCE, testParameters1())).ifPresent(document -> {
			ElementSelectorPair first = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, tablesDivClassSelector);
			ElementSelectorPair second = ofElementSelectorPair(ElementType.DIV, SelectorType.CLASS, SpecifierType.FULL, tableSelector);

			Elements divGainLossSection = document
				.select(first.ofSelector().toString())
				.select(second.ofSelector().toString());

			//            System.out.println(divGainLossSection);

			// 포괄 손익 계산 DIV
			ElementSelectorPair yearlyGainLossDiv = ofElementSelectorPair(ElementType.DIV, SelectorType.ID, SpecifierType.FULL, yearlyGainLossTableSelector);
			// 손익 계산 내에서 table 을 파싱
			ElementSelectorPair yearlyGainLossTable = ofElementSelectorPair(ElementType.TABLE, SelectorType.NONE, SpecifierType.NONE, "table");

			Elements yearlyTableElement = divGainLossSection
				.select(yearlyGainLossDiv.ofSelector().toString())
				.select(yearlyGainLossTable.ofSelector().toString());

			//            System.out.println(yearlyTableElement);

			// 1) 연도 파싱
			List<GainLossPeriods> yearsList = yearlyTableElement.select("tr").tagName("tr")
				.stream().limit(1)
				.map(thtd -> {
					List<String> data = thtd.select("th").tagName("th").eachText().stream().skip(1).limit(4).collect(
						Collectors.toList());

					GainLossPeriods periods = GainLossPeriods.builder()
						.firstPrev(data.get(GainLossPeriodType.FIRST_PREV.getIndexAs()))
						.secondPrev(data.get(GainLossPeriodType.SECOND_PREV.getIndexAs()))
						.thirdPrev(data.get(GainLossPeriodType.THIRD_PREV.getIndexAs()))
						.fourthPrev(data.get(GainLossPeriodType.FOURTH_PREV.getIndexAs()))
						.build();

					return periods;
				})
				.collect(Collectors.toList());

			System.out.println("years = " + yearsList.get(0));

			// 2) 각 항목 파싱 (매출액, 영업이익, 당기순이익)
			List<GainLossDto> values = yearlyTableElement.select("tr").tagName("tr")
				.stream().skip(1)
				.filter(thtd -> {
					if (thtd.tagName("th").select("div").text().equals("매출액")) return true;
					if (thtd.tagName("th").select("div").text().equals("영업이익")) return true;
					if (thtd.tagName("th").select("div").text().equals("당기순이익")) return true;
					else return false;
				})
				.map(thtd -> {
					String label = thtd.tagName("th").select("div").text();
					String value = thtd.tagName("td").select(".r").text();
					List<BigDecimal> valueList = Arrays.asList(value.split(" ")).subList(0, 4)
						.stream()
						.map(str -> {
							try {
								Number parse = NumberFormat.getNumberInstance(Locale.US).parse(str);
								BigDecimal parsedValue = new BigDecimal(parse.toString());
								return parsedValue;
							} catch (ParseException e) {
								e.printStackTrace();
							}
							return null;
						})
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
					return new GainLossDto(GainLossColumn.krTypeOf(label), valueList);
				})
				.collect(Collectors.toList());

			System.out.println("values = " + values);

			Map<GainLossColumn, Integer> columnIndexMap = IntStream
				.range(0, values.size()).boxed()
				.collect(Collectors.toMap(i -> values.get(i).getType(), Function.identity()));

			System.out.println("columnIndexMap = " + columnIndexMap);

			// 이 부분... GainLossDto 를 사용하는 부분... 자료형을 조금 더 종류를 줄일 방법있을까?
			// 그림 그려보고 중복되는 부분 찾아내기.
			System.out.println("===AAA===");
			GainLossDto totalProfit = values.get(columnIndexMap.get(GainLossColumn.TotalProfit));
			BigDecimal firstTotalProfit = totalProfit.getValues().get(GainLossPeriodType.FIRST_PREV.getIndexAs());
			BigDecimal secondTotalProfit = totalProfit.getValues().get(GainLossPeriodType.SECOND_PREV.getIndexAs());
			BigDecimal thirdTotalProfit = totalProfit.getValues().get(GainLossPeriodType.THIRD_PREV.getIndexAs());
			BigDecimal fourthTotalProfit = totalProfit.getValues().get(GainLossPeriodType.FOURTH_PREV.getIndexAs());

			GainLossDto opProfit = values.get(columnIndexMap.get(GainLossColumn.OperatingProfit));
			BigDecimal firstOpProfit = opProfit.getValues().get(GainLossPeriodType.FIRST_PREV.getIndexAs());
			BigDecimal secondOpProfit = opProfit.getValues().get(GainLossPeriodType.SECOND_PREV.getIndexAs());
			BigDecimal thirdOpProfit = opProfit.getValues().get(GainLossPeriodType.THIRD_PREV.getIndexAs());
			BigDecimal fourthOpProfit = opProfit.getValues().get(GainLossPeriodType.FOURTH_PREV.getIndexAs());

			GainLossDto netIncome = values.get(columnIndexMap.get(GainLossColumn.NetIncome));
			BigDecimal firstNetIncome = netIncome.getValues().get(GainLossPeriodType.FIRST_PREV.getIndexAs());
			BigDecimal secondNetIncome = netIncome.getValues().get(GainLossPeriodType.SECOND_PREV.getIndexAs());
			BigDecimal thirdNetIncome = netIncome.getValues().get(GainLossPeriodType.THIRD_PREV.getIndexAs());
			BigDecimal fourthNetIncome = netIncome.getValues().get(GainLossPeriodType.FOURTH_PREV.getIndexAs());

			GainLossData firstGainLoss = GainLossData.builder()
				.periodValue(yearsList.get(0).getFirstPrev())
				.gainLossPeriodType(GainLossPeriodType.FIRST_PREV)
				.totalProfit(firstTotalProfit)
				.operatingProfit(firstOpProfit)
				.netIncome(firstNetIncome)
				.build();

			GainLossData secondGainLoss = GainLossData.builder()
				.periodValue(yearsList.get(0).getSecondPrev())
				.gainLossPeriodType(GainLossPeriodType.SECOND_PREV)
				.totalProfit(secondTotalProfit)
				.operatingProfit(secondOpProfit)
				.netIncome(secondNetIncome)
				.build();

			GainLossData thirdGainLoss = GainLossData.builder()
				.periodValue(yearsList.get(0).getThirdPrev())
				.gainLossPeriodType(GainLossPeriodType.THIRD_PREV)
				.totalProfit(thirdTotalProfit)
				.operatingProfit(thirdOpProfit)
				.netIncome(thirdNetIncome)
				.build();

			GainLossData fourthGainLoss = GainLossData.builder()
				.periodValue(yearsList.get(0).getFourthPrev())
				.gainLossPeriodType(GainLossPeriodType.FOURTH_PREV)
				.totalProfit(fourthTotalProfit)
				.operatingProfit(fourthOpProfit)
				.netIncome(fourthNetIncome)
				.build();

			GainLossResult d = GainLossResult.builder()
				.firstPrev(firstGainLoss)
				.secondPrev(secondGainLoss)
				.thirdPrev(thirdGainLoss)
				.fourthPrev(fourthGainLoss)
				.build();

			System.out.println("d = " + d);
			System.out.println("===");
		});
	}

	//=TDD완료후이관예정
	//=TDD 완료된 기능들에 한정해서 패키지 설계 등을 거쳐서 소스레벨로 이관 예정.
	public ElementSelectorPair ofElementSelectorPair(ElementType elementType, SelectorType selectorType, SpecifierType specifierType, String specifier){
		Objects.requireNonNull(elementType);
		Objects.requireNonNull(selectorType);
		Objects.requireNonNull(specifierType);

		return ElementSelectorPair.builder()
			.elementType(elementType)
			.selectorType(selectorType)
			.specifierType(specifierType)
			.specifier(specifier)
			.build();
	}

	/**
	 * (START) 테스트 용도 파라미터 조합, 객체 생성 팩토리 메서드
	 */
	public List<ParameterPair> testParameters1(){
		ParameterPair pGb = new ParameterPair(ParameterType.pGb, "1");
		ParameterPair gicode = new ParameterPair(ParameterType.gicode, "A005930");
		return List.of(pGb, gicode);
	}

}
