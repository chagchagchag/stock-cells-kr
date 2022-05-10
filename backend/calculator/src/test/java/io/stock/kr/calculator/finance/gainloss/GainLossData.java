package io.stock.kr.calculator.finance.gainloss;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
public class GainLossData {
	@Getter private String periodValue;
	@Getter private GainLossPeriodType gainLossPeriodType;
	@Getter private BigDecimal totalProfit;
	@Getter private BigDecimal operatingProfit;
	@Getter private BigDecimal netIncome;

	@Builder
	public GainLossData(
		String periodValue,
		GainLossPeriodType gainLossPeriodType,
		BigDecimal totalProfit,
		BigDecimal operatingProfit,
		BigDecimal netIncome
	){
		this.periodValue = periodValue;
		this.gainLossPeriodType = gainLossPeriodType;
		this.totalProfit = totalProfit;
		this.operatingProfit = operatingProfit;
		this.netIncome = netIncome;
	}
}
