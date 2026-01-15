package org.writer.csv.pipeline.steps;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

public class ResolveMetadataStep implements PipelineStep<CsvContext> {
    @Override
    public void execute(CsvContext csvContext) {
        csvContext.setClazz(csvContext.getData().get(0).getClass());

        Field[] fields = csvContext.getClazz().getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        for (Field f : fields) {
            f.setAccessible(true);
        }
        csvContext.setFields(fields);
        System.out.println("Metadata is set...");
    }
}
