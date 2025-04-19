package ru.vsu.projectmanagement.domain;

public class Project {
    private int id;
    private String name;
    private String description;
    private int ownerId;
    private String prefix;

    public Project() {}

    public Project(int id, String name, String description, int ownerId, String prefix) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.prefix = prefix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
