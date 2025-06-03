package ru.vsu.projectmanagement.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        try {
            ApplicationContext appContext = ApplicationContextFactory.create();
            ctx.setAttribute("userService", appContext.userService());
            ctx.setAttribute("projectService", appContext.projectService());
            ctx.setAttribute("taskService", appContext.taskService());
            ctx.setAttribute("commentService", appContext.commentService());
            ctx.setAttribute("taskHistoryService", appContext.taskHistoryService());

            // For JSPs to access enum values easily
            ctx.setAttribute("UserRole", ru.vsu.projectmanagement.domain.UserRole.class);
            ctx.setAttribute("TaskStatus", ru.vsu.projectmanagement.domain.TaskStatus.class);
            ctx.setAttribute("TaskPriority", ru.vsu.projectmanagement.domain.TaskPriority.class);

            ctx.log("AppContextListener initialization successful: TaskTracker services loaded.");
        } catch (Exception ex) {
            ctx.log("Error during AppContextListener initialization for TaskTracker", ex);
            throw new RuntimeException("TaskTracker Listener initialization failed", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        ctx.log("AppContextListener destroyed for TaskTracker.");
    }
}
