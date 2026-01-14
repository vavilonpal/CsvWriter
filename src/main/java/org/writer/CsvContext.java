package org.writer;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

@Getter
@Setter
public class CsvContext {
    private List<?> data;
    private Path path;
    private boolean isFileExists;
    private Class<?> clazz;
    private Field[] fields;
    private String header;

    public CsvContext(List<?> data, String fileName) {
        this.data = data;
        this.path = Path.of(fileName);

    }


}
