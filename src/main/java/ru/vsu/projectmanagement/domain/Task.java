package ru.vsu.projectmanagement.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private long id;
    private long projectId;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId; // Nullable
    private long reporterId;
    private LocalDate dueDate; // Nullable
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // Fields for joined data, not directly mapped unless query supports it
    private String projectName;
    private String assigneeName;
    private String reporterName;

    private static final DateTimeFormatter OFFSET_DT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter ISO_LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;


    public Task() {
    }

    // Constructor, getters, and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getProjectId() { return projectId; }
    public void setProjectId(long projectId) { this.projectId = projectId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }
    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }
    public long getReporterId() { return reporterId; }
    public void setReporterId(long reporterId) { this.reporterId = reporterId; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getAssigneeName() { return assigneeName; }
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getFormattedDueDate() {
        if (this.dueDate == null) {
            return "";
        }
        return this.dueDate.format(LOCAL_DATE_FORMATTER);
    }

    public String getDueDateForInput() {
        if (this.dueDate == null) {
            return "";
        }
        return this.dueDate.format(ISO_LOCAL_DATE_FORMATTER);
    }

    public String getFormattedCreatedAt() {
        if (this.createdAt == null) {
            return "";
        }
        return this.createdAt.format(OFFSET_DT_FORMATTER);
    }

    public String getFormattedUpdatedAt() {
        if (this.updatedAt == null) {
            return "";
        }
        return this.updatedAt.format(OFFSET_DT_FORMATTER);
    }
}
