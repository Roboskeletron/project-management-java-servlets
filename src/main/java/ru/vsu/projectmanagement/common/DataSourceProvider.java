package ru.vsu.projectmanagement.common;

import org.postgresql.ds.PGConnectionPoolDataSource;

import javax.sql.ConnectionPoolDataSource;

public class DataSourceProvider {
    private static final PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();

    static {
        ds.setURL("jdbc:postgresql://localhost:5432/project-management");
        ds.setUser("postgres");
        ds.setPassword("postgres");
    }

    public static ConnectionPoolDataSource getDataSource() {
        return ds;
    }
}
