package com.example.final_project.controller.factory.commands.teacher;


import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.task.TaskBuilder;
import com.example.final_project.dto.TaskDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.TaskService;
import com.example.final_project.utilities.mappers.CourseMapper;
import com.example.final_project.utilities.mappers.TaskMapper;
import org.mapstruct.factory.Mappers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TeacherOperateWithTask implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        TaskService taskService = new TaskService(connectionPool);

        request.setAttribute("course", request.getParameter("course"));
        request.setAttribute("action", request.getParameter("action"));
        if (request.getParameter("action") != null
                && request.getParameter("action").compareTo("edit") == 0) {
            request.setAttribute("task", taskService.findTask(Integer.parseInt(request.getParameter("task"))));
        }
        request.setAttribute("pageToInclude", "/teacher/addTask.jsp");
        request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        TaskService taskService = new TaskService(connectionPool);

        String action = request.getParameter("action");
        switch (action) {
            case "add" -> {
                TaskDTO task = formTask(connectionPool, request);
                taskService.addTask(task);
            }
            case "edit" -> {
                TaskDTO task = formTask(connectionPool, request);
                taskService.updateTask(request.getParameter("taskId"), task);
            }
            case "delete" -> {
                taskService.deleteTask(request.getParameter("task"));
            }
        }

        response.sendRedirect("/project/controller?command=teacherDetailedCourse&title="
                + request.getParameter("course"));
    }

    private TaskDTO formTask(ConnectionPool connectionPool, HttpServletRequest request) {
        CourseService courseService = new CourseService(connectionPool);

        return Mappers.getMapper(TaskMapper.class)
                .taskToTaskDTO((new TaskBuilder())
                        .setCourse(Mappers.getMapper(CourseMapper.class).courseDTOToCourse(courseService.findCourse(request.getParameter("course"))))
                        .setTitle(request.getParameter("title"))
                        .setCondition(request.getParameter("condition"))
                        .buildTask());


    }

}