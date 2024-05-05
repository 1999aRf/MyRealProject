package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;


    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long Id) {
        return studentRepository.findById(Id).orElseThrow(EntityNotFoundException::new);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student updateStudent(Student student) {
       return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> findStudentsByAge(int age) {
        return getAllStudents().stream()
                                .filter(student -> student.getAge() == age)
                                .collect(Collectors.toList());
    }

    public Collection<Student> findStudentByBetweenAge(int ageMin, int ageMax) {
        return studentRepository.findStudentByAgeBetween(ageMin, ageMax);
    }
}
