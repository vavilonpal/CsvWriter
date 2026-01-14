package org.writer.csv.testdata;

import org.writer.model.Student;

import java.util.List;

public final class StudentTestData {

    private StudentTestData() {}

    public static Student alex() {
        return Student.builder()
                .name("Alex")
                .score(List.of("A", "B", "A"))
                .build();
    }

    public static Student maria() {
        return Student.builder()
                .name("Maria")
                .score(List.of("A", "A", "A"))
                .build();
    }

    public static Student withCommaInName() {
        return Student.builder()
                .name("John, Jr.")
                .score(List.of("A", "B"))
                .build();
    }

    public static List<Student> students() {
        return List.of(
                alex(),
                maria()
        );
    }
}

