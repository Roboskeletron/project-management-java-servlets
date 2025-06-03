package ru.vsu.projectmanagement.dao;

import ru.vsu.projectmanagement.domain.TaskHistory;
import ru.vsu.projectmanagement.jdbc.Executor;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class TaskHistoryDao extends AbstractCrudDao<TaskHistory, Long> {

    public TaskHistoryDao(Executor executor) {
        super(executor, TaskHistory.class);
    }

    @Override
    protected String getTableName() {
        return "task_history";
    }

    @Override
    public TaskHistory create(TaskHistory historyEntry) throws SQLException {
        final String sql = "INSERT INTO task_history(task_id, user_id, field_changed, old_value, new_value, changed_at) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id, changed_at";
        long id = executor.executeInsertAndGetId(sql,
                historyEntry.getTaskId(),
                historyEntry.getUserId(),
                historyEntry.getFieldChanged(),
                historyEntry.getOldValue(),
                historyEntry.getNewValue(),
                OffsetDateTime.now()
        );
        historyEntry.setId(id);
        return findById(id).orElseThrow(() -> new SQLException("Failed to create task history entry."));
    }

    @Override
    public void update(TaskHistory entity) throws SQLException {
        // Task history is typically immutable
        throw new UnsupportedOperationException("Task history entries cannot be updated.");
    }

    public List<TaskHistory> findByTaskId(Long taskId) throws SQLException {
        String sql = "SELECT th.*, u.username as username FROM " + getTableName() +
                " th JOIN users u ON th.user_id = u.id " +
                " WHERE th.task_id = ? ORDER BY th.changed_at DESC";
        return executor.executeQuery(sql, mapper, taskId);
    }
}
