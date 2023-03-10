package com.example.final_project.controller.session.filters;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.controller.factory.commands.admin.*;
import com.example.final_project.controller.factory.commands.auth.LoggingCommand;
import com.example.final_project.controller.factory.commands.auth.RegistrationCommand;
import com.example.final_project.controller.factory.commands.defaulT.*;
import com.example.final_project.controller.factory.commands.student.*;
import com.example.final_project.controller.factory.commands.teacher.*;
import com.example.final_project.controller.session.exceptions.NoAccessProvidedException;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.LoggingManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

/**
 * Filter used to check if user has right(specified by role) to request stated command
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/controller"})
public class SecuirtyFilter implements Filter {
    private static final HashMap<String, Command> ADMIN_COMMAND_MAP = new HashMap<>();
    private static final HashMap<String, Command> STUDENT_COMMAND_MAP = new HashMap<>();
    private static final HashMap<String, Command> TEACHER_COMMAND_MAP = new HashMap<>();
    private static final HashMap<String, Command> DEFAULT_COMMAND_MAP = new HashMap<>();

    static {
        ADMIN_COMMAND_MAP.put("adminEditingCourse", new AdminCourseEditingCommand());
        ADMIN_COMMAND_MAP.put("adminCourses", new AdminCoursesCommand());
        ADMIN_COMMAND_MAP.put("adminUsers", new AdminUsersCommand());
        ADMIN_COMMAND_MAP.put("adminAddCourse", new AdminAddCourse());
        ADMIN_COMMAND_MAP.put("filterCourses", new FilterCourses());
        ADMIN_COMMAND_MAP.put("adminDeleteCourse", new AdminDeleteCourse());
        ADMIN_COMMAND_MAP.put("adminBlockUnlockStudent", new AdminBlockUnlockStudent());
        ADMIN_COMMAND_MAP.put("adminAddTeacher", new AdminAddTeacher());
        ADMIN_COMMAND_MAP.put("deleteAccount", new UserDeleteAccount());

        DEFAULT_COMMAND_MAP.put("logging", new LoggingCommand());
        DEFAULT_COMMAND_MAP.put("registration", new RegistrationCommand());


        STUDENT_COMMAND_MAP.put("studentCatalogue", new StudentCatalogueCommand());
        STUDENT_COMMAND_MAP.put("studentCourses", new StudentCoursesCommand());
        STUDENT_COMMAND_MAP.put("studentDetailedCourse", new StudentDetailedCourseCommand());
        STUDENT_COMMAND_MAP.put("studentMessages", new StudentsMessagesCommand());
        STUDENT_COMMAND_MAP.put("registerUserToCourse", new RegisterUserToCourse());
        STUDENT_COMMAND_MAP.put("deleteAccount", new UserDeleteAccount());
        STUDENT_COMMAND_MAP.put("calendar", new CalendarCommand());
        STUDENT_COMMAND_MAP.put("studentClearMessages", new StudentClearMessages());
        STUDENT_COMMAND_MAP.put("receiveCerteficate", new StudentReceiveCerteficate());
        STUDENT_COMMAND_MAP.put("studentPassTask", new StudentPassTask());
        STUDENT_COMMAND_MAP.put("buyCourse", new StudentBuyCourse());
        STUDENT_COMMAND_MAP.put("fillBalance", new StudentFillBalance());


        DEFAULT_COMMAND_MAP.put("default", new DefaultCommand());
        DEFAULT_COMMAND_MAP.put("logOut", new LogOutCommand());
        DEFAULT_COMMAND_MAP.put("settings", new SettingsCommand());
        DEFAULT_COMMAND_MAP.put("users", new UsersCommand());


        TEACHER_COMMAND_MAP.put("teacherAddStudent", new TeacherAddStudentToCourseCommand());
        TEACHER_COMMAND_MAP.put("teacherCourses", new TeacherCoursesCommand());
        TEACHER_COMMAND_MAP.put("teacherDetailedCourse", new TeacherDetailedCourseCommand());
        TEACHER_COMMAND_MAP.put("teacherMessages", new TeacherMessagesCommand());
        TEACHER_COMMAND_MAP.put("teacherWriteMessage", new TeacherWriteMessage());
        TEACHER_COMMAND_MAP.put("deleteUserFromCourse", new DeleteUserFromCourse());
        TEACHER_COMMAND_MAP.put("teacherAddEvent", new TeacherAddEvent());
        TEACHER_COMMAND_MAP.put("calendar", new CalendarCommand());
        TEACHER_COMMAND_MAP.put("teacherDownloadStudentSolution", new TeacherDownloadStudentSolution());
        TEACHER_COMMAND_MAP.put("operateWithTask", new TeacherOperateWithTask());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;

        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        String command;
        if (req.getParameter("command") != null) {
            command = req.getParameter("command");

        } else {
            command = "";
        }

        try {
            if (user == null) {
                if (command.equals("") || DEFAULT_COMMAND_MAP.containsKey(command)) ;
                else throw new NoAccessProvidedException();
            } else if (user.getRole() == Role.Admin) {
                if (ADMIN_COMMAND_MAP.containsKey(command) || DEFAULT_COMMAND_MAP.containsKey(command)) ;
                else throw new NoAccessProvidedException();
            } else if (user.getRole() == Role.Teacher) {
                if (TEACHER_COMMAND_MAP.containsKey(command) || DEFAULT_COMMAND_MAP.containsKey(command)) ;
                else throw new NoAccessProvidedException();
            } else if (user.getRole() == Role.Student) {
                if (STUDENT_COMMAND_MAP.containsKey(command) || DEFAULT_COMMAND_MAP.containsKey(command)) ;
                else throw new NoAccessProvidedException();
                if (command.compareTo("deleteAccount") == 0 && req.getParameter("teacher") != null)
                    throw new NoAccessProvidedException();
            }
        } catch (NoAccessProvidedException e) {
            LoggingManager.logThreads(req, command);
            throw new NoAccessProvidedException();
        }
        chain.doFilter(servletRequest, servletResponse);
    }


}
