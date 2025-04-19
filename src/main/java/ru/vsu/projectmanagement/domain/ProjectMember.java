package ru.vsu.projectmanagement.domain;

public class ProjectMember {
    private final int userId;
    private final int projectId;
    private Role role;

    public ProjectMember(int userId, int projectId, Role role) {
        this.userId = userId;
        this.projectId = projectId;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
