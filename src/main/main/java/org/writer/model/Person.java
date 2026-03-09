package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Person {

    private String firstName;

    private String lastName;

    private int dayOfBirth;

    private Months monthOfBirth;

    private int yearOfBirth;



    public static List<Person>  getTestList(){
        return List.of(
                Person.builder()
                        .firstName("Alex")
                        .lastName("Ivanov")
                        .dayOfBirth(12)
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(1995)
                        .build(),

                Person.builder()
                        .firstName("Maria")
                        .lastName("Petrova")
                        .dayOfBirth(3)
                        .monthOfBirth(Months.FEBRUARY)
                        .yearOfBirth(1998)
                        .build(),

                Person.builder()
                        .firstName("John")
                        .lastName("Smith")
                        .dayOfBirth(21)
                        .monthOfBirth(Months.MARCH)
                        .yearOfBirth(1990)
                        .build(),

                Person.builder()
                        .firstName("Elena")
                        .lastName("Popescu")
                        .dayOfBirth(9)
                        .monthOfBirth(Months.APRIL)
                        .yearOfBirth(1997)
                        .build(),

                Person.builder()
                        .firstName("Victor")
                        .lastName("Radu")
                        .dayOfBirth(30)
                        .monthOfBirth(Months.MAY)
                        .yearOfBirth(1993)
                        .build(),

                Person.builder()
                        .firstName("Anna")
                        .lastName("Kowalski")
                        .dayOfBirth(14)
                        .monthOfBirth(Months.JUNE)
                        .yearOfBirth(2000)
                        .build(),

                Person.builder()
                        .firstName("Daniel")
                        .lastName("Novak")
                        .dayOfBirth(7)
                        .monthOfBirth(Months.JULY)
                        .yearOfBirth(1992)
                        .build(),

                Person.builder()
                        .firstName("Sofia")
                        .lastName("Marin")
                        .dayOfBirth(18)
                        .monthOfBirth(Months.AUGUST)
                        .yearOfBirth(1996)
                        .build(),

                Person.builder()
                        .firstName("Mihai")
                        .lastName("Dumitru")
                        .dayOfBirth(25)
                        .monthOfBirth(Months.SEPTEMBER)
                        .yearOfBirth(1994)
                        .build(),

                Person.builder()
                        .firstName("Laura")
                        .lastName("Bianchi")
                        .dayOfBirth(2)
                        .monthOfBirth(Months.OCTOBER)
                        .yearOfBirth(1999)
                        .build()
        );

    }

}
