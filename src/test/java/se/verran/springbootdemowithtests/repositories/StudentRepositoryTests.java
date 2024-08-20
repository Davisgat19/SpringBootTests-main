package se.verran.springbootdemowithtests.repositories;

import se.verran.springbootdemowithtests.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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
}
