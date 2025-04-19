package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.projectmanagement.model.UpdateUserRequest;
import ru.vsu.projectmanagement.repository.JDBCUserRepository;
import ru.vsu.projectmanagement.repository.UserRepository;
import ru.vsu.projectmanagement.service.UserService;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private UserService userService;
    private UserRepository repo;

    @Override
    public void init() {
        repo = new JDBCUserRepository();
        userService = new UserService(repo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            req.setAttribute("user", userService.getUserById(userId));
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp")
                    .forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String newUsername = req.getParameter("username");
        String newPassword = req.getParameter("password");

        var update = new UpdateUserRequest(
                userId,
                Optional.ofNullable(newUsername).filter(s -> !s.isBlank()),
                Optional.ofNullable(newPassword).filter(s -> !s.isBlank())
        );

        try {
            userService.updateUser(update);
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
