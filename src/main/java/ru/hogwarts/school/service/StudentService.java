package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for creating student");
        return studentRepository.save(student);
    }

    public Student findStudent(Long Id) {
        logger.debug("Was invoked method for finding student with id = {}", Id);
        return studentRepository.findById(Id).orElseThrow(() -> {
            logger.error("There is no student with id = {}", Id);
            return new EntityNotFoundException("Student not found");
        });
    }

    public Collection<Student> getAllStudents() {
        logger.debug("Was invoked method for getting all students");
        return studentRepository.findAll();
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for updating student with id = {}", student.getId());
       return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.warn("Was invoked method for deleting student with id = {}", id);
        try {
        studentRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting student with id = {}", id, e);
            throw e;
        }
    }

    public Collection<Student> findStudentsByAge(int age) {
        logger.debug("Was invoked method for finding students by age = {}", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentByBetweenAge(int ageMin, int ageMax) {
        logger.debug("Was invoked method for finding students between ages {} and {}", ageMin, ageMax);
           return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    public Faculty getFacultyStudent(Long studentId) {
        logger.debug("Was invoked method for getting faculty of student with id = {}", studentId);
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElseThrow(() -> {
                    logger.error("Student not found with id = {}", studentId);
                    return new EntityNotFoundException("Student not found");
                });
    }

    public Long countTotalStudents() {
        return studentRepository.countTotalStudents();
    }

    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    public List<Student> findLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }
}
