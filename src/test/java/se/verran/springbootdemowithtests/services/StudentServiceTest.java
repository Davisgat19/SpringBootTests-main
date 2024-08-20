package se.verran.springbootdemowithtests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testAddStudentWithExistingEmailThrowsException() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Student student = new Student("John", "Doe", birthDate, "john.doe@example.com");
        when(studentRepository.existsStudentByEmail("john.doe@example.com")).thenReturn(true);

        assertThatThrownBy(() -> studentService.addStudent(student))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    @Test
    void testAddStudentSuccess() {
        // Given
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Student student = new Student("John", "Doe", birthDate, "john.doe@example.com");
        when(studentRepository.existsStudentByEmail("john.doe@example.com")).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);

        // When
        Student savedStudent = studentService.addStudent(student);

        // Then
        assertThat(savedStudent).isEqualTo(student);
    }

    @Test
    void testGetAllStudentsEmptyList() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<Student> students = studentService.getAllStudents();

        assertThat(students).isEmpty();
    }

    @Test
    void testValidGradeBoundaries() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Student student = new Student("John", "Doe", birthDate, "john.doe@example.com");
        student.setId(1);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);

        assertAll(
                () -> {
                    Student updatedStudentLow = studentService.setGradeForStudentById(1, "0.0");
                    assertThat(updatedStudentLow.getJavaProgrammingGrade()).isEqualTo(0.0);
                },
                () -> {
                    Student updatedStudentHigh = studentService.setGradeForStudentById(1, "5.0");
                    assertThat(updatedStudentHigh.getJavaProgrammingGrade()).isEqualTo(5.0);
                }
        );
    }

}
