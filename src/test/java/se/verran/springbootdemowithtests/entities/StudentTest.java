package se.verran.springbootdemowithtests.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testDefaultConstructor() {
        Student student = new Student();
        assertNull(student.getFirstName());
        assertNull(student.getLastName());
        assertNull(student.getEmail());
        assertNull(student.getBirthDate());
        assertEquals(0, student.getId());
        assertNull(student.getJavaProgrammingGrade());
    }

    @Test
    void testParameterizedConstructor() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Student student = new Student("John", "Doe", birthDate, "john@example.com");

        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals(birthDate, student.getBirthDate());
        assertEquals("john@example.com", student.getEmail());
        assertEquals(0, student.getId()); // ID is not set in constructor
        assertNull(student.getJavaProgrammingGrade());
    }

    @Test
    void testSettersAndGetters() {
        LocalDate birthDate = LocalDate.of(1999, 2, 2);
        Student student = new Student();

        student.setId(1);
        student.setFirstName("Jane");
        student.setLastName("Doe");
        student.setBirthDate(birthDate);
        student.setEmail("jane@example.com");
        student.setJavaProgrammingGrade(4.5);

        assertEquals(1, student.getId());
        assertEquals("Jane", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals(birthDate, student.getBirthDate());
        assertEquals("jane@example.com", student.getEmail());
        assertEquals(4.5, student.getJavaProgrammingGrade());
    }

    @Test
    void testGetAge() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Student student = new Student();
        student.setBirthDate(birthDate);

        int expectedAge = LocalDate.now().getYear() - 2000;
        assertEquals(expectedAge, student.getAge());
    }

    @Test
    void testSetJavaProgrammingGrade() {
        Student student = new Student();
        student.setJavaProgrammingGrade(3.5);

        assertEquals(3.5, student.getJavaProgrammingGrade());
    }

    @Test
    void testSetJavaProgrammingGradeNull() {
        Student student = new Student();
        student.setJavaProgrammingGrade(null);

        assertNull(student.getJavaProgrammingGrade());
    }

    @Test
    void testSetJavaProgrammingGradeBoundary() {
        Student student = new Student();
        student.setJavaProgrammingGrade(5.0);

        assertEquals(5.0, student.getJavaProgrammingGrade());
    }
}
