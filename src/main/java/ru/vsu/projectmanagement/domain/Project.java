package ru.vsu.projectmanagement.domain;

import java.time.OffsetDateTime;

public class Project {
    private long id;
    private String name;
    private String description;
    private long ownerId; // Foreign key to User
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Project() {
    }

    // Constructor, getters, and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getOwnerId() { return ownerId; }
    public void setOwnerId(long ownerId) { this.ownerId = ownerId; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
