package training360.mentortools.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training360.mentortools.EntityNotFoundException;
import training360.mentortools.command.trainingclass.CreateTrainingClassCommand;
import training360.mentortools.command.trainingclass.UpdateTrainingClassCommand;
import training360.mentortools.dto.TrainingClassDto;
import training360.mentortools.entity.InternalDates;
import training360.mentortools.entity.TrainingClass;
import training360.mentortools.repository.InternalDatesRepository;
import training360.mentortools.repository.TrainingClassRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TrainingClassService {

    private ModelMapper modelMapper;

    private TrainingClassRepository trainingClassRepository;

    private InternalDatesRepository internalDatesRepo;

    public TrainingClassDto saveTrainingClass(CreateTrainingClassCommand command) {
        InternalDates internalDates = new InternalDates(command.getInternalDates().getStartDate(), command.getInternalDates().getFinishDate());
        TrainingClass trainingClass = new TrainingClass(command.getName(), internalDates);
        internalDatesRepo.save(internalDates);
        trainingClassRepository.save(trainingClass);
        return modelMapper.map(trainingClass, TrainingClassDto.class);
    }

    public List<TrainingClassDto> findTrainingClasses(Optional<String> partOfName) {
        List<TrainingClass> trainingClasses;
        if (partOfName.isPresent()) {
            trainingClasses = trainingClassRepository.findByNameIsContainingIgnoreCase(partOfName.get());
        } else {
            trainingClasses = trainingClassRepository.findAll();
        }
        Type targetType = new TypeToken<List<TrainingClassDto>>() {
        }.getType();
        return modelMapper.map(trainingClasses, targetType);
    }

    public TrainingClassDto findTrainingClassById(long id) {
        TrainingClass trainingClass = trainingClassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "TrainingClass"));
        return modelMapper.map(trainingClass, TrainingClassDto.class);
    }

    @Transactional
    public TrainingClassDto updateTrainingClass(long id, UpdateTrainingClassCommand command) {
        TrainingClass trainingClass = trainingClassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "TrainingClass"));
        trainingClass.setName(command.getName());
        trainingClass.setInternalDates(command.getInternalDates().getStartDate(), command.getInternalDates().getFinishDate());

        return modelMapper.map(trainingClass, TrainingClassDto.class);
    }

    public void deleteTrainingClassById(long id) {
        TrainingClass trainingClass = trainingClassRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "TrainingClass"));
        trainingClassRepository.delete(trainingClass);
    }
}
