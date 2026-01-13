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

public class CsvFileWriter implements Writable {

    /*
     * todo: дописать метод и отрефакторить в цепочку обязанностей
     * */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        validateData(data);

        Path path = Path.of(fileName);
        Class<?> clazz = data.get(0).getClass();

        Field[] fields = getAccessibleFields(clazz);
        String header = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.joining(","));

        boolean fileExists = isFileExists(path);
        if (fileExists) {
            validateHeader(path, header);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            if (!fileExists) {
                writeCsvRow(writer, header);
            }
            for (Object element : data) {
                String csvRow = joinFieldsToCsvRow(element, fields);
                writeCsvRow(writer, csvRow);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String joinFieldsToCsvRow(Object objectToRow, Field[]fields){
        return Arrays.stream(fields)
                .map(field -> {
                    try {
                        Object value = field.get(objectToRow);
                        return escapeCsv(value == null ? "" : value.toString());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(","));

    }
    private Field[] getAccessibleFields(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }
    private void writeCsvRow(BufferedWriter writer, String csvRow) {
        try {
            writer.write(csvRow);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String escapeCsv(String value) {

        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    private boolean isFileExists(Path path) {
        try {
            return Files.exists(path) && Files.size(path) > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateData(List<?> data) {
        if (data.isEmpty()) {
            throw new ArrayIsEmptyException("Fill array with data!");
        }
    }

    private void validateHeader(Path path, String existingHeader) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            boolean isHeaderEquals = header.equals(existingHeader);

            if (!isHeaderEquals) {
                throw new IllegalStateException(
                        "CSV header mismatch. Expected: "
                                + header + ", actual: " + existingHeader
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
