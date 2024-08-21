package se.verran.springbootdemowithtests.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.verran.springbootdemowithtests.entities.Student;
import se.verran.springbootdemowithtests.services.SchoolService;
import se.verran.springbootdemowithtests.services.StudentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SchoolControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private SchoolService schoolService;

    @InjectMocks
    private SchoolController schoolController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCountStudents() {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(
                new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com"),
                new Student("Jane", "Doe", LocalDate.of(2001, 2, 2), "jane.doe@example.com")
        ));

        ResponseEntity<Integer> response = schoolController.countStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody());
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testGetNumberOfGroupsWhenNStudentsPerGroup() {
        when(schoolService.numberOfGroupsWhenDividedIntoGroupsOf(2)).thenReturn("2 students per group is possible, there will be 1 groups");

        ResponseEntity<String> response = schoolController.getNumberOfGroupsWhenNStudentsPerGroup(2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("2 students per group is possible, there will be 1 groups", response.getBody());
        verify(schoolService, times(1)).numberOfGroupsWhenDividedIntoGroupsOf(2);
    }

    @Test
    void testGetStudentsPerGroup() {
        when(schoolService.numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(3)).thenReturn("3 groups could be formed with 1 students per group");

        ResponseEntity<String> response = schoolController.getStudentsPerGroup(3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("3 groups could be formed with 1 students per group", response.getBody());
        verify(schoolService, times(1)).numberOfStudentsPerGroupWhenDivideIntoNumberOfGroups(3);
    }

    @Test
    void testGetAverageGrade() {
        when(schoolService.calculateAverageGrade()).thenReturn("Average grade is 3.5");

        ResponseEntity<String> response = schoolController.getAverageGrade();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Average grade is 3.5", response.getBody());
        verify(schoolService, times(1)).calculateAverageGrade();
    }

    @Test
    void testGetTopScoringStudents() {
        Student student1 = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        student1.setJavaProgrammingGrade(4.5);
        Student student2 = new Student("Jane", "Doe", LocalDate.of(2001, 2, 2), "jane.doe@example.com");
        student2.setJavaProgrammingGrade(3.5);

        when(schoolService.getTopScoringStudents()).thenReturn(Arrays.asList(student1, student2));

        ResponseEntity<List<Student>> response = schoolController.getTopScoringStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(schoolService, times(1)).getTopScoringStudents();
    }
}
