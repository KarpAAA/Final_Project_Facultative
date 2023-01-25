package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;



public class TeacherDetailedCourseCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        String courseTitle = request.getParameter("title");

        CourseService courseService = new CourseService(connectionPool);
        UserService userService = new UserService(connectionPool);

        CourseDTO courseDTO = courseService.getCourseByTitle(courseTitle);
        Map<Integer, List<UserDTO>> userMap = userService.getUsersMarksMap(courseDTO);

        request.setAttribute("course", courseDTO);
        request.getSession().setAttribute("students", userMap);
        request.setAttribute("pageToInclude", "/teacher/detailedCourse.jsp");


        request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request, response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        String courseTitle = request.getParameter("title");
        UserService userService = new UserService(connectionPool);
        if(request.getParameter("action") != null && request.getParameter("action").compareTo("saveChanges") == 0){
            int amount = Integer.parseInt(request.getParameter("amount"));
            int[] newMarks= new int[amount];
            for(int i = 0 ; i < amount ;++i){
                newMarks[i] = Integer.parseInt(request.getParameter("mark" + i));
            }

            userService.saveMarks((Map<Integer, List<UserDTO>>) request.getSession().getAttribute("students"),courseTitle, newMarks);
            request.getSession().removeAttribute("students");
        }
        executeGet(request, resp);
    }

}
