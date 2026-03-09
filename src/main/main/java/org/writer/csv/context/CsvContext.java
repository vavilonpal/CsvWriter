package org.writer.csv.context;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

@Getter
@Setter
//@Builder
public class CsvContext implements ContextInterface {
    private List<?> data;
    private Path path;
    private boolean isFileExists = false;
    private Class<?> clazz;
    private Field[] fields;
    private String header;

    public CsvContext(List<?> data, String fileName) {
        this.data = data;
        this.path = Path.of(fileName);

    }


}
