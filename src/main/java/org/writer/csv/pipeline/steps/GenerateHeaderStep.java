package org.writer.csv.pipeline.steps;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;
import org.writer.exception.CsvHeaderMismatchException;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;


public class GenerateHeaderStep implements PipelineStep<CsvContext> {
    /**
     * Generates a CSV header based on the model fields and stores it in the context.
     * If the CSV file already exists, validates that the generated header matches
     * the header stored in the file.
     *
     * @param csvContext context containing file path and model metadata
     * @throws CsvHeaderMismatchException if the existing file header does not match the generated one
     * @throws RuntimeException           if an I/O error occurs while reading the file
     */
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
