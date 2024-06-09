package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAgeBetween(int ageMin, int ageMax);
    Collection<Student> findByAge(int age);

    @Query("SELECT COUNT(s) FROM Student s")
    Long countTotalStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    @Query(value = "SELECT * FROM students ORDER BY id DESc LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}
