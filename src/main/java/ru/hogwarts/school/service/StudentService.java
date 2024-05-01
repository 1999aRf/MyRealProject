package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private Map<Long, Student> students = new HashMap<>();
    private long nextId = 1;


    public Student createStudent(String name, int age) {
        Student student = new Student(nextId++, name, age);
        students.put(student.getId(), student);
        return student;
    }

    public Student findStudent(Long Id) {
        return students.get(Id);
    }

    public Collection<Student> getAllStudents() {
        return students.values();
    }

    public Student updateStudent(Long id, String name, int age) {
        Student student = students.get(id);
        if (student != students.get(id)) {
            student.setName(name);
            student.setAge(age);
        }
        return student;
    }

    public boolean deleteStudent(Long id) {
        return students.remove(id) != null;
    }

    public Collection<Student> findStudentsByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
