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
import training360.mentortools.command.CreateTrainingClassCommand;
import training360.mentortools.command.UpdateTrainingClassCommand;
import training360.mentortools.dto.TrainingClassDto;
import training360.mentortools.entity.InternalDates;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrainingClassControllerRestIT {

    @Autowired
    TestRestTemplate template;

    @Test
    @Sql(statements = "delete from training_classes")
    void testSaveThenListAll() {
        TrainingClassDto java_backend = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(null, null)),
                TrainingClassDto.class);
        TrainingClassDto frontend_fejleszto = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Frontend Fejlesztő", new InternalDates(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 3, 12))),
                TrainingClassDto.class);
        TrainingClassDto szoftvertesztelo = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Szoftvertesztelő", new InternalDates(LocalDate.of(2020, 10, 8), null)),
                TrainingClassDto.class);

        List<TrainingClassDto> trainingClasses = template.exchange("/api/trainingclasses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TrainingClassDto>>() {
                })
                .getBody();

        assertThat(trainingClasses)
                .hasSize(3)
                .extracting(TrainingClassDto::getName)
                .containsExactly("Java Backend Fejlesztő", "Frontend Fejlesztő", "Szoftvertesztelő");
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testFindTrainingClassById() {
        TrainingClassDto java_backend = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(null, null)),
                TrainingClassDto.class);
        TrainingClassDto szoftvertesztelo = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Szoftvertesztelő", new InternalDates(LocalDate.of(2020, 10, 8), null)),
                TrainingClassDto.class);
        TrainingClassDto frontend_fejleszto = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Frontend Fejlesztő", new InternalDates(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 3, 12))),
                TrainingClassDto.class);

        TrainingClassDto trainingClass = template.exchange("/api/trainingclasses/" + frontend_fejleszto.getId(),
                HttpMethod.GET,
                null,
                TrainingClassDto.class)
                .getBody();

        assertEquals("Frontend Fejlesztő", trainingClass.getName());
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testfindByInvalidId() {
        TrainingClassDto java_backend = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(null, null)),
                TrainingClassDto.class);
        TrainingClassDto szoftvertesztelo = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Szoftvertesztelő", new InternalDates(LocalDate.of(2020, 10, 8), null)),
                TrainingClassDto.class);
        TrainingClassDto frontend_fejleszto = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Frontend Fejlesztő", new InternalDates(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 3, 12))),
                TrainingClassDto.class);

        Problem invalidId = template.exchange("/api/trainingclasses/0",
                HttpMethod.GET,
                null,
                Problem.class)
                .getBody();

        assertEquals(404, invalidId.getStatus().getStatusCode());
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testSaveInvalidTrainingClass() {
        Problem blankNameProblem = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("", new InternalDates(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2))),
                Problem.class);
        String name = "a".repeat(201);
        Problem tooLongNameProblem = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand(name, new InternalDates(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2))),
                Problem.class);
        Problem invalidFinishDate = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(LocalDate.of(2020, 1, 1), LocalDate.of(1920, 1, 1))),
                Problem.class);
        Problem invalidFinishDateWithoutStartingDate = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Szoftvertesztelő", new InternalDates(null, LocalDate.of(2020, 1, 1))),
                Problem.class);

        assertEquals(400, blankNameProblem.getStatus().getStatusCode());
        assertEquals(400, tooLongNameProblem.getStatus().getStatusCode());
        assertEquals(400, invalidFinishDate.getStatus().getStatusCode());
        assertEquals(400, invalidFinishDateWithoutStartingDate.getStatus().getStatusCode());
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testFindByPartOfName() {
        TrainingClassDto java_backend_fejleszto = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(null, null)),
                TrainingClassDto.class);
        TrainingClassDto frontend_fejleszto = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Frontend Fejlesztő", new InternalDates(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 3, 12))),
                TrainingClassDto.class);
        TrainingClassDto szoftvertesztelo = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Szoftvertesztelő", new InternalDates(LocalDate.of(2020, 10, 8), null)),
                TrainingClassDto.class);

        List<TrainingClassDto> filtered = template.exchange("/api/trainingclasses?partofname=fejlesztő",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TrainingClassDto>>() {
                })
                .getBody();

        assertThat(filtered)
                .hasSize(2)
                .extracting(TrainingClassDto::getName)
                .containsExactly("Java Backend Fejlesztő", "Frontend Fejlesztő");
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testUpdateById() {
        TrainingClassDto trainingClassDto = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(null, null)),
                TrainingClassDto.class);

        template.put("/api/trainingclasses/" + trainingClassDto.getId(),
                new UpdateTrainingClassCommand("Szoftvertesztelő",
                        new InternalDates(LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 5, 1))));

        TrainingClassDto updated = template.exchange("/api/trainingclasses/" + trainingClassDto.getId(),
                HttpMethod.GET,
                null,
                TrainingClassDto.class)
                .getBody();

        assertThat(updated)
                .extracting(TrainingClassDto::getName,
                        tc -> tc.getInternalDates().getStartDate().toString(),
                        tc -> tc.getInternalDates().getFinishDate().toString())
                .containsExactly("Szoftvertesztelő", "2020-01-01", "2020-05-01");
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testUpdateByInvalidId() {
        Problem invalidIdProblem = template.exchange("/api/trainingclasses/0",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateTrainingClassCommand("Szoftvertesztelő",
                        new InternalDates(LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 5, 1)))),
                Problem.class).getBody();

        assertEquals(404, invalidIdProblem.getStatus().getStatusCode());
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testUpdateByIdWithInvalidTrainingClassData() {
        long java_backend_id = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend", new InternalDates(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2))),
                TrainingClassDto.class)
                .getId();

        Problem blankNameProblem = template.exchange("/api/trainingclasses/" + java_backend_id,
                HttpMethod.PUT,
                new HttpEntity<>(new CreateTrainingClassCommand("", new InternalDates(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2)))),
                Problem.class).getBody();
        String name = "a".repeat(201);
        Problem tooLongNameProblem = template.exchange("/api/trainingclasses/" + java_backend_id,
                HttpMethod.PUT,
                new HttpEntity<>(new CreateTrainingClassCommand(name, new InternalDates(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2)))),
                Problem.class).getBody();
        Problem invalidFinishDate = template.exchange("/api/trainingclasses/" + java_backend_id,
                HttpMethod.PUT,
                new HttpEntity<>(new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(LocalDate.of(2020, 1, 1), LocalDate.of(1920, 1, 1)))),
                Problem.class).getBody();
        Problem invalidFinishDateWithoutStartingDate = template.exchange("/api/trainingclasses/" + java_backend_id,
                HttpMethod.PUT,
                new HttpEntity<>(new CreateTrainingClassCommand("Szoftvertesztelő", new InternalDates(null, LocalDate.of(2020, 1, 1)))),
                Problem.class).getBody();

        assertEquals(400, blankNameProblem.getStatus().getStatusCode());
        assertEquals(400, tooLongNameProblem.getStatus().getStatusCode());
        assertEquals(400, invalidFinishDate.getStatus().getStatusCode());
        assertEquals(400, invalidFinishDateWithoutStartingDate.getStatus().getStatusCode());
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testDeleteById() {
        long java_backend_id = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend", new InternalDates(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2))),
                TrainingClassDto.class)
                .getId();

        template.delete("/api/trainingclasses/" + java_backend_id);

        List<TrainingClassDto> trainingClassDtos = template.exchange("/api/trainingclasses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TrainingClassDto>>() {
                })
                .getBody();

        assertThat(trainingClassDtos)
                .hasSize(0);
    }

    @Test
    @Sql(statements = "delete from training_classes")
    void testDeleteByInvalidId() {
        Problem invalidIdProblem = template.exchange("/api/trainingclasses/0",
                HttpMethod.DELETE,
                null,
                Problem.class).getBody();

        assertEquals(404, invalidIdProblem.getStatus().getStatusCode());
    }
}
