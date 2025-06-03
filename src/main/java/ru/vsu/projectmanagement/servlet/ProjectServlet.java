package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.projectmanagement.domain.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/projects", "/project/create", "/project/edit", "/project/view", "/project/delete"})
public class ProjectServlet extends BaseServlet {

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

        String path = request.getServletPath();
        try {
            if ("/projects".equals(path)) {
                List<Project> projects = projectService.findAllProjects(); // Or filter by user, e.g. owned or member
                request.setAttribute("projects", projects);
                request.getRequestDispatcher("/WEB-INF/jsp/project/projectList.jsp").forward(request, response);
            } else if ("/project/create".equals(path)) {
                request.setAttribute("project", new Project()); // For form binding
                request.setAttribute("isEditMode", false);
                request.getRequestDispatcher("/WEB-INF/jsp/project/projectForm.jsp").forward(request, response);
            } else if ("/project/edit".equals(path)) {
                handleEditGet(request, response, currentUser);
            } else if ("/project/view".equals(path)) {
                handleViewGet(request, response, currentUser);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in ProjectServlet GET", e);
        }
    }

    private void handleEditGet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long projectId = Long.parseLong(request.getParameter("id"));
        Optional<Project> projectOpt = projectService.findProjectById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            if (project.getOwnerId() == currentUser.getId() || currentUser.getRole() == UserRole.ADMIN) {
                request.setAttribute("project", project);
                request.setAttribute("isEditMode", true);
                request.getRequestDispatcher("/WEB-INF/jsp/project/projectForm.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет прав для редактирования этого проекта.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Проект не найден.");
        }
    }

    private void handleViewGet(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long projectId = Long.parseLong(request.getParameter("id"));
        Optional<Project> projectOpt = projectService.findProjectById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            List<Task> tasks = taskService.findTasksByProjectId(projectId);
            List<User> users = userService.findAllUsers(); // For assignee dropdown

            request.setAttribute("project", project);
            request.setAttribute("tasks", tasks);
            request.setAttribute("users", users);
            request.setAttribute("taskPriorities", TaskPriority.values());
            request.setAttribute("taskStatuses", TaskStatus.values());
            request.getRequestDispatcher("/WEB-INF/jsp/project/projectDetails.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Проект не найден.");
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

        request.setCharacterEncoding("UTF-8"); // For correct parameter encoding

        String path = request.getServletPath();
        try {
            if ("/project/create".equals(path)) {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                projectService.createProject(name, description, currentUser);
                response.sendRedirect(request.getContextPath() + "/projects");
            } else if ("/project/edit".equals(path)) {
                handleEditPost(request, response, currentUser);
            } else if ("/project/delete".equals(path)) {
                handleDeletePost(request, response, currentUser);
            }else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error in ProjectServlet POST", e);
        }
    }

    private void handleEditPost(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long projectId = Long.parseLong(request.getParameter("id"));
        Optional<Project> projectOpt = projectService.findProjectById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            if (project.getOwnerId() != currentUser.getId() && currentUser.getRole() != UserRole.ADMIN) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет прав для редактирования этого проекта.");
                return;
            }
            project.setName(request.getParameter("name"));
            project.setDescription(request.getParameter("description"));
            projectService.updateProject(project);
            response.sendRedirect(request.getContextPath() + "/projects");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Проект не найден.");
        }
    }

    private void handleDeletePost(HttpServletRequest request, HttpServletResponse response, User currentUser) throws ServletException, IOException, SQLException {
        long projectId = Long.parseLong(request.getParameter("id"));
        Optional<Project> projectOpt = projectService.findProjectById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            if (project.getOwnerId() == currentUser.getId() || currentUser.getRole() == UserRole.ADMIN) {
                projectService.deleteProject(projectId);
                response.sendRedirect(request.getContextPath() + "/projects?deleted=true");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет прав для удаления этого проекта.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Проект не найден.");
        }
    }
}