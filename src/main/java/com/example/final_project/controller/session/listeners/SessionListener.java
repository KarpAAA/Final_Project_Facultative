package com.example.final_project.controller.session.listeners;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.services.CourseService;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Listener used to adjust some initial parameters to session
 * such as language bundle, blocked user list
 * Also starting connection pool and changing courses state by date
 */
@WebListener()
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.setProperty("log4j.configurationFile",
                "C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\resources\\log4j2.xml");

        try {
            ConnectionPool connectionPool = ConnectionPool.create();
            se.getSession().getServletContext().setAttribute("connectionPool", connectionPool);
            se.getSession().getServletContext().setAttribute("blockedUsers", new ArrayList<String>());

            ResourceBundle defaultBundle =
                    ResourceBundle.getBundle("locale" , new Locale("en"));

            se.getSession().setAttribute("bundle", defaultBundle);
            se.getSession().setAttribute("lang", "EN");

            new Thread(() -> changeCoursesState(connectionPool)).start();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private synchronized void changeCoursesState(ConnectionPool connectionPool){
        CourseService courseService = new CourseService(connectionPool);
        courseService.changeCoursesState();
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        try {
            ((ConnectionPool)se.getSession().getAttribute("connectionPool")).shutdown();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
