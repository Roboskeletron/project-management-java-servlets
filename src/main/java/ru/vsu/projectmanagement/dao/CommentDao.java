package ru.vsu.projectmanagement.dao;

import ru.vsu.projectmanagement.domain.Comment;
import ru.vsu.projectmanagement.jdbc.Executor;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public class CommentDao extends AbstractCrudDao<Comment, Long> {

    public CommentDao(Executor executor) {
        super(executor, Comment.class);
    }

    @Override
    protected String getTableName() {
        return "comments";
    }

    @Override
    public Comment create(Comment comment) throws SQLException {
        final String sql = "INSERT INTO comments(task_id, user_id, content, created_at) " +
                "VALUES (?, ?, ?, ?) RETURNING id, created_at";
        long id = executor.executeInsertAndGetId(sql,
                comment.getTaskId(),
                comment.getUserId(),
                comment.getContent(),
                OffsetDateTime.now()
        );
        comment.setId(id);
        return findById(id).orElseThrow(() -> new SQLException("Failed to create comment."));
    }

    @Override
    public void update(Comment comment) throws SQLException {
        // Comments are typically not updated, but if needed:
        final String sql = "UPDATE comments SET content = ? WHERE id = ?";
        executor.executeUpdate(sql, comment.getContent(), comment.getId());
    }

    public List<Comment> findByTaskId(Long taskId) throws SQLException {
        String sql = "SELECT c.*, u.username as username FROM " + getTableName() +
                " c JOIN users u ON c.user_id = u.id " +
                " WHERE c.task_id = ? ORDER BY c.created_at ASC";
        return executor.executeQuery(sql, mapper, taskId);
    }
}
