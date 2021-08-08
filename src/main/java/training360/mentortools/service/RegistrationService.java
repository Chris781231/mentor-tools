package training360.mentortools.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training360.mentortools.EntityNotFoundException;
import training360.mentortools.command.registration.CreateRegistrationCommand;
import training360.mentortools.command.registration.UpdateRegistrationCommand;
import training360.mentortools.dto.RegisteredStudentByTrainingClassDto;
import training360.mentortools.dto.RegisteredTrainingClassByStudentDto;
import training360.mentortools.dto.RegistrationDto;
import training360.mentortools.entity.Registration;
import training360.mentortools.entity.Student;
import training360.mentortools.entity.TrainingClass;
import training360.mentortools.othertypes.Status;
import training360.mentortools.repository.RegistrationRepository;
import training360.mentortools.repository.StudentRepository;
import training360.mentortools.repository.TrainingClassRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RegistrationService {

    private ModelMapper modelMapper;

    private RegistrationRepository registrationRepo;

    private TrainingClassRepository trainingClassRepo;

    private StudentRepository studentRepo;

    public RegistrationDto save(long trainingClassId, CreateRegistrationCommand command) {
        TrainingClass trainingClass = trainingClassRepo.findById(trainingClassId)
                .orElseThrow(() -> new EntityNotFoundException(trainingClassId, "TrainingClass"));
        Student student = studentRepo.findById(command.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException(command.getStudentId(), "Student"));
        Registration registration = new Registration(trainingClass, student, Status.ACTIVE);
        registrationRepo.save(registration);
        return modelMapper.map(registration, RegistrationDto.class);
    }

    public List<RegisteredStudentByTrainingClassDto> getRegistrationsByTrainingClassId(long trainingClassId) {
        List<Registration> registrations = registrationRepo.getRegistrationsByTrainingClassId(trainingClassId);
        return mapToRegisteredStudentByTrainingClassDto(registrations);
    }

    public List<RegisteredTrainingClassByStudentDto> getRegistrationByStudentId(long studentId) {
        List<Registration> registrations = registrationRepo.getRegistrationsByStudentId(studentId);
        return mapToRegisteredTrainingClassByStudentDto(registrations);
    }

    public List<RegistrationDto> getRegistrations() {
        List<Registration> registrations = registrationRepo.findAll();
        Type targetType = new TypeToken<List<RegistrationDto>>(){}.getType();
        return modelMapper.map(registrations, targetType);
    }

    public RegistrationDto findRegistrationById(long id) {
        Registration registration = registrationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Registration"));
        return modelMapper.map(registration, RegistrationDto.class);
    }

    @Transactional
    public RegistrationDto updateRegistration(long id, UpdateRegistrationCommand command) {
        Registration registration = registrationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Registration"));
        registration.setStatus(command.getStatus());
        return modelMapper.map(registration, RegistrationDto.class);
    }

    public void deleteRegistrationById(long id) {
        Registration registration = registrationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Registration"));
        registrationRepo.delete(registration);
    }

private List<RegisteredStudentByTrainingClassDto> mapToRegisteredStudentByTrainingClassDto(List<Registration> registrations) {
    List<RegisteredStudentByTrainingClassDto> registeredStudentByTrainingClassDtos = new ArrayList<>();
    for (Registration registration : registrations) {
        registeredStudentByTrainingClassDtos.add(new RegisteredStudentByTrainingClassDto(
                registration.getStudent().getId(),
                registration.getStudent().getName(),
                registration.getStatus()));
    }
    return registeredStudentByTrainingClassDtos;
}

    private List<RegisteredTrainingClassByStudentDto> mapToRegisteredTrainingClassByStudentDto(List<Registration> registrations) {
        List<RegisteredTrainingClassByStudentDto> registeredTrainingClassByStudentDtos = new ArrayList<>();
        for (Registration registration : registrations) {
            registeredTrainingClassByStudentDtos.add(new RegisteredTrainingClassByStudentDto(
                    registration.getTrainingClass().getId(),
                    registration.getTrainingClass().getName()));
        }
        return registeredTrainingClassByStudentDtos;
    }
}
