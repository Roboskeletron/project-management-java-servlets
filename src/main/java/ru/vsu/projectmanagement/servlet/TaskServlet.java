package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.projectmanagement.domain.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/task/create", "/task/edit", "/task/view", "/task/delete", "/task/comment"})
public class TaskServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            session.setAttribute("intendedUrl", request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();

        try {
            if ("/task/create".equals(path)) {
                handleCreateTaskGet(request, response, currentUser);
            } else if ("/task/edit".equals(path)) {
                handleEditTaskGet(request, response, currentUser);
            } else if ("/task/view".equals(path)) {
                handleViewTaskGet(request, response, currentUser);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in TaskServlet GET", e);
        }
    }

    private void handleCreateTaskGet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long projectId = Long.parseLong(request.getParameter("projectId"));
        request.setAttribute("task", new Task()); // For form binding
        request.setAttribute("projectId", projectId);
        request.setAttribute("isEditMode", false);
        request.setAttribute("users", userService.findAllUsers());
        request.setAttribute("taskPriorities", TaskPriority.values());
        request.setAttribute("taskStatuses", TaskStatus.values());
        request.getRequestDispatcher("/WEB-INF/jsp/task/taskForm.jsp").forward(request, response);
    }

    private void handleEditTaskGet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long taskId = Long.parseLong(request.getParameter("id"));
        Optional<Task> taskOpt = taskService.findTaskById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            // Authorization: reporter, assignee or ADMIN can edit
            if (task.getReporterId() == currentUser.getId() ||
                    (task.getAssigneeId() != null && task.getAssigneeId() == currentUser.getId()) ||
                    currentUser.getRole() == UserRole.ADMIN) {

                request.setAttribute("task", task);
                request.setAttribute("projectId", task.getProjectId()); // Keep projectId for context
                request.setAttribute("isEditMode", true);
                request.setAttribute("users", userService.findAllUsers());
                request.setAttribute("taskPriorities", TaskPriority.values());
                request.setAttribute("taskStatuses", TaskStatus.values());
                request.getRequestDispatcher("/WEB-INF/jsp/task/taskForm.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет прав для редактирования этой задачи.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Задача не найдена.");
        }
    }

    private void handleViewTaskGet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long taskId = Long.parseLong(request.getParameter("id"));
        Optional<Task> taskOpt = taskService.findTaskById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            List<Comment> comments = commentService.getCommentsForTask(taskId);
            List<TaskHistory> history = taskHistoryService.getHistoryForTask(taskId);
            List<User> users = userService.findAllUsers();


            request.setAttribute("task", task);
            request.setAttribute("comments", comments);
            request.setAttribute("history", history);
            request.setAttribute("users", users); // For assignee change
            request.setAttribute("taskPriorities", TaskPriority.values());
            request.setAttribute("taskStatuses", TaskStatus.values());
            request.getRequestDispatcher("/WEB-INF/jsp/task/taskDetails.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Задача не найдена.");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пожалуйста, войдите в систему.");
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();

        try {
            if ("/task/create".equals(path)) {
                handleCreateTaskPost(request, response, currentUser);
            } else if ("/task/edit".equals(path)) {
                handleEditTaskPost(request, response, currentUser);
            } else if ("/task/delete".equals(path)) {
                handleDeleteTaskPost(request, response, currentUser);
            } else if ("/task/comment".equals(path)) {
                handleAddCommentPost(request, response, currentUser);
            }
            else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in TaskServlet POST", e);
        } catch (DateTimeParseException e) {
            // Handle error more gracefully if date format is wrong
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат даты: " + e.getMessage());
        }
    }

    private void handleCreateTaskPost(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long projectId = Long.parseLong(request.getParameter("projectId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        TaskStatus status = TaskStatus.fromDbValue(request.getParameter("status"));
        TaskPriority priority = TaskPriority.fromDbValue(request.getParameter("priority"));

        Long assigneeId = null;
        String assigneeIdStr = request.getParameter("assigneeId");
        if (assigneeIdStr != null && !assigneeIdStr.isEmpty() && !assigneeIdStr.equals("0")) { // "0" or empty for unassigned
            assigneeId = Long.parseLong(assigneeIdStr);
        }

        LocalDate dueDate = null;
        String dueDateStr = request.getParameter("dueDate");
        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            dueDate = LocalDate.parse(dueDateStr); // Assumes yyyy-MM-dd
        }

        taskService.createTask(projectId, title, description, status, priority, assigneeId, currentUser, dueDate);
        response.sendRedirect(request.getContextPath() + "/project/view?id=" + projectId);
    }

    private void handleEditTaskPost(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long taskId = Long.parseLong(request.getParameter("id"));
        Optional<Task> taskOpt = taskService.findTaskById(taskId);

        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            // Authorization check (reporter, assignee, admin)
            if (task.getReporterId() != currentUser.getId() &&
                    (task.getAssigneeId() == null || task.getAssigneeId() != currentUser.getId()) &&
                    currentUser.getRole() != UserRole.ADMIN) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет прав для редактирования этой задачи.");
                return;
            }

            task.setTitle(request.getParameter("title"));
            task.setDescription(request.getParameter("description"));
            task.setStatus(TaskStatus.fromDbValue(request.getParameter("status")));
            task.setPriority(TaskPriority.fromDbValue(request.getParameter("priority")));

            Long assigneeId = null;
            String assigneeIdStr = request.getParameter("assigneeId");
            if (assigneeIdStr != null && !assigneeIdStr.isEmpty() && !assigneeIdStr.equals("0")) {
                assigneeId = Long.parseLong(assigneeIdStr);
            }
            task.setAssigneeId(assigneeId);

            LocalDate dueDate = null;
            String dueDateStr = request.getParameter("dueDate");
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                dueDate = LocalDate.parse(dueDateStr);
            }
            task.setDueDate(dueDate);
            // projectId and reporterId usually don't change via edit form

            taskService.updateTask(task, currentUser);
            response.sendRedirect(request.getContextPath() + "/task/view?id=" + taskId);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Задача не найдена.");
        }
    }

    private void handleDeleteTaskPost(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long taskId = Long.parseLong(request.getParameter("id"));
        Optional<Task> taskOpt = taskService.findTaskById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            // Authorization: reporter or ADMIN
            if (task.getReporterId() == currentUser.getId() || currentUser.getRole() == UserRole.ADMIN) {
                long projectId = task.getProjectId(); // To redirect back to project
                taskService.deleteTask(taskId, currentUser);
                response.sendRedirect(request.getContextPath() + "/project/view?id=" + projectId + "&taskDeleted=true");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет прав для удаления этой задачи.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Задача не найдена.");
        }
    }

    private void handleAddCommentPost(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long taskId = Long.parseLong(request.getParameter("taskId"));
        String content = request.getParameter("content");

        if (content != null && !content.trim().isEmpty()) {
            commentService.addComment(taskId, currentUser, content);
        }
        response.sendRedirect(request.getContextPath() + "/task/view?id=" + taskId);
    }
}
