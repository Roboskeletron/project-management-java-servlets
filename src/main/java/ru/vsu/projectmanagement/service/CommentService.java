package ru.vsu.projectmanagement.service;

import ru.vsu.projectmanagement.dao.CommentDao;
import ru.vsu.projectmanagement.domain.Comment;
import ru.vsu.projectmanagement.domain.User;
import ru.vsu.projectmanagement.domain.UserRole;

import java.sql.SQLException;
import java.util.List;

public class CommentService {
    private final CommentDao commentDao;

    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public Comment addComment(Long taskId, User user, String content) throws SQLException {
        Comment comment = new Comment();
        comment.setTaskId(taskId);
        comment.setUserId(user.getId());
        comment.setContent(content);
        // createdAt set by DAO
        return commentDao.create(comment);
    }

    public List<Comment> getCommentsForTask(Long taskId) throws SQLException {
        return commentDao.findByTaskId(taskId);
    }

    public void deleteComment(Long commentId, User user) throws SQLException {
        // Add authorization: only comment owner or admin/project manager can delete
        java.util.Optional<Comment> commentOpt = commentDao.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            // Basic check: user who created comment or an admin
            if (comment.getUserId() == user.getId() || user.getRole() == UserRole.ADMIN) {
                commentDao.delete(commentId);
            } else {
                // Or throw an exception / return false
                System.err.println("User " + user.getUsername() + " not authorized to delete comment " + commentId);
            }
        }
    }
}
