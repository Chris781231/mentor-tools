package training360.mentortools.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import training360.mentortools.othertypes.InternalDates;

public interface InternalDatesRepository extends JpaRepository<InternalDates, Long> {
}
