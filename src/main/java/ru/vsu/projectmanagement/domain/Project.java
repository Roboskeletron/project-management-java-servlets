package ru.vsu.projectmanagement.domain;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Project {
    private long id;
    private String name;
    private String description;
    private long ownerId; // Foreign key to User
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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

    public String getFormattedCreatedAt() {
        if (this.createdAt == null) {
            return "";
        }
        return this.createdAt.format(DT_FORMATTER);
    }

    public String getFormattedUpdatedAt() {
        if (this.updatedAt == null) {
            return "";
        }
        return this.updatedAt.format(DT_FORMATTER);
    }
}
