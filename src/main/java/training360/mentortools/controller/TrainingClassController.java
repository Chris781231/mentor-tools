package training360.mentortools.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import training360.mentortools.command.trainingclass.CreateTrainingClassCommand;
import training360.mentortools.command.trainingclass.UpdateTrainingClassCommand;
import training360.mentortools.dto.TrainingClassDto;
import training360.mentortools.service.TrainingClassService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/trainingclasses")
public class TrainingClassController {

    private TrainingClassService trainingClassService;

    @GetMapping
    public List<TrainingClassDto> findTrainingClasses(@RequestParam ("partofname") Optional<String> partOfName) {
        return trainingClassService.findTrainingClasses(partOfName);
    }

    @GetMapping("/{id}")
    public TrainingClassDto findTrainingClassById(@PathVariable long id) {
        return trainingClassService.findTrainingClassById(id);
    }

    @PostMapping
    public TrainingClassDto saveTrainingClass(@RequestBody @Valid CreateTrainingClassCommand command) {
        return trainingClassService.saveTrainingClass(command);
    }

    @PutMapping("/{id}")
    public TrainingClassDto updateTrainingClass(@PathVariable long id, @RequestBody @Valid UpdateTrainingClassCommand command) {
        return trainingClassService.updateTrainingClass(id, command);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        trainingClassService.deleteTrainingClassById(id);
    }
}
