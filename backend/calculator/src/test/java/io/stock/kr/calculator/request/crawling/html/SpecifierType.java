package io.stock.kr.calculator.request.crawling.html;

import lombok.Getter;

public enum SpecifierType {
	FULL("FULL"), SHORT("SHORT"), NONE("NONE");
	@Getter
	private final String specifierTypeName;

	SpecifierType(String specifierTypeName){
		this.specifierTypeName = specifierTypeName;
	}
}
