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
import java.util.stream.Collectors;

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

    public Collection<Student> findByAge(int age) {
        logger.debug("Was invoked method for finding students by age = {}", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByBetweenAge(int ageMin, int ageMax) {
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

    public Collection<String> getNamesByA() {
        logger.info("Was invoked method for get names by A");

        return studentRepository.findAll().stream()
                .filter(student -> student.getName().startsWith("A"))
                .map(n -> n.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAgeByStream() {
        logger.info("Was invoked method for get average age");

        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0f);
    }

    public void printStudentsParallel() {
        List<Student> students = studentRepository.findAll();

        if (students.size() >= 6) {
            students.subList(0, 2).forEach(this::printStudent);
            printStudentsNewThread(students.subList(2, 4));
            printStudentsNewThread(students.subList(4, 6));
        }
    }

    public void printStudentsSync() {
        List<Student> students = studentRepository.findAll();

        if (students.size() >= 6) {
            students.subList(0, 2).forEach(this::printStudentSync);
            printStudentsNewThreadSync(students.subList(2, 4));
            printStudentsNewThreadSync(students.subList(4, 6));
        }
    }

    private void printStudent(Student student) {
        logger.info("Student, id: {}, name: {}", student.getId(), student.getName());
    }

    private void printStudentsNewThread(List<Student> students) {
        new Thread(() -> {
            students.forEach(this::printStudent);
        }).start();
    }

    private synchronized void printStudentSync(Student student) {
        logger.info("Student, id: {}, name: {}", student.getId(), student.getName());
    }

    private void printStudentsNewThreadSync(List<Student> students) {
        new Thread(() -> {
            students.forEach(this::printStudentSync);
        }).start();
    }
}
