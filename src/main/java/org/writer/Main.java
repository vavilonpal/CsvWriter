package org.writer;

import org.writer.csv.CsvFileWriter;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String peopleFileName = "persons.csv";
        String studentshipName = "students.csv";

        List<Person> personList = Person.getTestList();
        List<Student> studentList = Student.getTestList();


        CsvFileWriter csvFileWriter = new CsvFileWriter();

        csvFileWriter.writeToFile(studentList, peopleFileName);
        csvFileWriter.writeToFile(studentList, studentshipName);


    }

    {

    }
}