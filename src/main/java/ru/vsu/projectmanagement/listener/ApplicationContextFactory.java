package ru.vsu.projectmanagement.listener;


import ru.vsu.projectmanagement.config.DatabaseConfig;
import ru.vsu.projectmanagement.connection.ConnectionProvider;
import ru.vsu.projectmanagement.connection.DatabaseConnection;
import ru.vsu.projectmanagement.connection.DriverManagerStrategy;
import ru.vsu.projectmanagement.dao.*;
import ru.vsu.projectmanagement.jdbc.Executor;
import ru.vsu.projectmanagement.service.*;

public class ApplicationContextFactory {
    public static ApplicationContext create() {
        String url = System.getenv("JDBC_URL");
        String user = System.getenv("JDBC_USER");
        String password = System.getenv("JDBC_PASSWORD");

        if (url == null || user == null || password == null) {
            System.err.println("Warning: JDBC environment variables not set. Using defaults for H2 or local PG.");
            throw new IllegalStateException("JDBC_URL, JDBC_USER, JDBC_PASSWORD environment variables must be set.");
        }


        DatabaseConfig dbConfig = new DatabaseConfig(url, user, password);
        DriverManagerStrategy strategy = new DriverManagerStrategy();
        String driverClassName = "org.postgresql.Driver";

        ConnectionProvider connectionProvider = new DatabaseConnection(dbConfig, strategy, driverClassName);
        Executor executor = new Executor(connectionProvider);

        // DAOs
        UserDao userDao = new UserDao(executor);
        ProjectDao projectDao = new ProjectDao(executor);
        TaskDao taskDao = new TaskDao(executor);
        CommentDao commentDao = new CommentDao(executor);
        TaskHistoryDao taskHistoryDao = new TaskHistoryDao(executor);

        // Services
        UserService userService = new UserService(userDao);
        TaskHistoryService taskHistoryService = new TaskHistoryService(taskHistoryDao); // History service first
        ProjectService projectService = new ProjectService(projectDao);
        TaskService taskService = new TaskService(taskDao, taskHistoryService); // Task service needs history
        CommentService commentService = new CommentService(commentDao);

        return new ApplicationContext(userService, projectService, taskService, commentService, taskHistoryService);
    }
}

