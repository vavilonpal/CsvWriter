package org.writer;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvFileWriter implements Writable {

    /*
     * todo: дописать метод и отрефакторить в цепочку обязанностей
     * */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data.isEmpty()) {
            throw new ArrayIsEmptyException("Fill array with data!");
        }

        Path path = Path.of(fileName);
        boolean fileExists;

        Class<?> dataTypeClass = data.get(0).getClass();

        String header = Arrays.stream(dataTypeClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.joining(","));

        try {
            fileExists = Files.exists(path) && Files.size(path) > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (fileExists) {
            validateHeader(path, header);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            if (!fileExists) {
                writer.write(header);
                writer.newLine();
            }
            data.stream().map(element -> {
                        Class<?> clazz = element.getClass();
                        return Arrays.stream(clazz.getDeclaredFields())
                                .map(field -> {
                                    try {
                                        field.setAccessible(true);
                                        return escapeCsv(field.get(element).toString());
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).collect(Collectors.joining(","));
                    }
            ).forEach(csvRow -> {
                try {
                    writer.write(csvRow);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

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
