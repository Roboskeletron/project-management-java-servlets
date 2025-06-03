package ru.vsu.projectmanagement.service;

import ru.vsu.projectmanagement.dao.TaskDao;
import ru.vsu.projectmanagement.domain.Task;
import ru.vsu.projectmanagement.domain.TaskPriority;
import ru.vsu.projectmanagement.domain.TaskStatus;
import ru.vsu.projectmanagement.domain.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final TaskDao taskDao;
    private final TaskHistoryService taskHistoryService; // For logging changes

    public TaskService(TaskDao taskDao, TaskHistoryService taskHistoryService) {
        this.taskDao = taskDao;
        this.taskHistoryService = taskHistoryService;
    }

    public Task createTask(Long projectId, String title, String description, TaskStatus status,
                           TaskPriority priority, Long assigneeId, User reporter, LocalDate dueDate) throws SQLException {
        Task task = new Task();
        task.setProjectId(projectId);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status != null ? status : TaskStatus.TODO);
        task.setPriority(priority != null ? priority : TaskPriority.MEDIUM);
        task.setAssigneeId(assigneeId);
        task.setReporterId(reporter.getId());
        task.setDueDate(dueDate);
        // createdAt, updatedAt set by DAO

        Task createdTask = taskDao.create(task);
        taskHistoryService.recordTaskCreation(createdTask, reporter);
        return createdTask;
    }

    public Optional<Task> findTaskById(Long id) throws SQLException {
        return taskDao.findById(id);
    }

    public List<Task> findTasksByProjectId(Long projectId) throws SQLException {
        return taskDao.findByProjectId(projectId);
    }

    public List<Task> findTasksAssignedToUser(Long userId) throws SQLException {
        return taskDao.findByAssigneeId(userId);
    }

    public void updateTask(Task task, User updater) throws SQLException {
        Optional<Task> oldTaskOpt = taskDao.findById(task.getId());
        if (oldTaskOpt.isEmpty()) {
            throw new SQLException("Task not found for update: " + task.getId());
        }
        Task oldTask = oldTaskOpt.get();

        taskDao.update(task);
        taskHistoryService.recordTaskUpdate(oldTask, task, updater);
    }

    public void deleteTask(Long id, User deleter) throws SQLException {
        // Optional: record deletion in history, though task_history might be deleted by cascade
        // taskHistoryService.recordHistory(id, deleter.getId(), "task_deleted", "N/A", "N/A");
        taskDao.delete(id);
    }

    public void changeTaskStatus(Long taskId, TaskStatus newStatus, User updater) throws SQLException {
        Optional<Task> taskOpt = taskDao.findById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            TaskStatus oldStatus = task.getStatus();
            if (oldStatus != newStatus) {
                task.setStatus(newStatus);
                taskDao.update(task);
                taskHistoryService.recordHistory(taskId, updater.getId(), "status", oldStatus.name(), newStatus.name());
            }
        } else {
            throw new SQLException("Task not found: " + taskId);
        }
    }

    public void assignTask(Long taskId, Long assigneeId, User assigner) throws SQLException {
        Optional<Task> taskOpt = taskDao.findById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            Long oldAssigneeId = task.getAssigneeId();
            if ((oldAssigneeId == null && assigneeId != null) || (oldAssigneeId != null && !oldAssigneeId.equals(assigneeId))) {
                task.setAssigneeId(assigneeId);
                taskDao.update(task);
                taskHistoryService.recordHistory(taskId, assigner.getId(), "assignee_id",
                        oldAssigneeId != null ? oldAssigneeId.toString() : "Unassigned",
                        assigneeId != null ? assigneeId.toString() : "Unassigned");
            }
        } else {
            throw new SQLException("Task not found: " + taskId);
        }
    }
}
