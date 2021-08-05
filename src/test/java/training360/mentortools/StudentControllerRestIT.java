package training360.mentortools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import training360.mentortools.command.student.CreateStudentCommand;
import training360.mentortools.command.student.UpdateStudentCommand;
import training360.mentortools.dto.StudentDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerRestIT {

    @Autowired
    TestRestTemplate template;

    @Test
    @Sql(statements = "delete from students")
    void testSaveThenListAll() {
        long john_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", "john_doe", ""),
                StudentDto.class).getId();
        long jane_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("Jane Doe", "janedoe@domain.com", "jane_doe", ""),
                StudentDto.class).getId();
        long jack_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("Jack Doe", "jackdoe@domain.com", "jack_doe", ""),
                StudentDto.class).getId();

        List<StudentDto> students = template.exchange("/api/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StudentDto>>() {
                })
                .getBody();

        assertThat(students)
                .hasSize(3)
                .extracting(StudentDto::getName)
                .containsExactly("John Doe", "Jane Doe", "Jack Doe");
    }

    @Test
    @Sql(statements = "delete from students")
    void testFindById() {
        long john_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", "john_doe", ""),
                StudentDto.class).getId();
        long jane_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("Jane Doe", "janedoe@domain.com", "jane_doe", ""),
                StudentDto.class).getId();
        long jack_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("Jack Doe", "jackdoe@domain.com", "jack_doe", ""),
                StudentDto.class).getId();

        StudentDto student = template.exchange("/api/students/" + jane_doe_id,
                HttpMethod.GET,
                null,
                StudentDto.class)
                .getBody();

        assertEquals("Jane Doe", student.getName());
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testfindByInvalidId() {
        Problem invalidId = template.exchange("/api/students/0",
                HttpMethod.GET,
                null,
                Problem.class)
                .getBody();

        assertEquals(404, invalidId.getStatus().getStatusCode());
    }

    @Test
    @Sql(statements = "delete from students")
    void testSaveInvalidData() {
        String name = "a".repeat(256);

        Problem blankNameProblem = template.postForObject("/api/students",
                new CreateStudentCommand("", "johndoe@domain.com", "john_doe", ""),
                Problem.class);
        Problem tooLongNameProblem = template.postForObject("/api/students",
                new CreateStudentCommand(name, "johndoe@domain.com", "john_doe", ""),
                Problem.class);

        Problem invalidEmailProblem = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoedomain.com", "john_doe", ""),
                Problem.class);
        Problem tooLongEmailProblem = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", name + "@domain.com", "john_doe", ""),
                Problem.class);

        Problem tooLongGithubProblem = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", name, ""),
                Problem.class);

        assertEquals(400, blankNameProblem.getStatus().getStatusCode());
        assertEquals(400, tooLongNameProblem.getStatus().getStatusCode());
        assertEquals(400, invalidEmailProblem.getStatus().getStatusCode());
        assertEquals(400, tooLongEmailProblem.getStatus().getStatusCode());
        assertEquals(400, tooLongGithubProblem.getStatus().getStatusCode());
    }

    @Test
    @Sql(statements = "delete from students")
    void testFindByPartOfName() {
        long john_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", "john_doe", ""),
                StudentDto.class).getId();
        long jane_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("Jane Doe", "janedoe@domain.com", "jane_doe", ""),
                StudentDto.class).getId();
        long jack_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("Jack John Doe", "jackdoe@domain.com", "jack_doe", ""),
                StudentDto.class).getId();

        List<StudentDto> filtered = template.exchange("/api/students?partofname=john",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StudentDto>>() {
                })
                .getBody();

        assertThat(filtered)
                .hasSize(2)
                .extracting(StudentDto::getName)
                .containsExactly("John Doe", "Jack John Doe");
    }

    @Test
    @Sql(statements = "delete from students")
    void testUpdateById() {
        long john_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", "john_doe", ""),
                StudentDto.class).getId();

        template.put("/api/students/" + john_doe_id,
                new UpdateStudentCommand("Jane Doe", "janedoe@domain.com", "jane_doe", "description"));

        StudentDto updated = template.exchange("/api/students/" + john_doe_id,
                HttpMethod.GET,
                null,
                StudentDto.class)
                .getBody();

        assertThat(updated)
                .extracting(StudentDto::getName, StudentDto::getEmail, StudentDto::getGithubAccount, StudentDto::getDescription)
                .containsExactly("Jane Doe", "janedoe@domain.com", "jane_doe", "description");
    }

    @Test
    @Sql(statements = "delete from students")
    void testUpdateByInvalidId() {
        Problem invalidIdProblem = template.exchange("/api/students/0",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateStudentCommand("Jane Doe", "janedoe@domain.com", "jane_doe", "description")),
                Problem.class).getBody();

        assertEquals(404, invalidIdProblem.getStatus().getStatusCode());
    }

    @Test
    @Sql(statements = "delete from students")
    void testUpdateByIdWithInvalidData() {
        long john_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", "john_doe", ""),
                StudentDto.class).getId();

        String name = "a".repeat(256);

        Problem blankNameProblem = template.exchange("/api/students/" + john_doe_id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateStudentCommand("", "johndoe@domain.com", "john_doe", "")),
                Problem.class).getBody();
        Problem tooLongNameProblem = template.exchange("/api/students/" + john_doe_id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateStudentCommand(name, "johndoe@domain.com", "john_doe", "")),
                Problem.class).getBody();

        Problem invalidEmailProblem = template.exchange("/api/students/" + john_doe_id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateStudentCommand("John Doe", "johndoedomain.com", "john_doe", "")),
                Problem.class).getBody();
        Problem tooLongEmailProblem = template.exchange("/api/students/" + john_doe_id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateStudentCommand("John Doe", name + "@domain.com", "john_doe", "")),
                Problem.class).getBody();

        Problem tooLongGithubProblem = template.exchange("/api/students/" + john_doe_id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateStudentCommand("John Doe", "johndoe@domain.com", name, "")),
                Problem.class).getBody();

        assertEquals(400, blankNameProblem.getStatus().getStatusCode());
        assertEquals(400, tooLongNameProblem.getStatus().getStatusCode());
        assertEquals(400, invalidEmailProblem.getStatus().getStatusCode());
        assertEquals(400, tooLongEmailProblem.getStatus().getStatusCode());
        assertEquals(400, tooLongGithubProblem.getStatus().getStatusCode());
    }

    @Test
    @Sql(statements = "delete from students")
    void testDeleteById() {
        long john_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", "john_doe", ""),
                StudentDto.class).getId();

        template.delete("/api/students/" + john_doe_id);

        List<StudentDto> studentDtos = template.exchange("/api/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StudentDto>>() {
                })
                .getBody();

        assertThat(studentDtos)
                .hasSize(0);
    }

    @Test
    @Sql(statements = "delete from students")
    void testDeleteByInvalidId() {
        Problem invalidIdProblem = template.exchange("/api/students/0",
                HttpMethod.DELETE,
                null,
                Problem.class).getBody();

        assertEquals(404, invalidIdProblem.getStatus().getStatusCode());
    }
}
