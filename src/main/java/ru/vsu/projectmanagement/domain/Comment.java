package ru.vsu.projectmanagement.domain;

import java.time.OffsetDateTime;

public class Comment {
    private long id;
    private long taskId;
    private long userId;
    private String content;
    private OffsetDateTime createdAt;

    // Optional: Fields for joined data
    private String username;


    public Comment() {
    }

    // Constructor, getters, and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getTaskId() { return taskId; }
    public void setTaskId(long taskId) { this.taskId = taskId; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
