package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * Command of teacher role
 * Using to show courses related to teacher
 */
public class TeacherCoursesCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        CourseService courseService = new CourseService(connectionPool);
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");

        int page = 1;
        int recordsPerPage = 9;
        if (request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));

        List<CourseDTO> teacherCourses =
                courseService.getAllTeacherCourses(userDTO, (page - 1) * recordsPerPage,recordsPerPage);

        int noOfRecords = courseService.getTeacherCoursesAmount();
        int noOfPages = (int)Math.ceil(noOfRecords * 1.0 / recordsPerPage);


        request.setAttribute("amountOfPages", noOfPages);
        request.setAttribute("currentPage", page);

        request.setAttribute("pageToInclude", "/teacher/teacherCourses.jsp");
        request.setAttribute("myCourses", teacherCourses);
        request.setAttribute("servlet", "teacherCourses");

        request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request, response);

    }

}
