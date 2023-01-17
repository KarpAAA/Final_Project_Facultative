package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.MessageDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.entities.course.Course;
import com.example.final_project.entities.message.MessageBuilder;
import com.example.final_project.entities.message.Status;
import com.example.final_project.entities.user.User;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class TeacherMessagesCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        User user = (User) request.getSession().getAttribute("user");


        Map<Course, List<User>> map = userService.getAllRegisteredUserToTeacherCourses(user);
        request.setAttribute("map", map);


        AtomicInteger amount = new AtomicInteger();
        map.values().stream().forEach(userList -> amount.addAndGet(userList.size()));
        request.setAttribute("messagesAmount", amount.get());


        request.setAttribute("servlet", "messages");
        request.setAttribute("pageToInclude", "/teacher/teacherMessages.jsp");
        request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request, response);

    }

    private void executePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {}

}
