package ru.vsu.projectmanagement.dao;

import ru.vsu.projectmanagement.domain.User;
import ru.vsu.projectmanagement.domain.UserRole;
import ru.vsu.projectmanagement.jdbc.Executor;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

public class UserDao extends AbstractCrudDao<User, Long> {

    public UserDao(Executor executor) {
        super(executor, User.class);
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    public User create(User user) throws SQLException {
        final String sql = "INSERT INTO users(username, password_hash, email, full_name, role, is_active, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";

        long id = executor.executeInsertAndGetId(sql,
                user.getUsername(),
                user.getPasswordHash(),
                user.getEmail(),
                user.getFullName(),
                user.getRole() != null ? user.getRole().getDbValue() : UserRole.USER.getDbValue(),
                user.isActive(),
                OffsetDateTime.now()
        );
        user.setId(id);
        return findById(id).orElseThrow(() -> new SQLException("Failed to create user, not found after insert."));
    }

    @Override
    public void update(User user) throws SQLException {
        final String sql = "UPDATE users SET username = ?, password_hash = ?, email = ?, full_name = ?, " +
                "role = ?, is_active = ? WHERE id = ?";
        executor.executeUpdate(sql,
                user.getUsername(),
                user.getPasswordHash(),
                user.getEmail(),
                user.getFullName(),
                user.getRole() != null ? user.getRole().getDbValue() : UserRole.USER.getDbValue(),
                user.isActive(),
                user.getId());
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE username = ?";
        User user = executor.executeSingleResult(sql, mapper, username);
        return Optional.ofNullable(user);
    }
}
