package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StudentFillBalance implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");



        userService.updateBalance(user,Integer.parseInt(request.getParameter("sum")));
        request.getSession().removeAttribute("user");
        request.getSession().setAttribute("user",userService.findUser(user.getLogin()));
        response.sendRedirect("/project/controller?command=settings");
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
