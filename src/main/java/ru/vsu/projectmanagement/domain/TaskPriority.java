package ru.vsu.projectmanagement.domain;

public enum TaskPriority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    URGENT("URGENT");

    private final String dbValue;

    TaskPriority(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static TaskPriority fromDbValue(String dbValue) {
        if (dbValue == null) return null;
        for (TaskPriority priority : values()) {
            if (priority.dbValue.equalsIgnoreCase(dbValue)) {
                return priority;
            }
        }
        try {
            return TaskPriority.valueOf(dbValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown task priority: " + dbValue);
        }
    }
    @Override
    public String toString() {
        return dbValue;
    }
}