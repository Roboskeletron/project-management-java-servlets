package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.projectmanagement.exception.EntityNotFoundException;
import ru.vsu.projectmanagement.repository.JDBCUserRepository;
import ru.vsu.projectmanagement.service.UserService;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        var userRepository = new JDBCUserRepository();
        userService = new UserService(userRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            var userId = userService.login(email, password);

            var session = req.getSession();
            session.setAttribute("userId", userId);
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
        catch (EntityNotFoundException e){
            req.setAttribute("error", "Неверный email или пароль");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(req, resp);
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
