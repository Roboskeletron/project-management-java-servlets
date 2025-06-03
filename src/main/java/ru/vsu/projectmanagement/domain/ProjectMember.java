package ru.vsu.projectmanagement.domain;

public class ProjectMember {
    private final int userId;
    private final int projectId;
    private UserRole userRole;

    public ProjectMember(int userId, int projectId, UserRole userRole) {
        this.userId = userId;
        this.projectId = projectId;
        this.userRole = userRole;
    }

    public int getUserId() {
        return userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public UserRole getRole() {
        return userRole;
    }

    public void setRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
