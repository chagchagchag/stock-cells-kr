package io.stock.kr.calculator.request.fnguide;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class FnGuidePageParam {
	private final StringBuilder builder = new StringBuilder();

	@Getter
	private final PageType pageType;
	@Getter private final List<ParameterPair> parameterPairs;

	@Builder
	public FnGuidePageParam(
		PageType pageType,
		List<ParameterPair> parameterPairs
	){
		this.pageType = pageType;
		this.parameterPairs = parameterPairs;
	}

	public ParameterPair pair(ParameterPair pair){
		builder.append(pair.getParameterType().getParamName())
			.append("=")
			.append(pair.getValue());
		return pair;
	}

	public void and(ParameterPair pair){
		builder.append("&");
	}

	public String stripLastAnd(){
		if(builder.lastIndexOf("&") == builder.length() -1)
			return builder.substring(0, builder.length()-1);
		return builder.toString();
	}

	public String buildUrl(){
		builder.append(pageType.getBaseUrl()).append("?");

		parameterPairs.stream()
			.map(this::pair)
			.forEach(this::and);

		// 이 부분은 조금 마음에 안든다. ㅠㅠ
		return stripLastAnd();
	}

	public enum PageType{
		MAIN("MAIN", "Snapshot", "https://comp.fnguide.com/SVO2/ASP/SVD_Main.asp"){
			@Override
			public String requestUrl(FnGuidePageParam param) {
				return null;
			}
		},
		COMPANY_OVERVIEW("COMPANY_OVERVIEW", "기업개요", "https://comp.fnguide.com/SVO2/ASP/SVD_Corp.asp"){
			@Override
			public String requestUrl(FnGuidePageParam param) {
				return null;
			}
		},
		FINANCE("FINANCE", "재무제표", "http://comp.fnguide.com/SVO2/ASP/SVD_Finance.asp"){
			@Override
			public String requestUrl(FnGuidePageParam param) {
				return null;
			}
		};

		@Getter private final String menuEn;
		@Getter private final String menuKr;
		@Getter private final String baseUrl;

		PageType(String menuEn, String menuKr, String baseUrl){
			this.menuEn = menuEn;
			this.menuKr = menuKr;
			this.baseUrl = baseUrl;
		}

		public abstract String requestUrl(FnGuidePageParam param);
	}
}
