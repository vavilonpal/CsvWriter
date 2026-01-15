package org.writer.csv.pipeline.steps;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

public class ValidateHeaderStep implements PipelineStep<CsvContext> {
    @Override
    public void execute(CsvContext csvContext) {
        try (BufferedReader reader = Files.newBufferedReader(csvContext.getPath())) {
            String header = reader.readLine();
            boolean isHeaderEquals = header.equals(csvContext.getHeader());

            if (!isHeaderEquals) {
                throw new IllegalStateException(
                        "CSV header mismatch. Expected: "
                                + header + ", Generated: " + csvContext.getHeader()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
