package org.writer.csv.pipeline.steps;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GenerateHeaderStep implements PipelineStep<CsvContext> {
    @Override
    public void execute(CsvContext csvContext) {
        csvContext.setHeader(Arrays.stream(csvContext.getFields())
                .map(Field::getName)
                .collect(Collectors.joining(",")));
    }
}
