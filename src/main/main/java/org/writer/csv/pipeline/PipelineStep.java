package org.writer.csv.pipeline;

import org.writer.csv.context.ContextInterface;

public interface PipelineStep<TContext extends ContextInterface> {
    void execute(TContext context);
}
