package training360.mentortools.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import training360.mentortools.entity.InternalDates;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingClassDto {

    private Long id;

    private String name;

    private InternalDates internalDates;
}
