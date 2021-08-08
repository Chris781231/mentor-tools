package training360.mentortools.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentDto {

    private Long id;

    private String name;

    private String email;

    private String githubAccount;

    private String description;
}
