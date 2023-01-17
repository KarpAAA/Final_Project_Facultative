package com.example.final_project.controller.factory.commands.defaulT;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.entities.course.Course;
import com.example.final_project.entities.user.Role;
import com.example.final_project.entities.user.User;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;
import com.example.final_project.utilities.CoursesFilter;
import com.google.protobuf.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("action") != null) {
            request.getSession().setAttribute("coursesFilter", formCourseFilter(request));
        }
        executeGet(request, response);
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getSession().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);
        CourseService courseService = new CourseService(connectionPool);


        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int recordsPerPage = 12;

        CoursesFilter coursesFilter = (CoursesFilter) request.getSession().getAttribute("coursesFilter");
        if (coursesFilter == null) {
            coursesFilter = new CoursesFilter();
        }
        coursesFilter.setOffset((page - 1) * recordsPerPage);
        coursesFilter.setCoursesPerPage(recordsPerPage);


        CoursesDao dao = new CoursesDao(connectionPool);
        List<Course> list = dao.selectCoursesByCondition(coursesFilter);
        int coursesAmount = dao.getCoursesAmount();
        int pagesAmount = (int) Math.ceil(coursesAmount * 1.0 / recordsPerPage);

        request.getSession().setAttribute("teachersList", userService.getUsersByRole(Role.Teacher));
        request.getSession().setAttribute("topicsList", courseService.getAllTopics());
        request.setAttribute("coursesList", list);
        request.setAttribute("amountOfPages", pagesAmount);
        request.setAttribute("currentPage", page);

        request.setAttribute("pageToInclude", "/default/coursesList.jsp");
        request.getRequestDispatcher("/default/defaultPage.jsp").forward(request, response);
    }

    private CoursesFilter formCourseFilter(HttpServletRequest request) {
        CoursesFilter coursesFilter = new CoursesFilter();
        List<String> topicsList = (List<String>) request.getSession().getAttribute("topicsList");
        List<User> teachersList = (List<User>) request.getSession().getAttribute("teachersList");

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
            coursesFilter.addTeachers(teachersList.stream().map(User::getLogin).collect(Collectors.toList()));
        } else {
            coursesFilter.addTeachers(teachersList.stream()
                    .filter(teacher -> request.getParameter(teacher.getName()) != null
                            && request.getParameter(teacher.getName()).compareTo("on") == 0)
                    .map(User::getLogin).collect(Collectors.toList()));
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
