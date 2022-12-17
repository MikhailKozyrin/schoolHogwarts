package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {

        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        if (faculty.getId() < 0) {
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {

        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (findFaculty(faculty.getId()) != null) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public void deleteFaculty(long id) {

        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getFacultiesByColor(String color) {

        return facultyRepository.findByColor(color);
    }

}
