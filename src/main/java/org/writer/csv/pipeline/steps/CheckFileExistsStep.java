package org.writer.csv.pipeline.steps;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;

import java.io.IOException;
import java.nio.file.Files;

public class CheckFileExistsStep implements PipelineStep<CsvContext> {
    @Override
    public void execute(CsvContext csvcontext) {
        try {
            boolean isFileExists = Files.exists(csvcontext.getPath()) && Files.size(csvcontext.getPath()) > 0;
            csvcontext.setFileExists(isFileExists);

            System.out.println("File exists: " + isFileExists);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
