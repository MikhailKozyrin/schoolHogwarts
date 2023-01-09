package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);
    public Collection<Student> findByAgeBetween(int min, int max);

    public Collection<Student> findByFacultyId(long facultyId);

    @Query(value = "SELECT COUNT (*) FROM student", nativeQuery = true)
    int getCountAllStudents();

    @Query(value = "SELECT AVG(age) from student", nativeQuery = true)
    double getAverageAgeAllStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5;", nativeQuery = true)
    Collection<Student> getFiveLastStudents();
}
