package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.TaskDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.TaskService;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TeacherDetailedCourseCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        String courseTitle = request.getParameter("title");

        CourseService courseService = new CourseService(connectionPool);
        UserService userService = new UserService(connectionPool);

        CourseDTO courseDTO = courseService.getCourseByTitle(courseTitle);

        Map<UserDTO, Map<TaskDTO, Integer>> marksMap = userService.getUserTasksMarksMap(courseDTO);
        Map<UserDTO, Map<TaskDTO, byte[]>> solutionMap = userService.getUserTasksSolutionMap(courseDTO);


        Map<UserDTO, Integer> userMarks = userService.getUsersMarksMap(courseDTO);
        request.setAttribute("course", courseDTO);
        request.getSession().setAttribute("students", marksMap);
        request.setAttribute("solutionMap", solutionMap);
        request.setAttribute("tasksList", new TaskService(connectionPool).getTaskToCourse(courseDTO));
        request.setAttribute("userMarks", userMarks);
        request.setAttribute("pageToInclude", "/teacher/detailedCourse.jsp");


        request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        String courseTitle = request.getParameter("title");
        UserService userService = new UserService(connectionPool);

//        if (request.getParameter("action") != null && request.getParameter("action").compareTo("saveChanges") == 0) {
            Map<UserDTO, Map<TaskDTO,Integer>> newMarks =
                    (Map<UserDTO, Map<TaskDTO,Integer>>) request.getSession().getAttribute("students");

            for(var entry : newMarks.entrySet()){
                for(var entry1:entry.getValue().entrySet()){
                    String mark = request.getParameter("mark_" +
                            entry1.getKey().getId()+"_" +
                            entry.getKey().getLogin());

                    if(mark!=null)entry1.setValue(Integer.valueOf(mark));
                }
            }
            userService.saveMarks(newMarks, courseTitle);
            request.getSession().removeAttribute("students");
//        }
        executeGet(request, resp);
    }

    @Override
    public boolean securityCheck(HttpServletRequest request, HttpServletResponse response) {

        if (request.getParameter("action") != null && request.getParameter("action").compareTo("saveChanges") == 0) {
            ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
            CourseService courseService = new CourseService(connectionPool);

            CourseDTO course = courseService.getCourseByTitle(request.getParameter("title"));
            List<CourseDTO> teacherCourses = courseService.getAllTeacherCourses(
                    (UserDTO) request.getSession().getAttribute("user")
                    ,0,Integer.MAX_VALUE );
            if (course.getState() != State.InProgress
                    || !teacherCourses.contains(course)) return false;
        }


        return true;
    }
}
