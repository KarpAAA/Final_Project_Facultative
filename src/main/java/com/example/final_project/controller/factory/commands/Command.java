package com.example.final_project.controller.factory.commands;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command{
        void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
        default boolean securityCheck(HttpServletRequest request, HttpServletResponse response){
                return true;
        }
}