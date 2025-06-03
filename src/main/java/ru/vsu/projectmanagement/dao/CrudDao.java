package ru.vsu.projectmanagement.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudDao<T, ID> {
    Optional<T> findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
    T create(T entity) throws SQLException; // Modified to return created entity with ID
    void update(T entity) throws SQLException;
    void delete(ID id) throws SQLException;
}

