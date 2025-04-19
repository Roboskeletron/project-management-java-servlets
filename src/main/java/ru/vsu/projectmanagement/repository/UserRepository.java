package ru.vsu.projectmanagement.repository;

import ru.vsu.projectmanagement.domain.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    User create(User user) throws SQLException;
    void update(User user) throws SQLException;
    Optional<User> findByUsername(String username) throws SQLException;
    Optional<User> findByEmail(String email) throws SQLException;
    Optional<User> findById(int id) throws SQLException;
}
