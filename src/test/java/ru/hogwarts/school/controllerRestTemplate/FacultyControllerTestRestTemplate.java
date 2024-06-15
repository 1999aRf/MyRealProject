package ru.hogwarts.school.controllerRestTemplate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.SchoolApplication;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.hogwarts.school.ConstantsTest.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SchoolApplication.class)
class FacultyControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;
    @Autowired
    private TestRestTemplate testRestTemplate;

    private String getRootUrl() {
        return "http://localhost:" + port + "/faculty";
    }

    @Test
    void createFaculty() throws Exception{
        ResponseEntity<Faculty> newFacultyRs = testRestTemplate.postForEntity(
                getRootUrl(),
                MOCK_FACULTY,
                Faculty.class
        );

        assertThat(newFacultyRs.getStatusCode()).isEqualTo(HttpStatus.OK);

        Faculty newFaculty = newFacultyRs.getBody();

        assertThat(newFaculty.getName()).isEqualTo(MOCK_FACULTY.getName());
        assertThat(newFaculty.getColor()).isEqualTo(MOCK_FACULTY.getColor());
    }

    @Test
    void getFaculty() throws Exception{
        Faculty createdFaculty = createMockFaculty();

        ResponseEntity<Faculty> getStudentRs = testRestTemplate.getForEntity(
                getRootUrl() + "/" + createdFaculty.getId(),
                Faculty.class
        );

        assertThat(getStudentRs.getStatusCode()).isEqualTo(HttpStatus.OK);

        Faculty faculty = getStudentRs.getBody();

        assertThat(faculty.getName()).isEqualTo(createdFaculty.getName());
        assertThat(faculty.getColor()).isEqualTo(createdFaculty.getColor());
    }

    @Test
    void deleteFaculty() throws Exception{
        Faculty createdFaculty = createMockFaculty();

        testRestTemplate.delete(
                getRootUrl() + "/" + createdFaculty.getId(),
                Faculty.class
        );

        ResponseEntity<Faculty> getFacultyRs = testRestTemplate.getForEntity(
                getRootUrl() + "/" + createdFaculty.getId(),
                Faculty.class
        );

        assertThat(getFacultyRs.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void updateFaculty() throws Exception{
        Faculty createdFaculty = createMockFaculty();

        createdFaculty.setName(MOCK_FACULTY_NEW_NAME);

        testRestTemplate.put(
                getRootUrl() + "/" + createdFaculty.getId(),
                createdFaculty,
                Faculty.class
        );

        ResponseEntity<Faculty> getFacultyRs = testRestTemplate.getForEntity(
                getRootUrl() + "/" + createdFaculty.getId(),
                Faculty.class
        );

        assertThat(getFacultyRs.getStatusCode()).isEqualTo(HttpStatus.OK);

        Faculty faculty = getFacultyRs.getBody();

        assertThat(faculty.getName()).isEqualTo(createdFaculty.getName());
    }

    @Test
    void getAllFaculties() throws Exception{
        createMockFaculty();

        List<Faculty> faculties = testRestTemplate.exchange(
                getRootUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        ).getBody();

        assertFalse(faculties.isEmpty());
    }

    @Test
    void getFacultiesByColor() throws Exception{
        createMockFaculty();

        ResponseEntity<List<Faculty>> faculties = testRestTemplate.exchange(
                getRootUrl()+ "/filterByColor?color=" + MOCK_FACULTY_COLOR ,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertThat(faculties.getStatusCodeValue()).isEqualTo(200);
        assertThat(faculties.getBody()).isNotNull();
        assertThat(faculties.getBody().size()).isGreaterThan(0);
    }

    @Test
    void getFacultyStudents() throws Exception{
        Faculty createdFaculty = facultyService.createFaculty(MOCK_FACULTY);
        MOCK_STUDENT.setFaculty(createdFaculty);
        Student createdStudent = studentService.createStudent(MOCK_STUDENT);

        ResponseEntity<List<Student>> getStudentFacultyRs = testRestTemplate.exchange(
                getRootUrl() + "/students/" + createdFaculty.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );

        assertThat(getStudentFacultyRs.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(getStudentFacultyRs.getBody());
    }

    public Faculty createMockFaculty() {
        return facultyService.createFaculty(MOCK_FACULTY);
    }
}