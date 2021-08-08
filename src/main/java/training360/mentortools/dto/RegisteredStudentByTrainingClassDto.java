package training360.mentortools.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import training360.mentortools.othertypes.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredStudentByTrainingClassDto {

    private long studentId;

    private String studentName;

    private Status status;
}
