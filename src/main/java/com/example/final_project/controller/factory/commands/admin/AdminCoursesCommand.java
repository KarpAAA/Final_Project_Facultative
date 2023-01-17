package com.example.final_project.controller.factory.commands.admin;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.entities.course.Course;
import com.example.final_project.entities.course.CourseBuilder;
import com.example.final_project.entities.course.State;
import com.example.final_project.entities.user.Role;
import com.example.final_project.entities.user.User;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;
import com.example.final_project.utilities.CoursesFilter;
import com.example.final_project.validation.Validator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



public class AdminCoursesCommand implements Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        User user = (User) request.getSession().getAttribute("user");
        UserService userService = new UserService(connectionPool);
        CourseService courseService = new CourseService(connectionPool);
        int page = 1;
        int recordsPerPage = 9;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }


        CoursesFilter coursesFilter = (CoursesFilter) request.getSession().getAttribute("coursesFilter");
        if (coursesFilter == null) {
            coursesFilter = new CoursesFilter();
        }
        coursesFilter.setOffset((page - 1) * recordsPerPage);
        coursesFilter.setCoursesPerPage(recordsPerPage);



        List<Course> list = courseService.selectCoursesByCondition(coursesFilter);
        int coursesAmount = courseService.getCoursesAmount();
        int pagesAmount = (int) Math.ceil(coursesAmount * 1.0 / recordsPerPage);


        request.setAttribute("teachersList", userService.getUsersByRole(Role.Teacher));
        request.setAttribute("topicsList", courseService.getAllTopics());

        request.setAttribute("coursesList", list);
        request.setAttribute("servlet", "adminCourses");
        request.setAttribute("amountOfPages", pagesAmount);
        request.setAttribute("currentPage", page);

        request.setAttribute("pageToInclude", "/admin/adminCourses.jsp");
        request.getRequestDispatcher("/admin/adminPage.jsp").forward(request, response);

    }
    private void executePost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {}

}
