package io.stock.kr.calculator.finance.tdd;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.stock.kr.calculator.request.fnguide.FnGuidePageParam;
import io.stock.kr.calculator.request.fnguide.ParameterPair;
import io.stock.kr.calculator.request.fnguide.ParameterType;

public class FinanceCrawlingFnGuideTest {
	final StringBuilder urlStringBuilder = new StringBuilder();
	final String baseURL = "http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp";
	final String tablesDivClassSelector = "ul_col2wrap pd_t25";
	final String tableSelector = "ul_co1_c pd_t1";

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
}
