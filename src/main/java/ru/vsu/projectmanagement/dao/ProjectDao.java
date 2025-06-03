package ru.vsu.projectmanagement.dao;

import ru.vsu.projectmanagement.domain.Project;
import ru.vsu.projectmanagement.jdbc.Executor;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class ProjectDao extends AbstractCrudDao<Project, Long> {

    public ProjectDao(Executor executor) {
        super(executor, Project.class);
    }

    @Override
    protected String getTableName() {
        return "projects";
    }

    @Override
    public Project create(Project project) throws SQLException {
        final String sql = "INSERT INTO projects(name, description, owner_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id, created_at, updated_at";
        long id = executor.executeInsertAndGetId(sql,
                project.getName(),
                project.getDescription(),
                project.getOwnerId(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        project.setId(id);
        return findById(id).orElseThrow(() -> new SQLException("Failed to create project."));
    }

    @Override
    public void update(Project project) throws SQLException {
        final String sql = "UPDATE projects SET name = ?, description = ?, owner_id = ?, updated_at = ? WHERE id = ?";
        executor.executeUpdate(sql,
                project.getName(),
                project.getDescription(),
                project.getOwnerId(),
                OffsetDateTime.now(),
                project.getId());
    }

    public List<Project> findByOwnerId(Long ownerId) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE owner_id = ? ORDER BY name";
        return executor.executeQuery(sql, mapper, ownerId);
    }
}