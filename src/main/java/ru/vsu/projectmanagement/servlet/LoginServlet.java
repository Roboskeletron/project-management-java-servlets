package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.projectmanagement.domain.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("logoutSuccess") != null) {
            request.setAttribute("message", "Вы успешно вышли из системы.");
        }
        if (request.getParameter("registrationSuccess") != null) {
            request.setAttribute("message", "Регистрация прошла успешно! Теперь вы можете войти.");
        }
        request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Optional<User> userOpt = userService.login(username, password);
            if (userOpt.isPresent()) {
                HttpSession session = request.getSession(true); // Create session
                session.setAttribute("currentUser", userOpt.get());

                String intendedUrl = (String) session.getAttribute("intendedUrl");
                if (intendedUrl != null && !intendedUrl.isEmpty()) {
                    session.removeAttribute("intendedUrl");
                    response.sendRedirect(intendedUrl);
                } else {
                    response.sendRedirect(request.getContextPath() + "/projects"); // Default redirect
                }
            } else {
                request.setAttribute("error", "Неверный логин или пароль.");
                request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Ошибка входа: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp").forward(request, response);
        }
    }
}
