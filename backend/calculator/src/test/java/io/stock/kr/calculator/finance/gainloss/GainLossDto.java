package io.stock.kr.calculator.finance.gainloss;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
public class GainLossDto {
	@Getter
	private final GainLossColumn type;
	//    @Getter private final List<String> values;
	@Getter private final List<BigDecimal> values;

	@Builder
	public GainLossDto(GainLossColumn type, List<BigDecimal> values){
		this.type = type;
		this.values = values;
	}
}
