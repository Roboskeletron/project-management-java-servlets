package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import ru.vsu.projectmanagement.service.*;

public abstract class BaseServlet extends HttpServlet {
    protected UserService userService;
    protected ProjectService projectService;
    protected TaskService taskService;
    protected CommentService commentService;
    protected TaskHistoryService taskHistoryService;

    @Override
    public void init() throws ServletException {
        super.init();
        ServletContext ctx = getServletContext();
        this.userService = (UserService) ctx.getAttribute("userService");
        this.projectService = (ProjectService) ctx.getAttribute("projectService");
        this.taskService = (TaskService) ctx.getAttribute("taskService");
        this.commentService = (CommentService) ctx.getAttribute("commentService");
        this.taskHistoryService = (TaskHistoryService) ctx.getAttribute("taskHistoryService");

        if (this.userService == null || this.projectService == null || this.taskService == null ||
                this.commentService == null || this.taskHistoryService == null) {
            throw new ServletException("One or more services not initialized in BaseServlet. Check AppContextListener.");
        }
    }
}
