package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    // Создание факультета
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    // Получение факультета по ID
    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // Получение всех факультетов
    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    // Обновление данных факультета
    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    // Удаление факультета
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findFacultiesByColor(String color) {
        return getAllFaculties().stream()
                                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                                .collect(Collectors.toList());
    }
}
