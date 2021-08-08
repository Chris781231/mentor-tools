package training360.mentortools.dto.registration;

import lombok.Data;
import lombok.NoArgsConstructor;
import training360.mentortools.entity.Student;
import training360.mentortools.entity.TrainingClass;
import training360.mentortools.othertypes.Status;

@Data
@NoArgsConstructor
public class RegistrationDto {

    private Long id;

    private TrainingClass trainingClass;

    private Student student;

    private Status status;
}
