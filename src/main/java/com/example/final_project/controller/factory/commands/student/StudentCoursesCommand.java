package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.entities.course.Course;
import com.example.final_project.entities.course.State;
import com.example.final_project.entities.user.User;
import com.example.final_project.services.CourseService;
import com.example.final_project.utilities.CoursesFilter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class StudentCoursesCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        CourseService courseService = new CourseService(connectionPool);

        User user = (User) request.getSession().getAttribute("user");
        State state = new CourseService(connectionPool).validateCourseState(request.getParameter("courseState"));


        int page = 1;
        int recordsPerPage = 9;
        if (request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));


        List<Course> coursesList =  courseService.selectStatedAmountOfUserCourses(user, (page - 1) * recordsPerPage, recordsPerPage, state);
        int noOfRecords = courseService.getCoursesAmount();
        int noOfPages = (int)Math.ceil(noOfRecords * 1.0
                / recordsPerPage);


        request.setAttribute("amountOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageToInclude", "/client/myCourses.jsp");

        request.setAttribute("myCourses", coursesList);
        request.setAttribute("amountOfPages", noOfPages);
        request.setAttribute("servlet", "myCourses");

        RequestDispatcher view
                = request.getRequestDispatcher("/client/clientPage.jsp");
        view.forward(request, response);
    }

}
