package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.meeting.MeetingBuilder;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.MeetingDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.MeetingsService;
import com.example.final_project.utilities.mappers.CourseMapper;
import com.example.final_project.utilities.mappers.MeetingMapper;
import com.mysql.cj.protocol.a.LocalTimeValueEncoder;
import org.mapstruct.factory.Mappers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Command of teacher role
 * Using to add event to database
 */
public class TeacherAddEvent implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        List<CourseDTO> coursesList = (new CourseService(connectionPool)).getAllTeacherCourses(user, 0, Integer.MAX_VALUE);

        request.setAttribute("coursesList", coursesList);
        request.setAttribute("pageToInclude", "/teacher/teacherAddEvent.jsp");
        request.getRequestDispatcher("/teacher/teacherPage.jsp").forward(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        MeetingsService meetingsService = new MeetingsService(connectionPool);
        MeetingDTO meeting = formMeeting(connectionPool, request);
        meetingsService.addEvent(meeting);


        response.sendRedirect("/project/controller?command=calendar");
    }


    /**
     * @param connectionPool pool of connection need to request data from database
     * @param request contains necessary info about event
     * @return created meeting of type MeetingDTO
     */
    private MeetingDTO formMeeting(ConnectionPool connectionPool, HttpServletRequest request) {
        CourseService courseService = new CourseService(connectionPool);
        String date = request.getParameter("date");

        return Mappers.getMapper(MeetingMapper.class)
                .meetingToMeetingDTO((new MeetingBuilder())
                        .setCourse(Mappers.getMapper(CourseMapper.class).courseDTOToCourse(courseService.findCourse(request.getParameter("course"))))
                        .setTitle(request.getParameter("title"))
                        .setDescription(request.getParameter("description"))
                        .setLink(request.getParameter("link"))
                        .setTime(findTime(date.split(" ")[1]+date.split(" ")[2]))
                        .setStartDate(Date.valueOf(LocalDate.parse(date.substring(0, 10).replaceAll("/", "-"),
                                DateTimeFormatter.ofPattern("MM-dd-yyyy"))))
                        .buildMeeting());


    }

    private Time findTime(String time) {

        if(time.split(":")[0].length()==1) time = "0" + time;

        Map<Long, String> ampmStrings = Map.of(0L, "AM", 1L, "PM");
        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("hh:mm")
                .appendText(ChronoField.AMPM_OF_DAY, ampmStrings)
                .toFormatter();

        LocalTime localTime = LocalTime.parse(time,timeFormatter);
        return Time.valueOf(localTime);
    }
}
