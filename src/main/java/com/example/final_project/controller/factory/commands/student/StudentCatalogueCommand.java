package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;
import com.example.final_project.utilities.CoursesFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class StudentCatalogueCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        CourseService courseService = new CourseService(connectionPool);
        request.getSession().setAttribute("teachersList", userService.getUsersByRole(Role.Teacher));
        request.getSession().setAttribute("topicsList", courseService.getAllTopics());


        int page = 1;
        int recordsPerPage = 9;
        if (request.getParameter("page") != null)
            page = Integer.parseInt(
                    request.getParameter("page"));

        CoursesFilter coursesFilter = (CoursesFilter) request.getSession().getAttribute("coursesFilter");
        if (coursesFilter == null) {
            coursesFilter = new CoursesFilter();
        }
        coursesFilter.setOffset((page - 1) * recordsPerPage);
        coursesFilter.setCoursesPerPage(recordsPerPage);

        List<CourseDTO> coursesList = courseService.selectCoursesByCondition(coursesFilter);

        int noOfRecords = courseService.getCoursesAmount();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        request.setAttribute("currentPage", page);
        request.setAttribute("myCourses", coursesList);
        request.setAttribute("amountOfPages", noOfPages);
        request.setAttribute("servlet", "catalogue");


        request.setAttribute("pageToInclude", "/client/myCourses.jsp");
        request.getRequestDispatcher("/client/clientPage.jsp").forward(request, response);


    }
    private void executePost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        if (request.getParameter("action") != null) {
            request.getSession().setAttribute("coursesFilter", formCourseFilter(request));
        }
        executeGet(request, resp);
    }


    private CoursesFilter formCourseFilter(HttpServletRequest request) {
        CoursesFilter coursesFilter = new CoursesFilter();
        List<String> topicsList = (List<String>) request.getSession().getAttribute("topicsList");
        List<UserDTO> teachersList = (List<UserDTO>) request.getSession().getAttribute("teachersList");

        CoursesFilter.SortBy sortBy = CoursesFilter.SortBy.NONE;
        if (request.getParameter("sortAsc") != null && request.getParameter("sortAsc").compareTo("on") == 0)
            sortBy = CoursesFilter.SortBy.AZ;
        else if (request.getParameter("sortDesc") != null && request.getParameter("sortDesc").compareTo("on") == 0)
            sortBy = CoursesFilter.SortBy.ZA;
        coursesFilter.addSortFilter(sortBy);


        coursesFilter.addTopics(topicsList.stream()
                .filter(topic -> request.getParameter(topic) != null
                        && request.getParameter(topic).compareTo("on") == 0).collect(Collectors.toList()));

        if (request.getParameter("chooseAll") != null && request.getParameter("chooseAll").compareTo("on") == 0) {
            coursesFilter.addTeachers(teachersList.stream().map(UserDTO::getLogin).collect(Collectors.toList()));
        } else {
            coursesFilter.addTeachers(teachersList.stream()
                    .filter(teacher -> request.getParameter(teacher.getName()) != null
                            && request.getParameter(teacher.getName()).compareTo("on") == 0)
                    .map(UserDTO::getLogin).collect(Collectors.toList()));
        }


        List<CoursesFilter.Enrolled> enrolledList = new ArrayList<>();
        if (request.getParameter("enrolled50Less") != null && request.getParameter("enrolled50Less").compareTo("on") == 0)
            enrolledList.add(CoursesFilter.Enrolled.TO50);

        if (request.getParameter("enrolled100Less") != null && request.getParameter("enrolled100Less").compareTo("on") == 0)
            enrolledList.add(CoursesFilter.Enrolled.TO100);

        if (request.getParameter("enrolled100More") != null && request.getParameter("enrolled100More").compareTo("on") == 0)
            enrolledList.add(CoursesFilter.Enrolled.MORE);

        coursesFilter.addEnrolled(enrolledList);


        List<CoursesFilter.Duration> durationList = new ArrayList<>();
        if (request.getParameter("durationWeek") != null && request.getParameter("durationWeek").compareTo("on") == 0)
            durationList.add(CoursesFilter.Duration.WEEK);

        if (request.getParameter("durationMonth") != null && request.getParameter("durationMonth").compareTo("on") == 0)
            durationList.add(CoursesFilter.Duration.MONTH);

        if (request.getParameter("durationYear") != null && request.getParameter("durationYear").compareTo("on") == 0)
            durationList.add(CoursesFilter.Duration.YEAR);

        if (request.getParameter("durationYearMore") != null && request.getParameter("durationYearMore").compareTo("on") == 0)
            durationList.add(CoursesFilter.Duration.MORE);
        coursesFilter.addDurations(durationList);

        if(request.getParameter("search") != null){
            coursesFilter.setSearch(request.getParameter("search"));
        }
        return coursesFilter;
    }
}
