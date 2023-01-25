package com.example.final_project.controller.session.filters;


import com.example.final_project.dto.UserDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter(filterName = "encodingFilter", urlPatterns = {"/*"},
        initParams = {@WebInitParam(name = "encoding", value = "UTF-8")})
public class BlocekdFilter implements Filter {



    public void init(FilterConfig filterConfig){}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        if(user!=null){
            ArrayList<String> blockedUsers = (ArrayList<String>) req.getServletContext().getAttribute("blockedUsers");
            if(blockedUsers.contains(user.getLogin())){
                req.setAttribute("ifBlockedUser","true");
            }
        }
        chain.doFilter(request, response);
    }

    public void destroy() {}

}