package ru.vsu.projectmanagement.dao;

import ru.vsu.projectmanagement.domain.Task;
import ru.vsu.projectmanagement.domain.TaskPriority;
import ru.vsu.projectmanagement.domain.TaskStatus;
import ru.vsu.projectmanagement.jdbc.Executor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class TaskDao extends AbstractCrudDao<Task, Long> {

    public TaskDao(Executor executor) {
        super(executor, Task.class);
    }

    @Override
    protected String getTableName() {
        return "tasks";
    }

    @Override
    public Task create(Task task) throws SQLException {
        final String sql = "INSERT INTO tasks(project_id, title, description, status, priority, " +
                "assignee_id, reporter_id, due_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at, updated_at";

        long id = executor.executeInsertAndGetId(sql,
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus() != null ? task.getStatus().getDbValue() : TaskStatus.TODO.getDbValue(),
                task.getPriority() != null ? task.getPriority().getDbValue() : TaskPriority.MEDIUM.getDbValue(),
                task.getAssigneeId(),
                task.getReporterId(),
                task.getDueDate(), // Executor handles LocalDate to java.sql.Date
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        task.setId(id);
        return findById(id).orElseThrow(() -> new SQLException("Failed to create task."));
    }

    @Override
    public void update(Task task) throws SQLException {
        final String sql = "UPDATE tasks SET project_id = ?, title = ?, description = ?, status = ?, " +
                "priority = ?, assignee_id = ?, reporter_id = ?, due_date = ?, updated_at = ? " +
                "WHERE id = ?";
        executor.executeUpdate(sql,
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus() != null ? task.getStatus().getDbValue() : TaskStatus.TODO.getDbValue(),
                task.getPriority() != null ? task.getPriority().getDbValue() : TaskPriority.MEDIUM.getDbValue(),
                task.getAssigneeId(),
                task.getReporterId(),
                task.getDueDate(),
                OffsetDateTime.now(),
                task.getId());
    }

    public List<Task> findByProjectId(Long projectId) throws SQLException {
        // A more detailed query joining with users for names
        String sql = "SELECT t.*, p.name as project_name, " +
                "assignee.full_name as assignee_name, reporter.full_name as reporter_name " +
                "FROM tasks t " +
                "JOIN projects p ON t.project_id = p.id " +
                "LEFT JOIN users assignee ON t.assignee_id = assignee.id " +
                "JOIN users reporter ON t.reporter_id = reporter.id " +
                "WHERE t.project_id = ? ORDER BY t.priority DESC, t.due_date ASC, t.created_at ASC";
        return executor.executeQuery(sql, mapper, projectId);
    }

    public List<Task> findByAssigneeId(Long userId) throws SQLException {
        String sql = "SELECT t.*, p.name as project_name, " +
                "assignee.full_name as assignee_name, reporter.full_name as reporter_name " +
                "FROM tasks t " +
                "JOIN projects p ON t.project_id = p.id " +
                "LEFT JOIN users assignee ON t.assignee_id = assignee.id " +
                "JOIN users reporter ON t.reporter_id = reporter.id " +
                "WHERE t.assignee_id = ? ORDER BY t.due_date ASC, t.priority DESC";
        return executor.executeQuery(sql, mapper, userId);
    }

    // Override findById to include joined names
    @Override
    public java.util.Optional<Task> findById(Long id) throws SQLException {
        String sql = "SELECT t.*, p.name as project_name, " +
                "assignee.full_name as assignee_name, reporter.full_name as reporter_name " +
                "FROM tasks t " +
                "JOIN projects p ON t.project_id = p.id " +
                "LEFT JOIN users assignee ON t.assignee_id = assignee.id " +
                "JOIN users reporter ON t.reporter_id = reporter.id " +
                "WHERE t.id = ?";
        Task task = executor.executeSingleResult(sql, mapper, id);
        return java.util.Optional.ofNullable(task);
    }
}
