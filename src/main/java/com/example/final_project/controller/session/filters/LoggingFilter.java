package com.example.final_project.controller.session.filters;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.entities.user.Role;
import com.example.final_project.entities.user.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@WebFilter(filterName = "loggingFilter", urlPatterns = {"/controller"})
//?command=users
public class LoggingFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if (servletRequest.getParameter("command") != null
                && servletRequest.getParameter("command").compareTo("users") == 0) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            ConnectionPool connectionPool = (ConnectionPool) req.getSession().getAttribute("connectionPool");
            User user = (User) req.getSession().getAttribute("user");
            if (user.getRole() == Role.Student) {
                req.setAttribute("toRedirect", "/project/controller?command=studentCourses&page=1");
            } else if (user.getRole() == Role.Teacher) {
                req.setAttribute("toRedirect", "/project/controller?command=teacherCourses&page=1");
            } else {
                req.setAttribute("toRedirect", "/project/controller?command=adminCourses&page=1");
            }
            req.getSession().removeAttribute("coursesFilter");
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }


}
