package org.writer.csv.pipeline.steps;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;
import org.writer.exception.ArrayIsEmptyException;

public class ValidateDataStep implements PipelineStep<CsvContext> {
    /**
     * Validates that the data list is not empty.
     *
     * @param csvContext context containing the data to be written
     * @throws ArrayIsEmptyException if the data list is empty
     */
    @Override
    public void execute(CsvContext csvContext) {
        if (csvContext.getData().isEmpty()) {
            throw new ArrayIsEmptyException("Fill array with data!");
        }
    }
}
