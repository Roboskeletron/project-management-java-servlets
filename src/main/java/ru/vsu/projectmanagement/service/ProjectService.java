package ru.vsu.projectmanagement.service;

import ru.vsu.projectmanagement.dao.ProjectDao;
import ru.vsu.projectmanagement.domain.Project;
import ru.vsu.projectmanagement.domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProjectService {
    private final ProjectDao projectDao;

    public ProjectService(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public Project createProject(String name, String description, User owner) throws SQLException {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setOwnerId(owner.getId());
        // createdAt and updatedAt will be set by DAO or DB
        return projectDao.create(project);
    }

    public Optional<Project> findProjectById(Long id) throws SQLException {
        return projectDao.findById(id);
    }

    public List<Project> findAllProjects() throws SQLException {
        return projectDao.findAll();
    }

    public List<Project> findProjectsByOwner(User owner) throws SQLException {
        return projectDao.findByOwnerId(owner.getId());
    }

    public void updateProject(Project project) throws SQLException {
        projectDao.update(project);
    }

    public void deleteProject(Long id) throws SQLException {
        projectDao.delete(id);
    }
}