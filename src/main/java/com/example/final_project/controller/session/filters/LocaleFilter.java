package com.example.final_project.controller.session.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Filter used to switch interface language
 */
@WebFilter(filterName = "localeFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String lang = req.getParameter("lang");

        if(lang!=null && lang.compareToIgnoreCase("EN") == 0){
            ResourceBundle bundle =
                    ResourceBundle.getBundle("locale" , new Locale("en"));
            req.getSession().removeAttribute("bundle");
            req.getSession().setAttribute("bundle", bundle);
            req.getSession().setAttribute("lang", "EN");
        }
        else if(lang!=null &&  lang.compareToIgnoreCase("UA") == 0){
            ResourceBundle bundle =
                    ResourceBundle.getBundle("locale" , new Locale("ua"));
            req.getSession().removeAttribute("bundle");
            req.getSession().setAttribute("bundle", bundle);
            req.getSession().setAttribute("lang", "UA");
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }


}
