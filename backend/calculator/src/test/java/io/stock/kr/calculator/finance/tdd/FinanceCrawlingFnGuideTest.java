package io.stock.kr.calculator.finance.tdd;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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




	/**
	 * (START) 테스트 용도 파라미터 조합, 객체 생성 팩토리 메서드
	 */
	public List<ParameterPair> testParameters1(){
		ParameterPair pGb = new ParameterPair(ParameterType.pGb, "1");
		ParameterPair gicode = new ParameterPair(ParameterType.gicode, "A005930");
		return List.of(pGb, gicode);
	}

}
