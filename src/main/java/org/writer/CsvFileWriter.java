package org.writer;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Writes Java objects to a CSV file using reflection.
 *
 * <p>The writer automatically generates a CSV header based on
 * the object's fields and ensures header consistency when appending data.</p>
 *
 * <p>This class is not thread-safe.</p>
 */
public class CsvFileWriter implements Writable {
    /***
     * Writes a list of objects to a CSV file
     *
     * @param data list of objects to be written; must not be empty
     * @param fileName name of file where data will be written
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        CsvContext csvContext = new CsvContext(data, fileName);

        validateData(csvContext);
        resolveMetadata(csvContext);
        checkFile(csvContext);


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

    /***
     *  Joins field values into a  CSV string
     * @param objectToRow the object whose data we will connect
     * @param fields the object whose data we will connect
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
     *
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

    /**
     * Checks whether the target CSV file exists and validates its header if present.
     *
     * <p>If the file exists and is not empty, the method verifies that the existing
     * CSV header matches the header generated from the current data structure.</p>
     *
     * @param context the CSV processing context containing file path and header
     *
     * @throws IllegalStateException if the existing CSV header does not match
     * @throws RuntimeException if an I/O error occurs while accessing the file
     */
    private void checkFile(CsvContext context) {
        try {
            boolean isFileExists = Files.exists(context.getPath()) && Files.size(context.getPath()) > 0;
            context.setFileExists(isFileExists);
            if (isFileExists){
                validateHeader(context.getPath(), context.getHeader());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates that the CSV context contains data to be written.
     *
     * @param context the CSV processing context
     *
     * @throws ArrayIsEmptyException if the data list is empty
     */
    private void validateData(CsvContext context) {
        if (context.getData().isEmpty()) {
            throw new ArrayIsEmptyException("Fill array with data!");
        }
    }

    /**
     * Validates that the header of an existing CSV file matches the expected header.
     *
     * @param path the path to the existing CSV file
     * @param dataHeader the expected CSV header generated from the data structure
     *
     * @throws IllegalStateException if the headers do not match
     * @throws RuntimeException if an I/O error occurs while reading the file
     */

    private void validateHeader(Path path, String dataHeader) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            boolean isHeaderEquals = header.equals(dataHeader);

            if (!isHeaderEquals) {
                throw new IllegalStateException(
                        "CSV header mismatch. Expected: "
                                + header + ", actual: " + dataHeader
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Resolves reflection metadata required for CSV serialization.
     *
     * <p>The method determines the class of the data elements, extracts
     * declared fields, sorts them in a deterministic order, and prepares
     * them for access. It also generates a CSV header based on the field names.</p>
     *
     * @param context the CSV processing context to populate with metadata
     */
    private void resolveMetadata(CsvContext context) {
        context.clazz = context.data.get(0).getClass();

        Field[] fields = context.clazz.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        for (Field f : fields) {
            f.setAccessible(true);
        }

        context.fields = fields;
        context.header = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }
}
