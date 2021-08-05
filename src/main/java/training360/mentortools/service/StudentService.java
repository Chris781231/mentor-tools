package training360.mentortools.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training360.mentortools.EntityNotFoundException;
import training360.mentortools.command.student.CreateStudentCommand;
import training360.mentortools.command.student.UpdateStudentCommand;
import training360.mentortools.dto.StudentDto;
import training360.mentortools.entity.Student;
import training360.mentortools.repository.StudentRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {

    private ModelMapper modelMapper;

    private StudentRepository studentRepository;

    public StudentDto save(CreateStudentCommand command) {
        Student student = new Student(command.getName(), command.getEmail(), command.getGithubAccount(), command.getDescription());
        studentRepository.save(student);
        return modelMapper.map(student, StudentDto.class);
    }

    public List<StudentDto> findStudents(Optional<String> partOfName) {
        List<Student> students;
        if (partOfName.isPresent()) {
            students = studentRepository.findByNameIsContainingIgnoreCase(partOfName.get());
        } else {
            students = studentRepository.findAll();
        }
        Type targetType = new TypeToken<List<StudentDto>>() {
        }.getType();
        return modelMapper.map(students, targetType);
    }

    public StudentDto findStudentById(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Student"));
        return modelMapper.map(student, StudentDto.class);
    }

    @Transactional
    public StudentDto updateStudent(long id, UpdateStudentCommand command) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Student"));
        student.setName(command.getName());
        student.setEmail(command.getEmail());
        student.setGithubAccount(command.getGithubAccount());
        student.setDescription(command.getDescription());

        return modelMapper.map(student, StudentDto.class);
    }

    public void deleteStudentById(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Student"));
        studentRepository.delete(student);
    }
}
