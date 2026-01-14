package org.writer.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.csv.testdata.StudentTestData;
import org.writer.exception.ArrayIsEmptyException;
import org.writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvFileWriterTest {

    private final CsvFileWriter writer = new CsvFileWriter();

    @TempDir
    Path tempDir;

    private Student alex;
    private Student maria;
    private List<Student> data;

    @BeforeEach
    void setUp() {
        this.alex = StudentTestData.alex();
        this.maria = StudentTestData.maria();
        this.data = List.of(alex, maria);
    }

    @Test
    void shouldWriteCsvWithHeaderAndRows() throws IOException {
        Path file = tempDir.resolve("students.csv");

        writer.writeToFile(data, file.toString());

        List<String> lines = Files.readAllLines(file);

        assertEquals(3, lines.size());

        // порядок полей алфавитный: name, score
        assertEquals("name,score", lines.get(0));

        assertEquals("Alex,\"[A, B, A]\"", lines.get(1));
        assertEquals("Maria,\"[A, A, A]\"", lines.get(2));
    }

    @Test
    void shouldAppendDataWithoutDuplicatingHeader() throws IOException {
        Path file = tempDir.resolve("students.csv");

        writer.writeToFile(List.of(alex), file.toString());
        writer.writeToFile(List.of(maria), file.toString());

        List<String> lines = Files.readAllLines(file);

        assertEquals(3, lines.size());
        assertEquals("name,score", lines.get(0));
        assertEquals("Alex,\"[A, B, A]\"", lines.get(1));
        assertEquals("Maria,\"[A, A, A]\"", lines.get(2));
    }

    @Test
    void shouldEscapeCsvValues() throws IOException {
        Path file = tempDir.resolve("students.csv");

        Student withComma = Student.builder()
                .name("Alex, Jr.")
                .score(List.of("A", "B"))
                .build();

        writer.writeToFile(List.of(withComma), file.toString());

        List<String> lines = Files.readAllLines(file);

        assertEquals("name,score", lines.get(0));
        assertEquals("\"Alex, Jr.\",\"[A, B]\"", lines.get(1));
    }

    @Test
    void shouldThrowExceptionWhenDataIsEmpty() {
        Path file = tempDir.resolve("students.csv");

        assertThrows(
                ArrayIsEmptyException.class,
                () -> writer.writeToFile(List.of(), file.toString())
        );
    }

    @Test
    void shouldThrowExceptionWhenHeaderDoesNotMatch() throws IOException {
        Path file = tempDir.resolve("students.csv");

        //CSV с неправильным заголовком
        Files.write(file, List.of("wrong,header"));

        assertThrows(
                IllegalStateException.class,
                () -> writer.writeToFile(data, file.toString())
        );
    }
}
