package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    public Collection<Faculty> findByNameOrColor(String name, String color);
    public Collection<Faculty> findFacultiesByColor(String color);
}
