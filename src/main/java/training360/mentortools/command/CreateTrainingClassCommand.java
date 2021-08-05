package training360.mentortools.command;

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
    @Size(max = 200)
    private String name;

    @IsValidFinishDate
    private InternalDates internalDates;
}
