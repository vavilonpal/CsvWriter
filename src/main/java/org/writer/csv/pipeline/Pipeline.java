package org.writer.csv.pipeline;

import org.writer.csv.context.ContextInterface;

import java.util.LinkedList;
import java.util.List;

public class Pipeline<TContext extends ContextInterface> {
    private final List<PipelineStep<TContext>> steps = new LinkedList<>();

    public void execute(TContext context) {
        steps.forEach(pipelineStep -> pipelineStep.execute(context));
    }

    public void addStep(PipelineStep<TContext> pipelineStep) {
        steps.add(pipelineStep);
    }
    public void  addStepByCondition(boolean condition, PipelineStep<TContext> firstStep, PipelineStep<TContext> secondStep){
        if (condition){
            steps.add(firstStep);
        }else {
            steps.add(secondStep);
        }
    }

}
