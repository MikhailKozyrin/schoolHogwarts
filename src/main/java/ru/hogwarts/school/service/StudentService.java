package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;


@Service
public class StudentService {
    private final StudentRepository studentRepository;
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        if (student.getId() < 0) {
            return null;
        }
        return studentRepository.save(student);
    }


    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }


    public Student editStudent(Student student) {
        if (findStudent(student.getId()) != null) {
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getStudentByAge(int age) {
        return studentRepository.findByAge(age);
    }
}
