package com.example.final_project.controller;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.controller.factory.CommandFactory;

import com.example.final_project.controller.session.exceptions.NoAccessProvidedException;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/controller")
public class FrontController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {


        CommandFactory factory = new CommandFactory();
        Command command = factory.defineCommand(request, response);


        if(!command.securityCheck(request, response)) throw new NoAccessProvidedException();
        command.execute(request, response);

    }
}
