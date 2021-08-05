package training360.mentortools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_classes")
public class TrainingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private InternalDates internalDates;

    public TrainingClass(String name, InternalDates internalDates) {
        this.name = name;
        this.internalDates = internalDates;
    }

    public void setInternalDates(LocalDate startDate, LocalDate finishDate) {
        internalDates.setStartDate(startDate);
        internalDates.setFinishDate(finishDate);
    }
}
