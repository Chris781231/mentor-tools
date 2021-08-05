package training360.mentortools.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "internal_dates_for_training_classes")
public class InternalDates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    private LocalDate startDate;

    private LocalDate finishDate;

    public InternalDates(LocalDate startDate, LocalDate finishDate) {
        this.startDate = startDate;
        this.finishDate = finishDate;
    }
}
