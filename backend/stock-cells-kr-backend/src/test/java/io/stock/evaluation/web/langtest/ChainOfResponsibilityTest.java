package io.stock.evaluation.web.langtest;

import io.stock.evaluation.web.langtest.pojo.EvaluationDataStatus;
import io.stock.evaluation.web.langtest.pojo.EvaluationDataStep;
import org.junit.jupiter.api.Test;

public class ChainOfResponsibilityTest {

    @Test
    public void TEST(){
        EvaluationDataStep firstStep = new EvaluationDataStep(evaluationData -> {
            if (evaluationData.getStatus() == EvaluationDataStatus.CREATED) {
                evaluationData.setStatus(EvaluationDataStatus.IN_PROGRESS);
            }
        });

//        new EvaluationDataStep(evaluationData -> {
//            if(evaluationData.getStatus() == EvaluationDataStatus.IN_PROGRESS){
//
//            }
//        })
    }
}
