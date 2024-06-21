package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    // Создание факультета
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for creating faculty");
        return facultyRepository.save(faculty);
    }

    // Получение факультета по ID
    public Faculty findFaculty(Long id) {
        logger.debug("Was invoked method for finding faculty with id = {}", id);
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is no faculty with id = {}", id);
            return new EntityNotFoundException("Faculty not found");
        });
    }

    // Получение всех факультетов
    public Collection<Faculty> getAllFaculties() {
        logger.debug("Was invoked method for getting all faculties");
        return facultyRepository.findAll();
    }

    // Обновление данных факультета
    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Was invoked method for updating faculty with id = {}", faculty.getId());
        return facultyRepository.save(faculty);
    }

    // Удаление факультета
    public void deleteFaculty(Long id) {
        logger.warn("Was invoked method for deleting faculty with id = {}", id);
        try {
            facultyRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting faculty with id = {}", id, e);
            throw e;
        }
    }

    public Collection<Faculty> findFacultiesByColor(String color) {
        logger.debug("Was invoked method for finding faculties by color = {}", color);
        return facultyRepository.findFacultiesByColor(color);
    }

    public List<Student> getStudentFaculty(Long facultyId) {
        logger.debug("Was invoked method for getting students of faculty with id = {}", facultyId);
        return facultyRepository.findById(facultyId)
                .map(Faculty::getStudents)
                .orElseThrow(() -> {
                    logger.error("Faculty not found with id = {}", facultyId);
                    return new EntityNotFoundException("Faculty not found");
                });
    }

    public String getLongFacultyName() {
        logger.info("Was invoked method for get long name Faculty");

        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length))
                .orElse("");
    }
}
