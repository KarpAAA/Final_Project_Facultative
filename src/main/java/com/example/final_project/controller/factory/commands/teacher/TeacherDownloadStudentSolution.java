package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.task.Task;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.TaskDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.TaskService;
import com.example.final_project.services.UserService;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class TeacherDownloadStudentSolution implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        TaskService taskService = new TaskService(connectionPool);
        TaskDTO taskDTO = taskService.findTask(Integer.valueOf(request.getParameter("task")));
        UserDTO user = new UserService(connectionPool).findUser(request.getParameter("user"));


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String filename = user.getLogin() + "_" + taskDTO.getTitle() + "_solution.pdf";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");


        FileOutputStream outputStream = new FileOutputStream("downloadFile.txt");
        byte[] file = taskService.getUserSolution(user, taskDTO);
        if (file != null) {
            outputStream.write(file);
            outputStream.close();
            FileInputStream fileInputStream = new FileInputStream("downloadFile.txt");
            int i;
            while ((i = fileInputStream.read()) != -1) {
                out.write(i);
            }
            fileInputStream.close();
            out.close();
        } else {
            response.sendRedirect("/project/controller?command=teacherDetailedCourse&title="
                    + taskDTO.getCourse().getTitle());
        }

    }

}