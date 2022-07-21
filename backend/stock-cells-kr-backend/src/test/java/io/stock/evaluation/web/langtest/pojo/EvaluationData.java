package io.stock.evaluation.web.langtest.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EvaluationData {
    @Setter EvaluationDataStatus status;
    private final List<EvaluationData> dataList = new ArrayList<>();
    BigDecimal totalAmount;
}
