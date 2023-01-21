package com.example.final_project.controller.factory.commands.auth;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LoggingCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }


    private void executeGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        executePost(req, resp);
    }

    private void executePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) req.getSession().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        UserDTO userDTO = userService.identifyUser(req.getParameter("login"), req.getParameter("pwd"));
        if (userDTO == null || userDTO.getBlocked_state() == Blocked_State.BLOCKED) {
            req.setAttribute("login", req.getParameter("login"));
            req.setAttribute("password", req.getParameter("pwd"));
            req.getRequestDispatcher("/auth/logging.jsp").forward(req, resp);
        }
        else{
            req.getSession().setAttribute("user", userDTO);
            resp.sendRedirect("/project/controller?command=users");
        }
    }


}

