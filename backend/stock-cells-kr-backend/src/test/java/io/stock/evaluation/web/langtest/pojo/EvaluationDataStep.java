package io.stock.evaluation.web.langtest.pojo;

import java.util.Optional;
import java.util.function.Consumer;

public class EvaluationDataStep {
    private final Consumer<EvaluationData> generateData;
    private EvaluationDataStep next;

    public EvaluationDataStep(Consumer<EvaluationData> generateData){
        this.generateData = generateData;
    }

    public EvaluationDataStep setNext(EvaluationDataStep next){
        if(this.next == null){
            this.next = next;
        } else {
            this.next.setNext(next);
        }
        return this;
    }

    public void generate(EvaluationData evaluationData){
        generateData.accept(evaluationData);
        Optional.ofNullable(next)
                .ifPresent(nextStep -> nextStep.generate(evaluationData));
    }
}
