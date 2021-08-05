package training360.mentortools.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import training360.mentortools.command.student.CreateStudentCommand;
import training360.mentortools.command.student.UpdateStudentCommand;
import training360.mentortools.dto.StudentDto;
import training360.mentortools.service.StudentService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private StudentService studentService;

    @GetMapping
    public List<StudentDto> findStudent(@RequestParam ("partofname") Optional<String> partOfName) {
        return studentService.findStudents(partOfName);
    }

    @GetMapping("/{id}")
    public StudentDto findStudentById(@PathVariable long id) {
        return studentService.findStudentById(id);
    }

    @PostMapping
    public StudentDto save(@RequestBody @Valid CreateStudentCommand command) {
        return studentService.save(command);
    }

    @PutMapping("/{id}")
    public StudentDto updateStudent(@PathVariable long id, @RequestBody @Valid UpdateStudentCommand command) {
        return studentService.updateStudent(id, command);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        studentService.deleteStudentById(id);
    }
}
