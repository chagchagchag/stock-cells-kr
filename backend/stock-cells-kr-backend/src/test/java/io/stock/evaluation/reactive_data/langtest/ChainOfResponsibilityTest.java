package io.stock.evaluation.reactive_data.langtest;

import io.stock.evaluation.reactive_data.langtest.pojo.EvaluationDataStatus;
import io.stock.evaluation.reactive_data.langtest.pojo.EvaluationDataStep;
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
