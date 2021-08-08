package training360.mentortools.dto.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredTrainingClassByStudentDto {

    private long trainingClassId;

    private String trainingClassName;
}
