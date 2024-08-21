package se.verran.springbootdemowithtests.repositories;

import se.verran.springbootdemowithtests.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class StudentRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testExistsStudentByEmail() {
        String email = "john.doe@example.com";
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        Student student = new Student("John", "Doe", birthDate, email);
        entityManager.persist(student);
        entityManager.flush();

        boolean exists = studentRepository.existsStudentByEmail(email);

        assertThat(exists).isTrue();

        boolean notExists = studentRepository.existsStudentByEmail("not.exists@example.com");

        assertThat(notExists).isFalse();
    }

    @Test
    void testExistsStudentByEmailTrue() {
        Student student = new Student("John", "Doe", LocalDate.of(2000, 1, 1), "john.doe@example.com");
        studentRepository.save(student);

        boolean exists = studentRepository.existsStudentByEmail("john.doe@example.com");

        assertTrue(exists);
    }

    @Test
    void testExistsStudentByEmailFalse() {
        Student student = new Student("Jane", "Doe", LocalDate.of(2001, 2, 2), "jane.doe@example.com");
        studentRepository.save(student);

        boolean exists = studentRepository.existsStudentByEmail("non.existent@example.com");

        assertFalse(exists);
    }


}
