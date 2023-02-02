package com.example.final_project.controller.factory.commands.defaulT;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.MeetingsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CalendarCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");
        MeetingsService meetingsService =
                new MeetingsService((ConnectionPool) request.getServletContext().getAttribute("connectionPool"));

        var list = meetingsService.getUserMeetings(userDTO);
        request.getSession().setAttribute("meetings", list);
        request.setAttribute("pageToInclude", "/default/calendar.jsp");
        if(userDTO.getRole() == Role.Student)request.getRequestDispatcher("/client/clientPage.jsp").forward(request, response);
        else request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request,response);
    }

}