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
class StudentControllerTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String getRootUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    public void createStudent() {
        ResponseEntity<Student> newStudentRs = testRestTemplate.postForEntity(
                getRootUrl(),
                MOCK_STUDENT,
                Student.class
        );

        assertThat(newStudentRs.getStatusCode()).isEqualTo(HttpStatus.OK);

        Student newStudent = newStudentRs.getBody();

        assertThat(newStudent.getName()).isEqualTo(MOCK_STUDENT.getName());
        assertThat(newStudent.getAge()).isEqualTo(MOCK_STUDENT.getAge());
    }

    @Test
    void getStudent() {
        Student createdStudent = createMockStudent();

        ResponseEntity<Student> getStudentRs = testRestTemplate.getForEntity(
                getRootUrl() + "/" + createdStudent.getId(),
                Student.class
        );

        assertThat(getStudentRs.getStatusCode()).isEqualTo(HttpStatus.OK);

        Student student = getStudentRs.getBody();

        assertThat(student.getName()).isEqualTo(createdStudent.getName());
        assertThat(student.getAge()).isEqualTo(createdStudent.getAge());
    }

    @Test
    void deleteStudent() {
        Student createdStudent = createMockStudent();

        testRestTemplate.delete(
                getRootUrl() + "/" + createdStudent.getId(),
                Student.class
        );

        ResponseEntity<Student> getStudentRs = testRestTemplate.getForEntity(
                getRootUrl() + "/" + createdStudent.getId(),
                Student.class
        );

        assertThat(getStudentRs.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void updateStudent() {
        Student createdStudent = createMockStudent();

        createdStudent.setName(MOCK_STUDENT_NEW_NAME);

        testRestTemplate.put(
                getRootUrl(),
                createdStudent,
                Student.class
        );

        ResponseEntity<Student> getStudentRs = testRestTemplate.getForEntity(
                getRootUrl() + "/" + createdStudent.getId(),
                Student.class
        );

        assertThat(getStudentRs.getStatusCode()).isEqualTo(HttpStatus.OK);

        Student student = getStudentRs.getBody();

        assertThat(student.getName()).isEqualTo(createdStudent.getName());
    }

    @Test
    void getAllStudents() {
        createMockStudent();

        List<Student> students = testRestTemplate.exchange(
                getRootUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        ).getBody();

        assertFalse(students.isEmpty());
    }

    @Test
    void getStudentsByAgeRange() {
        createMockStudent(1L, MOCK_STUDENT_NAME, MOCK_STUDENT_AGE + 1);
        createMockStudent(2L, MOCK_STUDENT_NAME, MOCK_STUDENT_AGE + 2);

        List<Student> students = testRestTemplate.exchange(
                getRootUrl()+ "/age?min=" + MOCK_STUDENT_AGE + "&max=" + (MOCK_STUDENT_AGE + 1),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        ).getBody();

        assertFalse(students.size() >= 2);
    }

    @Test
    void getFacultyStudent() {
        Faculty createdFaculty = facultyService.createFaculty(MOCK_FACULTY);
        MOCK_STUDENT.setFaculty(createdFaculty);
        Student createdStudent = studentService.createStudent(MOCK_STUDENT);

        ResponseEntity<Faculty> getFacultyStudentRs = testRestTemplate.getForEntity(
                getRootUrl() + "/faculty/" + createdStudent.getId(),
                Faculty.class
        );

        assertThat(getFacultyStudentRs.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(getFacultyStudentRs.getBody());
    }

    public Student createMockStudent() {
        return studentService.createStudent(MOCK_STUDENT);
    }

    public Student createMockStudent(Long id, String name, Integer age) {
        return studentService.createStudent(new Student(id, name, age));
    }
}