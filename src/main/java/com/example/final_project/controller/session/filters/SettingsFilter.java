package com.example.final_project.controller.session.filters;


import com.example.final_project.database.entities.user.Role;
import com.example.final_project.dto.UserDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;



@WebFilter(filterName = "settingsFilter", urlPatterns = {"/controller"})
//?command=settings
public class SettingsFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if (servletRequest.getParameter("command") != null
                && servletRequest.getParameter("command").compareTo("settings") == 0) {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            if(((UserDTO)req.getSession().getAttribute("user")).getRole() == Role.Student){
                req.setAttribute("toInclude", "/client/settings.jsp");
                req.setAttribute("toForward", "/client/clientPage.jsp");


            }
            else if(((UserDTO)req.getSession().getAttribute("user")).getRole() == Role.Teacher){
                req.setAttribute("toInclude", "/teacher/teacherSettings.jsp");
                req.setAttribute("toForward", "/teacher/teacherPage.jsp");

            }
            else{
                req.setAttribute("toInclude", "/teacher/teacherSettings.jsp");
                req.setAttribute("toForward", "/admin/adminPage.jsp");

            }
            req.setAttribute("toRedirect", "/project/controller?command=users");
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }


}
