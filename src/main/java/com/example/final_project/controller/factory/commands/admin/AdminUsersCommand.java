package com.example.final_project.controller.factory.commands.admin;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.entities.user.Role;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class AdminUsersCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        if(request.getParameter("action")!=null
        && request.getParameter("action").compareToIgnoreCase("teachers") == 0){
            request.setAttribute("map", userService.getUserToCourseMapByRole(Role.Teacher));
            request.setAttribute("users", "teachers");
        }
        else{
            request.setAttribute("map", userService.getUserToCourseMapByRole(Role.Student));
            request.setAttribute("users", "students");
        }


        request.setAttribute("pageToInclude", "/admin/usersAdmin.jsp");
        request.getRequestDispatcher("/admin/adminPage.jsp").forward(request, response);
    }

}

