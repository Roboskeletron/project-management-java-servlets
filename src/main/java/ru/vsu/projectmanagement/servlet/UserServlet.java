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

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        var repo = new JDBCUserRepository();
        userService = new UserService(repo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sid = req.getParameter("id");
        try {
            int id = Integer.parseInt(sid);
            var user = userService.getUserById(id);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/views/user.jsp")
                    .forward(req, resp);
        }
        catch (EntityNotFoundException e) {
            req.getRequestDispatcher("/WEB-INF/views/user.jsp")
                    .forward(req, resp);
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
