package ru.vsu.projectmanagement.jdbc;

import ru.vsu.projectmanagement.connection.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Executor {
    private final ConnectionProvider connectionProvider;

    public Executor(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    // For INSERT operations that need to return generated keys (like ID)
    public <R> R executeInsert(String sql, ResultSetMapper<R> generatedKeyMapper, Object... params) throws SQLException {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, params);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeyMapper.extract(generatedKeys);
                } else {
                    throw new SQLException("Creating record failed, no ID obtained.");
                }
            }
        }
    }

    public long executeInsertAndGetId(String sql, Object... params) throws SQLException {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, params);
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // Assuming ID is the first column
                } else {
                    throw new SQLException("Creating record failed, no ID obtained.");
                }
            }
        }
    }


    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            return ps.executeUpdate();
        }
    }

    public <T> List<T> executeQuery(String sql, ResultSetMapper<T> mapper, Object... params) throws SQLException {
        List<T> result = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapper.extract(rs));
                }
            }
        }
        return result;
    }

    public <T> T executeSingleResult(String sql, ResultSetMapper<T> mapper, Object... params) throws SQLException {
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapper.extract(rs);
                }
                return null; // Or throw NotFoundException
            }
        }
    }

    private void setParams(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof Enum) {
                try {
                    ps.setObject(i + 1, param.toString());
                } catch (Exception e) {
                    ps.setObject(i + 1, param);
                }

            } else if (param instanceof java.time.LocalDate) {
                ps.setObject(i + 1, java.sql.Date.valueOf((java.time.LocalDate) param));
            } else if (param instanceof java.time.LocalDateTime) {
                ps.setObject(i + 1, java.sql.Timestamp.valueOf((java.time.LocalDateTime) param));
            } else if (param instanceof java.time.OffsetDateTime) {
                ps.setObject(i + 1, java.sql.Timestamp.from(((java.time.OffsetDateTime)param).toInstant()));
            }
            else {
                ps.setObject(i + 1, param);
            }
        }
    }
}
