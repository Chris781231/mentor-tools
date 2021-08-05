package training360.mentortools.command.trainingclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import training360.mentortools.entity.InternalDates;
import training360.mentortools.validation.IsValidFinishDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTrainingClassCommand {

    @NotBlank
    @Size(max = 255)
    private String name;

    @IsValidFinishDate
    private InternalDates internalDates;
}
