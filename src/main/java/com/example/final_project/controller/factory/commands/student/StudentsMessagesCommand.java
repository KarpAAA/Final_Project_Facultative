package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.MessagesService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class StudentsMessagesCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");

        request.setAttribute("pageToInclude", "/client/userMessages.jsp");
        request.getSession().setAttribute("messagesList",
                new MessagesService(connectionPool).getUserMessages(userDTO));
        request.setAttribute("servlet", "messages");

        RequestDispatcher view
                = request.getRequestDispatcher("/client/clientPage.jsp");
        view.forward(request, response);
    }
}
