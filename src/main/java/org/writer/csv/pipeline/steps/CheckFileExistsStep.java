package org.writer.csv.pipeline.steps;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;

import java.io.IOException;
import java.nio.file.Files;

public class CheckFileExistsStep implements PipelineStep<CsvContext> {
    /**
     * Checks whether the target CSV file exists and is not empty.
     * Stores the result in the CsvContext.
     *
     * @param csvContext context containing the path to the CSV file
     * @throws RuntimeException if an I/O error occurs while checking the file
     */

    @Override
    public void execute(CsvContext csvContext) {
        try {
            boolean isFileExists = Files.exists(csvContext.getPath()) && Files.size(csvContext.getPath()) > 0;
            csvContext.setFileExists(isFileExists);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
