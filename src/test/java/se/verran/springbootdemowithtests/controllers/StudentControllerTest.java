package se.verran.springbootdemowithtests.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.services.StudentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddStudent() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        when(studentService.addStudent(student)).thenReturn(student);

        ResponseEntity<Student> response = studentController.addStudent(student);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).addStudent(student);
    }

    @Test
    void testGetStudentById() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        when(studentService.getStudentById(1)).thenReturn(student);

        ResponseEntity<Student> response = studentController.getStudentById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).getStudentById(1);
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        Student student2 = new Student("Jane", "Doe", LocalDate.of(2001, 2, 2), "jane.doe@example.com");
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        ResponseEntity<List<Student>> response = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(student1, response.getBody().get(0));
        assertEquals(student2, response.getBody().get(1));
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testUpdateStudentById() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        when(studentService.updateStudent(student)).thenReturn(student);

        ResponseEntity<Student> response = studentController.updateStudentById(student);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService, times(1)).updateStudent(student);
    }

    @Test
    void testSetGradeForStudentById() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        student.setJavaProgrammingGrade(4.0);
        when(studentService.setGradeForStudentById(1, "4.0")).thenReturn(student);

        ResponseEntity<Student> response = studentController.setGradeForStudentById(1, "4.0");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4.0, Objects.requireNonNull(response.getBody()).getJavaProgrammingGrade());
        verify(studentService, times(1)).setGradeForStudentById(1, "4.0");
    }

    @Test
    void testDeleteStudentById() {
        ResponseEntity<String> response = studentController.deleteStudentById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Student by id 1 deleted", response.getBody());
        verify(studentService, times(1)).deleteStudent(1);
    }
}
