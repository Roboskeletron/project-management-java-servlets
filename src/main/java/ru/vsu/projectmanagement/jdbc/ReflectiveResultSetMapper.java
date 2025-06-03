package ru.vsu.projectmanagement.jdbc;

import ru.vsu.projectmanagement.mapper.ReflectiveMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReflectiveResultSetMapper<T> implements ResultSetMapper<T> {
    private final Class<T> clazz;

    public ReflectiveResultSetMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T extract(ResultSet rs) throws SQLException {
        return ReflectiveMapper.map(rs, clazz);
    }
}
