package com.example.final_project.controller.factory.commands.admin;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.entities.course.Course;
import com.example.final_project.entities.course.CourseBuilder;
import com.example.final_project.entities.course.State;
import com.example.final_project.entities.user.User;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;
import com.example.final_project.validation.Validator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileExistsException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class AdminCourseEditingCommand implements Command{
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        String courseTitle = request.getParameter("courseTitle");

        CourseService courseService = new CourseService(connectionPool);
        UserService userService = new UserService(connectionPool);

        Course course = courseService.getCourseByTitle(courseTitle);
        Map<Integer, List<User>> userMap = userService.getUsersMarksMap(course);



        request.setAttribute("course", course);
        request.setAttribute("students", userMap);
        request.setAttribute("pageToInclude", "/admin/editingCourse.jsp");


        request.getRequestDispatcher("/admin/adminPage.jsp").forward(request, response);
    }

    private void executePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) req.getSession().getAttribute("connectionPool");
        CourseService courseService = new CourseService(connectionPool);
        if(req.getParameter("action") != null && req.getParameter("action").compareTo("editCourse") == 0){



            Course course = formCourse(connectionPool, req);
            List<String> errorList = courseService.validateCourse(course);

            errorList.remove("title");
            req.setAttribute("errorList", errorList);

            if(errorList.size()==0){
                courseService.updateCourse(connectionPool,course);
                resp.sendRedirect("/project/controller?command=adminEditingCourse&courseTitle="+course.getTitle());


            }
            else {
                req.setAttribute("course", course);
                req.setAttribute("pageToInclude", "/admin/editingCourse.jsp");
                req.getRequestDispatcher("/admin/adminPage.jsp").forward(req, resp);

            }

        }

        else{
            Course course = courseService.getCourseByTitle(req.getParameter("courseTitle"));
            courseService.addPhotoToCourse(req,course);
            resp.sendRedirect("/project/controller?command=adminEditingCourse&courseTitle="+course.getTitle());

        }
    }
    private Course formCourse(ConnectionPool connectionPool, HttpServletRequest req) {
        UserService userService = new UserService(connectionPool);
        CourseBuilder courseBuilder = new CourseBuilder();
        courseBuilder.setTitle(req.getParameter("courseTitle"))
                .setPrice(Integer.parseInt(req.getParameter("price")))
                .setTopic(req.getParameter("topic"))
                .setTeacher(userService.findUser(req.getParameter("teacher")))
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
