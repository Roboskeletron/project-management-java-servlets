package ru.vsu.projectmanagement.repository;

import ru.vsu.projectmanagement.domain.User;

import java.sql.*;
import java.util.Optional;

public class JDBCUserRepository extends BasicRepository implements UserRepository {
    @Override
    public User create(User user) throws SQLException {
        var sql = "insert into users (username, password, email) values (?, ?, ?) returning id";

        try (var connection = getConnection();
             var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert user, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                    return user;
                }

                throw new SQLException("Failed to insert user, generated key not found");
            }
        }
    }

    @Override
    public void update(User user) throws SQLException {
        var sql = "update users set username = ?, password = ?, email = ? where id = ?";

        try (var connection = getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to update user, no rows affected");
            }
        }
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        var sql = "select id, username, email, password from users where username = ?";

        try (var connection = getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            var rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        var sql = "select id, username, email, password from users where email = ?";

        try (var connection = getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            var rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
    }

    @Override
    public Optional<User> findById(int id) throws SQLException {
        var sql = "select id, username, email, password from users where id = ?";

        try (var connection = getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            var rs = stmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
    }
}
