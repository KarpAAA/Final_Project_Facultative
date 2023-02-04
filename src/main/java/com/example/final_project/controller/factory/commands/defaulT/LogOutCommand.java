package com.example.final_project.controller.factory.commands.defaulT;

import com.example.final_project.controller.factory.commands.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LogOutCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("coursesFilter");
        response.sendRedirect("/project/controller?command=default");
    }

}