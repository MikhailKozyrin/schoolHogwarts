package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolHogwartsApplicationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testGetStudent() throws Exception {
        Student student = createTestStudent("Test1", 28);
        ResponseEntity<Student> responseCreated = getCreateStudentResponse(student);
        assertCreatedStudent(responseCreated);

        Student createdStudent = responseCreated.getBody();
        assert createdStudent != null;
        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + createdStudent.getId(),
                Student.class);

        Assertions.assertThat(response.getBody()).isEqualTo(createdStudent);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetStudentsByAge() throws Exception {
        int age = 20;

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/age?age=" + age,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


        if (!response.getBody().isEmpty()) {
            for (Student student : response.getBody()) {
                Assertions.assertThat(student.getAge()).isEqualTo(age);
            }
        }
    }

    @Test
    public void testFindByAgeBetween() throws Exception {
        int min = 15;
        int max = 18;

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student?min=" + min + "&max=" + max,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        if (!response.getBody().isEmpty()) {
            for (Student student : response.getBody()) {
                Assertions.assertThat(student.getAge()).isBetween(min, max);
            }
        }
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student student = createTestStudent("Test1", 28);
        ResponseEntity<Student> response = getCreateStudentResponse(student);
        assertCreatedStudent(response);
    }

    @Test
    public void testEditStudent() throws Exception {
        String newName = "UpdatedTest";
        int newAge = 40;


        Student student = createTestStudent("Test1", 28);
        ResponseEntity<Student> responseCreated = getCreateStudentResponse(student);
        assertCreatedStudent(responseCreated);


        Student createdStudent = responseCreated.getBody();
        assert createdStudent != null;
        createdStudent.setName(newName);
        createdStudent.setAge(newAge);

        restTemplate.put(
                "http://localhost:" + port + "/student",
                createdStudent);


        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + createdStudent.getId(),
                Student.class);

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo(newName);
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(newAge);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Student student = createTestStudent("Test1", 28);
        ResponseEntity<Student> responseCreated = getCreateStudentResponse(student);
        assertCreatedStudent(responseCreated);


        Student createdStudent = responseCreated.getBody();


        assert createdStudent != null;
        restTemplate.delete(
                "http://localhost:" + port + "/student/" + createdStudent.getId());


        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + createdStudent.getId(),
                Student.class);


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Student createTestStudent(String name, int age) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        return student;
    }

    private ResponseEntity<Student> getCreateStudentResponse(Student student) {
        return restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Student.class);
    }

    private void assertCreatedStudent(ResponseEntity<Student> response) {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

}
