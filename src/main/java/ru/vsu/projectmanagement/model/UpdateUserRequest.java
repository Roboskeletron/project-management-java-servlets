package ru.vsu.projectmanagement.model;

import java.util.Optional;

public record UpdateUserRequest(int id, Optional<String> username, Optional<String> password) {
}
