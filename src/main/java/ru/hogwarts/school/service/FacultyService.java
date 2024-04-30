package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long nextId = 1;

    // Создание факультета
    public Faculty createFaculty(String name, String color) {
        Faculty faculty = new Faculty(nextId++, name, color);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    // Получение факультета по ID
    public Faculty findFaculty(Long id) {
        return faculties.get(id);
    }

    // Получение всех факультетов
    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    // Обновление данных факультета
    public Faculty updateFaculty(Long id, String name, String color) {
        Faculty faculty = faculties.get(id);
        if (faculty != null) {
            faculty.setName(name);
            faculty.setColor(color);
        }
        return faculty;
    }

    // Удаление факультета
    public boolean deleteFaculty(Long id) {
        return faculties.remove(id) != null;
    }

    public Collection<Faculty> findFacultiesByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }
}
