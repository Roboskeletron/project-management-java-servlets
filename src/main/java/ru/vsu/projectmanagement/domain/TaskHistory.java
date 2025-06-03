package ru.vsu.projectmanagement.domain;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class TaskHistory {
    private long id;
    private long taskId;
    private long userId;
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private OffsetDateTime changedAt;

    // Optional: Fields for joined data
    private String username; // User who made the change

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public TaskHistory() {
    }

    // Constructor, getters, and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTaskId() { return taskId; }
    public void setTaskId(long taskId) { this.taskId = taskId; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public String getFieldChanged() { return fieldChanged; }
    public void setFieldChanged(String fieldChanged) { this.fieldChanged = fieldChanged; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public OffsetDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(OffsetDateTime changedAt) { this.changedAt = changedAt; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFormattedChangedAt() {
        if (this.changedAt == null) {
            return "";
        }
        return this.changedAt.format(DT_FORMATTER);
    }
}
