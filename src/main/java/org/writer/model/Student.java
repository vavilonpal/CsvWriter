package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    private String name;

    private List<String> score;


    public static List<Student> getTestList(){
        return List.of(
                Student.builder()
                        .name("Alex")
                        .score(List.of("A", "B", "A"))
                        .build(),

                Student.builder()
                        .name("Maria")
                        .score(List.of("A", "A", "A"))
                        .build(),

                Student.builder()
                        .name("John")
                        .score(List.of("B", "C", "B"))
                        .build(),

                Student.builder()
                        .name("Elena")
                        .score(List.of("A", "B", "C"))
                        .build(),

                Student.builder()
                        .name("Victor")
                        .score(List.of("B", "B", "B"))
                        .build(),

                Student.builder()
                        .name("Anna")
                        .score(List.of("A", "C", "B"))
                        .build(),

                Student.builder()
                        .name("Daniel")
                        .score(List.of("C", "B", "A"))
                        .build(),

                Student.builder()
                        .name("Sofia")
                        .score(List.of("A", "A", "B"))
                        .build(),

                Student.builder()
                        .name("Mihai")
                        .score(List.of("B", "C", "C"))
                        .build(),

                Student.builder()
                        .name("Laura")
                        .score(List.of("A", "B", "A"))
                        .build()
        );

    }
}