package se.verran.springbootdemowithtests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testAddStudentSuccessfully() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");

        when(studentRepository.existsStudentByEmail("john.doe@example.com")).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);

        Student savedStudent = studentService.addStudent(student);

        assertThat(savedStudent).isEqualTo(student);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testAddStudentWithExistingEmail() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");

        when(studentRepository.existsStudentByEmail("john.doe@example.com")).thenReturn(true);

        assertThatThrownBy(() -> studentService.addStudent(student))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email john.doe@example.com already exists");
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        Student student2 = new Student("Jane", "Doe", LocalDate.of(2001, 2, 2), "jane.doe@example.com");

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        assertThat(studentService.getAllStudents()).containsExactly(student1, student2);
    }

    @Test
    void testDeleteStudentSuccessfully() {
        when(studentRepository.existsById(1)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1);

        studentService.deleteStudent(1);

        verify(studentRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteStudentNotFound() {
        when(studentRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Could not find and delete student by id 1");
    }

    @Test
    void testUpdateStudentSuccessfully() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        student.setId(1);

        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.save(student)).thenReturn(student);

        Student updatedStudent = studentService.updateStudent(student);

        assertThat(updatedStudent).isEqualTo(student);
    }

    @Test
    void testUpdateStudentNotFound() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        student.setId(1);

        when(studentRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> studentService.updateStudent(student))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Could not find and update student by id 1");
    }

    @Test
    void testGetStudentByIdFound() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        student.setId(1);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        assertThat(studentService.getStudentById(1)).isEqualTo(student);
    }

    @Test
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(1))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Could not find student by id 1");
    }

    @Test
    void testSetGradeForStudentByIdSuccessfully() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        student.setId(1);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);

        Student updatedStudent = studentService.setGradeForStudentById(1, "4.0");

        assertThat(updatedStudent.getJavaProgrammingGrade()).isEqualTo(4.0);
    }

    @Test
    void testSetGradeForStudentByIdInvalidGrade() {
        assertThatThrownBy(() -> studentService.setGradeForStudentById(1, "6.0"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Valid grades are 0.0 - 5.0");
    }

    @Test
    void testSetGradeForStudentByIdInvalidFormat() {
        assertThatThrownBy(() -> studentService.setGradeForStudentById(1, "invalid"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Valid grades are 0.0 - 5.0");
    }

    @Test
    void testSetGradeForStudentByIdStudentNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.setGradeForStudentById(1, "3.0"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Could not find and update grades for student by id 1");
    }
}
