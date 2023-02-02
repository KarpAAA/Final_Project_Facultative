package com.example.final_project.controller.factory;


import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.controller.factory.commands.admin.*;
import com.example.final_project.controller.factory.commands.auth.LoggingCommand;
import com.example.final_project.controller.factory.commands.auth.RegistrationCommand;
import com.example.final_project.controller.factory.commands.defaulT.*;
import com.example.final_project.controller.factory.commands.student.*;
import com.example.final_project.controller.factory.commands.teacher.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class CommandFactory {
    private static HashMap<String, Command> COMMAND_MAP = new HashMap<>();
    static{
        COMMAND_MAP.put("adminEditingCourse", new AdminCourseEditingCommand());
        COMMAND_MAP.put("adminCourses", new AdminCoursesCommand());
        COMMAND_MAP.put("adminUsers", new AdminUsersCommand());
        COMMAND_MAP.put("adminAddCourse", new AdminAddCourse());
        COMMAND_MAP.put("filterCourses", new FilterCourses());
        COMMAND_MAP.put("adminDeleteCourse", new AdminDeleteCourse());
        COMMAND_MAP.put("adminBlockUnlockStudent", new AdminBlockUnlockStudent());
        COMMAND_MAP.put("adminAddTeacher", new AdminAddTeacher());

        COMMAND_MAP.put("logging", new LoggingCommand());
        COMMAND_MAP.put("registration", new RegistrationCommand());

        COMMAND_MAP.put("studentCatalogue", new StudentCatalogueCommand());
        COMMAND_MAP.put("studentPassTask", new StudentPassTask());
        COMMAND_MAP.put("studentCourses", new StudentCoursesCommand());
        COMMAND_MAP.put("studentDetailedCourse", new StudentDetailedCourseCommand());
        COMMAND_MAP.put("studentMessages", new StudentsMessagesCommand());
        COMMAND_MAP.put("studentClearMessages", new StudentClearMessages());
        COMMAND_MAP.put("receiveCerteficate", new StudentReceiveCerteficate());
        COMMAND_MAP.put("deleteAccount", new UserDeleteAccount());
        COMMAND_MAP.put("registerUserToCourse", new RegisterUserToCourse());

        COMMAND_MAP.put("default", new DefaultCommand());
        COMMAND_MAP.put("logOut", new LogOutCommand());
        COMMAND_MAP.put("settings", new SettingsCommand());
        COMMAND_MAP.put("users", new UsersCommand());
        COMMAND_MAP.put("calendar", new CalendarCommand());

        COMMAND_MAP.put("teacherAddStudent", new TeacherAddStudentToCourseCommand());
        COMMAND_MAP.put("teacherCourses", new TeacherCoursesCommand());
        COMMAND_MAP.put("teacherDetailedCourse", new TeacherDetailedCourseCommand());
        COMMAND_MAP.put("teacherMessages", new TeacherMessagesCommand());
        COMMAND_MAP.put("teacherWriteMessage", new TeacherWriteMessage());
        COMMAND_MAP.put("teacherAddEvent", new TeacherAddEvent());
        COMMAND_MAP.put("deleteUserFromCourse", new DeleteUserFromCourse());
        COMMAND_MAP.put("teacherDownloadStudentSolution", new TeacherDownloadStudentSolution());
        COMMAND_MAP.put("operateWithTask", new TeacherOperateWithTask());
        COMMAND_MAP.put("buyCourse", new StudentBuyCourse());
        COMMAND_MAP.put("fillBalance", new StudentFillBalance());
    }

    public Command defineCommand(HttpServletRequest request, HttpServletResponse response) {
        if(request.getAttribute("ifBlockedUser")!=null){
            return COMMAND_MAP.get("logOut");
        }
        Command current = new DefaultCommand();

        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return current;
        }

        try {
            current = COMMAND_MAP.get(action);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        }
        return current;
    }
}