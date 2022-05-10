package io.stock.kr.calculator.finance.gainloss;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum GainLossPeriodType {
	FIRST_PREV("firstPrev", 3){
		@Override
		public String getPeriodValue(GainLossPeriods gainLossPeriods) {
			return gainLossPeriods.getFirstPrev();
		}
	},
	SECOND_PREV("secondPrev", 2){
		@Override
		public String getPeriodValue(GainLossPeriods gainLossPeriods) {
			return gainLossPeriods.getSecondPrev();
		}
	},
	THIRD_PREV("thirdPrev", 1){
		@Override
		public String getPeriodValue(GainLossPeriods gainLossPeriods) {
			return gainLossPeriods.getThirdPrev();
		}
	},
	FOURTH_PREV("fourthPrev", 0){
		@Override
		public String getPeriodValue(GainLossPeriods gainLossPeriods) {
			return gainLossPeriods.getFourthPrev();
		}
	};

	@Getter
	private final String periodLabel;
	@Getter private final int indexAs;

	static Map<Integer, GainLossPeriodType> periodMap = new HashMap<>();

	GainLossPeriodType(String periodLabel, int indexAs){
		this.periodLabel = periodLabel;
		this.indexAs = indexAs;
	}

	public abstract String getPeriodValue(GainLossPeriods gainLossPeriods);
}
