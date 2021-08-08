package training360.mentortools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import training360.mentortools.command.registration.CreateRegistrationCommand;
import training360.mentortools.command.registration.UpdateRegistrationCommand;
import training360.mentortools.command.student.CreateStudentCommand;
import training360.mentortools.command.trainingclass.CreateTrainingClassCommand;
import training360.mentortools.dto.*;
import training360.mentortools.dto.registration.RegisteredStudentByTrainingClassDto;
import training360.mentortools.dto.registration.RegisteredTrainingClassByStudentDto;
import training360.mentortools.dto.registration.RegistrationDto;
import training360.mentortools.othertypes.InternalDates;
import training360.mentortools.othertypes.Status;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerRestIT {

    @Autowired
    TestRestTemplate template;

    private long java_backend_id;
    private long frontend_fejleszto_id;

    private long john_doe_id;
    private long jane_doe_id;

    private long jane_doe_frontend_fejleszto_id;
    private long john_doe_java_backend_id;
    private long john_doe_frontend_fejleszto_id;

    private void createSampleData() {
        java_backend_id = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(null, null)),
                TrainingClassDto.class).getId();
        frontend_fejleszto_id = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Frontend Fejlesztő", new InternalDates(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 3, 12))),
                TrainingClassDto.class).getId();

        john_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", "john_doe", ""),
                StudentDto.class).getId();
        jane_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("Jane Doe", "janedoe@domain.com", "jane_doe", ""),
                StudentDto.class).getId();

        jane_doe_frontend_fejleszto_id = template.postForObject("/api/trainingclasses/" + frontend_fejleszto_id + "/registrations",
                new CreateRegistrationCommand(jane_doe_id),
                RegistrationDto.class).getId();
        john_doe_java_backend_id = template.postForObject("/api/trainingclasses/" + java_backend_id + "/registrations",
                new CreateRegistrationCommand(john_doe_id),
                RegistrationDto.class).getId();
        john_doe_frontend_fejleszto_id = template.postForObject("/api/trainingclasses/" + frontend_fejleszto_id + "/registrations",
                new CreateRegistrationCommand(john_doe_id),
                RegistrationDto.class).getId();
    }

    @Test
    @Sql(statements = {"delete from registrations", "delete from students", "delete from training_classes"})
    void testSaveThenListAll() {
        long java_backend_id = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Java Backend Fejlesztő", new InternalDates(null, null)),
                TrainingClassDto.class).getId();
        long frontend_fejleszto_id = template.postForObject("/api/trainingclasses",
                new CreateTrainingClassCommand("Frontend Fejlesztő", new InternalDates(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 3, 12))),
                TrainingClassDto.class).getId();

        long john_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("John Doe", "johndoe@domain.com", "john_doe", ""),
                StudentDto.class).getId();
        long jane_doe_id = template.postForObject("/api/students",
                new CreateStudentCommand("Jane Doe", "janedoe@domain.com", "jane_doe", ""),
                StudentDto.class).getId();

        long jane_doe_frontend_fejleszto_id = template.postForObject("/api/trainingclasses/" + frontend_fejleszto_id + "/registrations",
                new CreateRegistrationCommand(jane_doe_id),
                RegistrationDto.class).getId();
        long john_doe_java_backend_id = template.postForObject("/api/trainingclasses/" + java_backend_id + "/registrations",
                new CreateRegistrationCommand(john_doe_id),
                RegistrationDto.class).getId();
        long john_doe_frontend_fejleszto_id = template.postForObject("/api/trainingclasses/" + frontend_fejleszto_id + "/registrations",
                new CreateRegistrationCommand(john_doe_id),
                RegistrationDto.class).getId();

        List<RegistrationDto> registrations = template.exchange("/api/registrations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RegistrationDto>>() {
                }).getBody();

        assertThat(registrations)
                .hasSize(3)
                .extracting(r -> r.getStudent().getName(), r -> r.getTrainingClass().getName(), RegistrationDto::getStatus)
                .contains(tuple("John Doe", "Frontend Fejlesztő", Status.ACTIVE));
    }

    @Test
    @Sql(statements = {"delete from registrations", "delete from training_classes", "delete from students"})
    void testGetRegistrationsByTrainingClass() {
        createSampleData();

        List<RegisteredStudentByTrainingClassDto> registrations = template.exchange("/api/trainingclasses/" + java_backend_id + "/registrations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RegisteredStudentByTrainingClassDto>>() {
                }).getBody();

        assertThat(registrations)
                .hasSize(1)
                .extracting(RegisteredStudentByTrainingClassDto::getStudentName)
                .contains("John Doe");
    }

    @Test
    @Sql(statements = {"delete from registrations", "delete from training_classes", "delete from students"})
    void testGetRegistrationsByStudent() {
        createSampleData();

        List<RegisteredTrainingClassByStudentDto> registrations = template.exchange("/api/students/" + jane_doe_id + "/registrations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RegisteredTrainingClassByStudentDto>>() {
                }).getBody();

        assertThat(registrations)
                .hasSize(1)
                .extracting(RegisteredTrainingClassByStudentDto::getTrainingClassName)
                .contains("Frontend Fejlesztő");
    }

    @Test
    @Sql(statements = {"delete from registrations", "delete from training_classes", "delete from students"})
    void testGetRegistrationById() {
        createSampleData();

        RegistrationDto registrations = template.exchange("/api/registrations/" + jane_doe_frontend_fejleszto_id,
                HttpMethod.GET,
                null,
                RegistrationDto.class).getBody();

        assertThat(registrations)
                .extracting(r -> r.getStudent().getName(), r -> r.getTrainingClass().getName())
                .contains("Jane Doe", "Frontend Fejlesztő");
    }

    @Test
    @Sql(statements = {"delete from registrations", "delete from training_classes", "delete from students"})
    void testUpdateRegistration() {
        createSampleData();

        jane_doe_frontend_fejleszto_id = template.exchange("/api/registrations/" + jane_doe_frontend_fejleszto_id,
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateRegistrationCommand(Status.EXIT_IN_PROGRESS)),
                RegistrationDto.class).getBody().getId();

        RegistrationDto registration = template.exchange("/api/registrations/" + jane_doe_frontend_fejleszto_id,
                HttpMethod.GET,
                null,
                RegistrationDto.class).getBody();

        assertEquals(Status.EXIT_IN_PROGRESS, registration.getStatus());
    }

    @Test
    @Sql(statements = {"delete from registrations", "delete from training_classes", "delete from students"})
    void testDeleteRegistrationById() {
        createSampleData();

        template.delete("/api/registrations/" + jane_doe_frontend_fejleszto_id);
        template.delete("/api/registrations/" + john_doe_frontend_fejleszto_id);
        template.delete("/api/registrations/" + john_doe_java_backend_id);

        List<RegistrationDto> registrations = template.exchange("/api/registrations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RegistrationDto>>() {
                }).getBody();

        assertTrue(registrations.isEmpty());
    }

    // TODO: 2021. 08. 08. Tesztek a hibás bemenő adatokra (@NotNull, @NotBlank vizsgálata)
}
