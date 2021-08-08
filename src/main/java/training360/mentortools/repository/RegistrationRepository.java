package training360.mentortools.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training360.mentortools.entity.Registration;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> getRegistrationsByTrainingClassId(long id);

    List<Registration> getRegistrationsByStudentId(long id);
}
