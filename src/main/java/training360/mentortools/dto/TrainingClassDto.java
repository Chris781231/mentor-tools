package training360.mentortools.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import training360.mentortools.othertypes.InternalDates;

@Data
@NoArgsConstructor
public class TrainingClassDto {

    private Long id;

    private String name;

    private InternalDates internalDates;
}
