package org.writer.csv.pipeline.steps;

import org.writer.csv.context.CsvContext;
import org.writer.csv.pipeline.PipelineStep;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WriteDataToCsvFileStep implements PipelineStep<CsvContext> {
    @Override
    public void execute(CsvContext csvContext) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                csvContext.getPath(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            if (!csvContext.isFileExists()) {
                writeCsvRow(writer, csvContext.getHeader());
            }
            for (Object element : csvContext.getData()) {
                String csvRow = joinFieldsToCsvRow(element, csvContext.getFields());
                writeCsvRow(writer, csvRow);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Joins field values into a  CSV string
     *
     * @param objectToRow the object whose data we will connect
     * @param fields      fields of the object to be serialized
     * @return joined row for CSV
     */
    private String joinFieldsToCsvRow(Object objectToRow, Field[] fields) {
        return Arrays.stream(fields)
                .map(field -> {
                    try {
                        Object value = field.get(objectToRow);
                        return escapeCsv(value == null ? "" : value.toString());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Access was not permitted ", e);
                    }
                })
                .collect(Collectors.joining(","));

    }

    /**
     * Writes a single CSV row to the given writer.
     *
     * <p>The method appends the row followed by a line separator.</p>
     *
     * @param writer the {@link BufferedWriter} used to write data to the CSV file
     * @param csvRow the CSV-formatted row to write
     * @throws RuntimeException if an I/O error occurs while writing the row
     */
    private void writeCsvRow(BufferedWriter writer, String csvRow) {
        try {
            writer.write(csvRow);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Escapes a value according to CSV format rules.
     *
     * <p>If the value contains a comma, double quote, or line break,
     * it is wrapped in double quotes and internal quotes are escaped
     * by doubling them.</p>
     *
     * @param value the raw value to escape
     * @return a CSV-safe representation of the value
     */
    private String escapeCsv(String value) {

        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
