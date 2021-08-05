package training360.mentortools.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training360.mentortools.entity.TrainingClass;

import java.util.List;

public interface TrainingClassRepository extends JpaRepository<TrainingClass, Long> {

    List<TrainingClass> findByNameIsContainingIgnoreCase(String name);
}
