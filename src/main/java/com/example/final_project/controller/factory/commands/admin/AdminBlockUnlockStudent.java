package com.example.final_project.controller.factory.commands.admin;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


public class AdminBlockUnlockStudent implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        String login = request.getParameter("login");
        String toDo = request.getParameter("do");
        UserService userService = new UserService(connectionPool);

        ArrayList<String> blockedUsers = (ArrayList<String>) request.getServletContext().getAttribute("blockedUsers");
        if (toDo != null && toDo.compareTo("block") == 0) {
            blockedUsers.add(login);
        }
        else {
            blockedUsers.remove(login);
        }

        userService.blockOrUnlockStudent(toDo,login);
        response.sendRedirect("/project/controller?command=adminUsers");
    }

}
