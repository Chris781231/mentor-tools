package training360.mentortools.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training360.mentortools.entity.Student;
import training360.mentortools.entity.TrainingClass;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByNameIsContainingIgnoreCase(String name);
}
