package ru.vsu.projectmanagement.domain;

public enum TaskStatus {
    TODO("TODO"),
    IN_PROGRESS("IN_PROGRESS"),
    REVIEW("REVIEW"),
    DONE("DONE"),
    CANCELLED("CANCELLED");

    private final String dbValue;

    TaskStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static TaskStatus fromDbValue(String dbValue) {
        if (dbValue == null) return null;
        for (TaskStatus status : values()) {
            if (status.dbValue.equalsIgnoreCase(dbValue)) {
                return status;
            }
        }
        try {
            return TaskStatus.valueOf(dbValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown task status: " + dbValue);
        }
    }
    @Override
    public String toString() {
        return dbValue;
    }
}