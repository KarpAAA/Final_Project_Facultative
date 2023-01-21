package com.example.final_project.controller.factory.commands.admin;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AdminBlockUnlockStudent implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        String login = request.getParameter("login");
        String toDo = request.getParameter("do");
        UserService userService = new UserService(connectionPool);

        userService.blockOrUnlockStudent(toDo,login);

        response.sendRedirect("/project/controller?command=adminUsers");
    }

}
