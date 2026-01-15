package org.writer.csv.pipeline.steps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;


public class GenerateHeaderStep implements PipelineStep<CsvContext> {
    @Override
    public void execute(CsvContext csvContext) {
        csvContext.setHeader(Arrays.stream(csvContext.getFields())
                .map(Field::getName)
                .collect(Collectors.joining(",")));

        if (csvContext.isFileExists()) {
            try (BufferedReader reader = Files.newBufferedReader(csvContext.getPath())) {
                String fileHeader = reader.readLine();

                if (!fileHeader.equals(csvContext.getHeader())) {
                    throw new CsvHeaderMismatchException(
                            "CSV header mismatch. Expected: "
                                    + csvContext.getHeader()
                                    + ", Actual: "
                                    + fileHeader
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
