package training360.mentortools.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import training360.mentortools.command.registration.CreateRegistrationCommand;
import training360.mentortools.command.registration.UpdateRegistrationCommand;
import training360.mentortools.dto.registration.RegisteredStudentByTrainingClassDto;
import training360.mentortools.dto.registration.RegisteredTrainingClassByStudentDto;
import training360.mentortools.dto.registration.RegistrationDto;
import training360.mentortools.service.RegistrationService;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("/trainingclasses/{id}/registrations")
    public RegistrationDto save(@PathVariable ("id") long trainingClassId, @RequestBody @Valid CreateRegistrationCommand command) {
        return registrationService.save(trainingClassId, command);
    }

    @GetMapping("/trainingclasses/{id}/registrations")
    public List<RegisteredStudentByTrainingClassDto> findRegistrationByTrainingClassId(@PathVariable ("id") long trainingClassId) {
        return registrationService.getRegistrationsByTrainingClassId(trainingClassId);
    }

    @GetMapping("/students/{id}/registrations")
    public List<RegisteredTrainingClassByStudentDto> findRegistrationByStudentId(@PathVariable ("id") long studentId) {
        return registrationService.getRegistrationByStudentId(studentId);
    }

    @GetMapping("/registrations")
    public List<RegistrationDto> getRegistrations() {
        return registrationService.getRegistrations();
    }

    @GetMapping("/registrations/{id}")
    public RegistrationDto findRegistrationById(@PathVariable long id) {
        return registrationService.findRegistrationById(id);
    }

    @PutMapping("/registrations/{id}")
    public RegistrationDto updateRegistration(@PathVariable long id, @RequestBody @Valid UpdateRegistrationCommand command) {
        return registrationService.updateRegistration(id, command);
    }

    @DeleteMapping("/registrations/{id}")
    public void deleteById(@PathVariable long id) {
        registrationService.deleteRegistrationById(id);
    }

//    @GetMapping("/registrations")
//    public List<RegistrationDto> getRegistrations(@RequestParam ("partofname") Optional<String> partOfName) {
//        return registrationService.findRegistrations(partOfName);
//    }
}
