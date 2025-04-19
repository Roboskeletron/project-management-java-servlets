package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.projectmanagement.repository.JDBCUserRepository;
import ru.vsu.projectmanagement.repository.UserRepository;
import ru.vsu.projectmanagement.service.UserService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        UserRepository repo = new JDBCUserRepository();
        userService = new UserService(repo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");


        try {
            int userId = userService.registerUser(username, email, password);
            HttpSession session = req.getSession();
            session.setAttribute("userId", userId);
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
