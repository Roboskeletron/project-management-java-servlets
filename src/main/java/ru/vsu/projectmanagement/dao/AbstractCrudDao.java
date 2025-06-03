package ru.vsu.projectmanagement.dao;

import ru.vsu.projectmanagement.jdbc.Executor;
import ru.vsu.projectmanagement.jdbc.ReflectiveResultSetMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudDao<T, ID> implements CrudDao<T, ID> {
    protected final Executor executor;
    protected final ReflectiveResultSetMapper<T> mapper;
    private final Class<T> entityClass;


    public AbstractCrudDao(Executor executor, Class<T> entityClass) {
        this.executor = executor;
        this.entityClass = entityClass;
        this.mapper = new ReflectiveResultSetMapper<>(entityClass);
    }

    @Override
    public Optional<T> findById(ID id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        T entity = executor.executeSingleResult(sql, mapper, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findAll() throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " ORDER BY id";
        return executor.executeQuery(sql, mapper);
    }

    // Create and Update are specific to each entity due to different fields
    @Override
    public abstract T create(T entity) throws SQLException;

    @Override
    public abstract void update(T entity) throws SQLException;

    @Override
    public void delete(ID id) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        executor.executeUpdate(sql, id);
    }

    protected abstract String getTableName();

    protected Class<T> getEntityClass() {
        return entityClass;
    }
}

