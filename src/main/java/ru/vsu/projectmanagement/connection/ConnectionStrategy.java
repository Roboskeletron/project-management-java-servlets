package ru.vsu.projectmanagement.connection;

import ru.vsu.projectmanagement.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionStrategy {
    Connection createConnection(DatabaseConfig config) throws SQLException;
}
