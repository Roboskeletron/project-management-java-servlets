package ru.vsu.projectmanagement.domain;

public enum UserRole {
    ADMIN("ADMIN"),
    PROJECT_MANAGER("PROJECT_MANAGER"), // Example additional role
    USER("USER");

    private final String dbValue;

    UserRole(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static UserRole fromDbValue(String dbValue) {
        if (dbValue == null) return null;
        for (UserRole role : values()) {
            if (role.dbValue.equalsIgnoreCase(dbValue)) {
                return role;
            }
        }
        // Fallback to valueOf if direct match fails (e.g. if dbValue is same as enum constant name)
        try {
            return UserRole.valueOf(dbValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown role: " + dbValue);
        }
    }

    @Override
    public String toString() { // Important for Executor setParams
        return dbValue;
    }
}