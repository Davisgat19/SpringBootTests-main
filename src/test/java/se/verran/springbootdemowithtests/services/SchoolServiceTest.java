package se.verran.springbootdemowithtests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.verran.springbootdemowithtests.entities.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private SchoolService schoolService;

    @Test
    void testNumberOfStudentsPerGroupWithInvalidGroupCount() {
        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(1);
        assertThat(result).isEqualTo("There should be at least two groups");
    }

    @Test
    void testNumberOfStudentsPerGroupWithTooManyGroups() {
        when(studentService.getAllStudents()).thenReturn(List.of(new Student("John", "Doe", LocalDate.of(1998, 1, 1), "john@example.com")));
        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(5);
        assertThat(result).isEqualTo("Not able to divide 1 students into 5 groups");
    }

    @Test
    void testNumberOfStudentsPerGroupWithValidGroups() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("John", "Doe", LocalDate.of(1998, 1, 1), "john@example.com"),
                new Student("Jane", "Doe", LocalDate.of(1999, 2, 2), "jane@example.com"),
                new Student("Davis", "Her", LocalDate.of(2000, 3, 3), "davis@example.com")));

        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(3);

        assertThat(result).isEqualTo("Not able to manage 3 groups with 3 students");
    }



    @Test
    void testNumberOfStudentsPerGroupWithHangingStudents() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("John", "Doe", LocalDate.of(1998, 1, 1), "john@example.com"),
                new Student("Jane", "Doe", LocalDate.of(1999, 2, 2), "jane@example.com")));
        String result = schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(2);
        assertThat(result).isEqualTo("Not able to manage 2 groups with 2 students");
    }

    @Test
    void testNumberOfGroupsWhenDividedIntoGroupsOfInvalidSize() {
        String result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(1);
        assertThat(result).isEqualTo("Size of group should be at least 2");
    }

    @Test
    void testNumberOfGroupsWhenDividedIntoGroupsOfValid() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("John", "Doe", LocalDate.of(1998, 1, 1), "john@example.com"),
                new Student("Jane", "Doe", LocalDate.of(1999, 2, 2), "jane@example.com")));
        String result = schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2);
        assertThat(result).isEqualTo("Not able to manage groups of 2 with only 2 students");
    }


    @Test
    void testCalculateAverageGradeWithNoStudents() {
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        assertThatThrownBy(() -> schoolService.calculateAverageGrade())
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void testCalculateAverageGrade() {
        Student student1 = new Student("John", "Doe", LocalDate.of(1998, 1, 1), "john@example.com");
        student1.setJavaProgrammingGrade(4.0);
        Student student2 = new Student("Jane", "Doe", LocalDate.of(1999, 2, 2), "jane@example.com");
        student2.setJavaProgrammingGrade(3.5);
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));
        String result = schoolService.calculateAverageGrade();
        assertThat(result).isEqualTo("Average grade is 3,8");  // Expecting comma
    }

    @Test
    void testGetTopScoringStudentsWithEmptyList() {
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        assertThatThrownBy(() -> schoolService.getTopScoringStudents())
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetTopScoringStudents() {
        Student student1 = new Student("John", "Doe", LocalDate.of(1998, 1, 1), "john@example.com");
        student1.setJavaProgrammingGrade(4.0);
        Student student2 = new Student("Jane", "Doe", LocalDate.of(1999, 2, 2), "jane@example.com");
        student2.setJavaProgrammingGrade(3.5);
        Student student3 = new Student("Davis", "Her", LocalDate.of(2000, 3, 3), "davis@example.com");
        student3.setJavaProgrammingGrade(3.7);
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2, student3));
        List<Student> result = schoolService.getTopScoringStudents();
        assertThat(result).containsExactly(student1);
    }


}
