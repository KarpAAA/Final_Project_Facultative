package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.MessageDao;
import com.example.final_project.entities.message.Message;
import com.example.final_project.entities.user.User;
import com.example.final_project.services.MessagesService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;



public class StudentsMessagesCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        User user = (User) request.getSession().getAttribute("user");

        request.setAttribute("pageToInclude", "/client/userMessages.jsp");
        request.setAttribute("messagesList", new MessagesService(connectionPool).getUserMessages(user));
        request.setAttribute("servlet", "messages");

        RequestDispatcher view
                = request.getRequestDispatcher("/client/clientPage.jsp");
        view.forward(request, response);
    }
}
