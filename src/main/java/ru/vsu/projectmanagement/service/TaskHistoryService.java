package ru.vsu.projectmanagement.service;

import ru.vsu.projectmanagement.dao.TaskHistoryDao;
import ru.vsu.projectmanagement.domain.Task;
import ru.vsu.projectmanagement.domain.TaskHistory;
import ru.vsu.projectmanagement.domain.User;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class TaskHistoryService {
    private final TaskHistoryDao taskHistoryDao;

    public TaskHistoryService(TaskHistoryDao taskHistoryDao) {
        this.taskHistoryDao = taskHistoryDao;
    }

    public void recordHistory(Long taskId, Long userId, String fieldChanged, String oldValue, String newValue) throws SQLException {
        TaskHistory historyEntry = new TaskHistory();
        historyEntry.setTaskId(taskId);
        historyEntry.setUserId(userId);
        historyEntry.setFieldChanged(fieldChanged);
        historyEntry.setOldValue(oldValue);
        historyEntry.setNewValue(newValue);
        // changedAt will be set by DAO
        taskHistoryDao.create(historyEntry);
    }

    public void recordTaskCreation(Task task, User reporter) throws SQLException {
        recordHistory(task.getId(), reporter.getId(), "task_created", null, task.getTitle());
    }

    // Generic method to compare two objects and log changes (simplified)
    public void recordTaskUpdate(Task oldTask, Task newTask, User updater) throws SQLException {
        if (!oldTask.getTitle().equals(newTask.getTitle())) {
            recordHistory(newTask.getId(), updater.getId(), "title", oldTask.getTitle(), newTask.getTitle());
        }
        if (oldTask.getDescription() == null ? newTask.getDescription() != null : !oldTask.getDescription().equals(newTask.getDescription())) {
            recordHistory(newTask.getId(), updater.getId(), "description", oldTask.getDescription(), newTask.getDescription());
        }
        if (oldTask.getStatus() != newTask.getStatus()) {
            recordHistory(newTask.getId(), updater.getId(), "status", oldTask.getStatus().name(), newTask.getStatus().name());
        }
        if (oldTask.getPriority() != newTask.getPriority()) {
            recordHistory(newTask.getId(), updater.getId(), "priority", oldTask.getPriority().name(), newTask.getPriority().name());
        }
        if (oldTask.getAssigneeId() == null ? newTask.getAssigneeId() != null : !oldTask.getAssigneeId().equals(newTask.getAssigneeId())) {
            recordHistory(newTask.getId(), updater.getId(), "assignee_id",
                    oldTask.getAssigneeId() != null ? oldTask.getAssigneeId().toString() : "Unassigned",
                    newTask.getAssigneeId() != null ? newTask.getAssigneeId().toString() : "Unassigned");
        }
        if (oldTask.getDueDate() == null ? newTask.getDueDate() != null : !oldTask.getDueDate().equals(newTask.getDueDate())) {
            recordHistory(newTask.getId(), updater.getId(), "due_date",
                    oldTask.getDueDate() != null ? oldTask.getDueDate().toString() : "None",
                    newTask.getDueDate() != null ? newTask.getDueDate().toString() : "None");
        }
    }


    public List<TaskHistory> getHistoryForTask(Long taskId) throws SQLException {
        return taskHistoryDao.findByTaskId(taskId);
    }
}