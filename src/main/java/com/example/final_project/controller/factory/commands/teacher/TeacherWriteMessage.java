package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.MessagesService;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TeacherWriteMessage implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) req.getServletContext().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);

        UserDTO receiver = userService.findUser(req.getParameter("receiver"));
        String subject = req.getParameter("subject");
        String text = req.getParameter("text");

        if(receiver==null || subject==null || text==null){
            req.setAttribute("errorSending", "Not all necessary fields entered or user doesnt exist!");
            executeGet(req, response);
            return;
        }

        MessagesService messagesService = new MessagesService(connectionPool);
        messagesService.sendMessage(receiver,(UserDTO)req.getSession().getAttribute("user"),subject,text);

        response.sendRedirect("/project/controller?command=teacherWriteMessage");

    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageToInclude", "/teacher/writeMessage.jsp");
        request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request, response);
    }

}

