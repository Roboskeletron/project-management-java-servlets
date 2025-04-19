package ru.vsu.projectmanagement.repository;

import ru.vsu.projectmanagement.common.DataSourceProvider;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BasicRepository {
    public Connection getConnection() throws SQLException {
        return DataSourceProvider.getDataSource().getPooledConnection().getConnection();
    }
}
