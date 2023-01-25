package com.example.final_project.controller.factory.commands.defaulT;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class UserDeleteAccount implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        if (request.getParameter("teacher") != null) {
            userService.deleteUser(userService.findUser(request.getParameter("teacher")));
            response.sendRedirect("/project/controller?command=adminUsers&action=teachers");
            ArrayList<String> blockedUsers = (ArrayList<String>) request.getServletContext().getAttribute("blockedUsers");
            blockedUsers.add(request.getParameter("teacher"));


        } else {
            userService.deleteUser((UserDTO) request.getSession().getAttribute("user"));
            response.sendRedirect("/project/controller?command=default");
        }

    }

}
