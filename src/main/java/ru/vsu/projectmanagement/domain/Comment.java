package ru.vsu.projectmanagement.domain;

import java.time.OffsetDateTime;

public class Comment {
    private int id;
    private String text;
    private int userId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Comment(int id, String text, int userId, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
