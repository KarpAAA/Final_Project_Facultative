package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.MessageDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.MessagesService;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Command of teacher role
 * Using to show teacher messages of student desire to join teachers course
 */
public class TeacherMessagesCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");

        String action = request.getParameter("action");
        if (action != null && action.compareTo("sent") == 0) {
            List<MessageDTO> list = (new MessagesService(connectionPool)).getMessagesBySender(userDTO);

            request.setAttribute("messages", list);
            request.setAttribute("messagesAmount", list.size());

        } else {

            Map<CourseDTO, List<UserDTO>> map = userService.getAllRegisteredUserToTeacherCourses(userDTO);
            request.setAttribute("map", map);


            AtomicInteger amount = new AtomicInteger();
            map.values().stream().forEach(userList -> amount.addAndGet(userList.size()));
            request.setAttribute("messagesAmount", amount.get());
        }


        request.setAttribute("servlet", "messages");
        request.setAttribute("pageToInclude", "/teacher/teacherMessages.jsp");
        request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request, response);

    }

    private void executePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

}
