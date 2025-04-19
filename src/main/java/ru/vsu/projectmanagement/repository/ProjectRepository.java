package ru.vsu.projectmanagement.repository;

import ru.vsu.projectmanagement.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project create(Project project);
    void update(Project project);
    Optional<Project> findById(int id);
    List<Project> findByName(String name);
}
