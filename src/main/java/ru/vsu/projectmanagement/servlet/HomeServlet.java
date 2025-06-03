package ru.vsu.projectmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.projectmanagement.domain.User;

import java.io.IOException;

@WebServlet("")
public class HomeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Don't create if not exists
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser != null) {
            response.sendRedirect(request.getContextPath() + "/projects"); // User logged in, go to projects
        } else {
            response.sendRedirect(request.getContextPath() + "/login"); // Not logged in, go to login
        }
    }
}
