package com.example.final_project.controller.factory.commands.admin;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.course.CourseBuilder;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;
import com.example.final_project.utilities.mappers.UserMapper;
import com.example.final_project.validation.Validator;
import org.mapstruct.factory.Mappers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;


public class AdminAddCourse implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        CourseService courseService = new CourseService(connectionPool);


        Course course = formCourse(connectionPool, request);
        List<String> errorList = courseService.validateCourse(course);


        request.setAttribute("teachersList", userService.getUsersByRole(Role.Teacher));
        request.setAttribute("errorList", errorList);
        request.setAttribute("course", course);

        if (errorList.size() == 0) {
            courseService.addCourse(course);
            resp.sendRedirect("/project/controller?command=adminCourses");
        }
        else {
            request.setAttribute("pageToInclude", "/admin/addCourse.jsp");
            request.getRequestDispatcher("/admin/adminPage.jsp").forward(request, resp);
        }


    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageToInclude", "/admin/addCourse.jsp");
        request.getRequestDispatcher("/admin/adminPage.jsp").forward(request, response);
    }

    private Course formCourse(ConnectionPool connectionPool, HttpServletRequest req) {
        UserService userService = new UserService(connectionPool);
        CourseBuilder courseBuilder = new CourseBuilder();
        courseBuilder.setTitle(req.getParameter("title"))
                .setPrice(Integer.parseInt(req.getParameter("price")))
                .setTopic(req.getParameter("topic"))
                .setTeacher(Mappers.getMapper(UserMapper.class).userDTOToUser(userService.findUser(req.getParameter("teacher"))))
                .setState(State.NotStarted)
                .setMaxStudentsAmount(Integer.parseInt(req.getParameter("maxStudentsAmount")))
                .setDescription("");


        if (req.getParameter("description") != null) courseBuilder.setDescription(req.getParameter("description"));

        if(!Validator.validateDate(req.getParameter("startDate")))courseBuilder.setStartDate(null);
        else{
            courseBuilder.setStartDate(Date.valueOf(req.getParameter("startDate")));
        }

        if(!Validator.validateDate(req.getParameter("finishDate")))courseBuilder.setFinishDate(null);
        else{
            courseBuilder.setFinishDate(Date.valueOf(req.getParameter("finishDate")));
        }

        return courseBuilder.buildCourse();
    }


}

