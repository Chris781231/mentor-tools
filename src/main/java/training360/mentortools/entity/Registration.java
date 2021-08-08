package training360.mentortools.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import training360.mentortools.othertypes.Status;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "trainingclass_id")
    private TrainingClass trainingClass;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Registration(TrainingClass trainingClass, Student student, Status status) {
        this.trainingClass = trainingClass;
        this.student = student;
        this.status = status;
    }
}
