package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.projectmanagement.domain.User;
import ru.vsu.projectmanagement.domain.UserRole;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/register")
public class RegistrationServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        UserRole role = UserRole.USER;

        try {
            Optional<User> newUserOpt = userService.registerUser(username, password, email, fullName, role);
            if (newUserOpt.isPresent()) {
                response.sendRedirect(request.getContextPath() + "/login?registrationSuccess=true");
            } else {
                request.setAttribute("error", "Пользователь с таким логином или email уже существует.");
                request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Ошибка регистрации: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp").forward(request, response);
            // For production, log e and show a generic error
            // throw new ServletException("Database error during registration", e);
        }
    }
}
