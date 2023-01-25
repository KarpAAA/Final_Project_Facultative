package com.example.final_project.controller.factory.commands.defaulT;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.user.DirectorBuilder;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.database.entities.user.UserBuilder;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) req.getServletContext().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);

        if (req.getParameter("action") != null
                && req.getParameter("action").compareToIgnoreCase("updateUserFields") == 0) {

            User user = formUser(req);
            List<String> errorList = userService.validateUser(req.getParameter("cpwd"), user);

            errorList.remove("login");
            req.setAttribute("errorList", errorList);

            if (errorList.size() == 0) {
                userService.updateUser(user);
                req.getSession().removeAttribute("user");
                req.getSession().setAttribute("user", userService.findUser(user.getLogin()));
                resp.sendRedirect((String) req.getAttribute("toRedirect"));
            }
            else {
                req.setAttribute("pageToInclude", req.getAttribute("toInclude"));
                req.getRequestDispatcher((String) req.getAttribute("toForward")).forward(req, resp);
            }
        }
        else if(req.getParameter("action") != null
                && req.getParameter("action").compareToIgnoreCase("updateUserPassword") == 0){
            String pwd = req.getParameter("pwd");
            String cPwd = req.getParameter("cpwd");

            List<String> errorList = new ArrayList<>();
            if (!cPwd.equals("")&& cPwd.compareTo(pwd) != 0) errorList.add("cpwd");
            req.setAttribute("errorList", errorList);


            if (errorList.size() == 0) {
                UserDTO user = (UserDTO)req.getSession().getAttribute("user");
                userService.updateUserPassword((UserDTO)req.getSession().getAttribute("user"), pwd);
                req.getSession().setAttribute("user", user);
                resp.sendRedirect((String) req.getAttribute("toRedirect"));
            }
            else {
                req.setAttribute("pageToInclude", req.getAttribute("toInclude"));
                req.getRequestDispatcher((String) req.getAttribute("toForward")).forward(req, resp);
            }
        }
        else {
            UserDTO sessionUserDTO = (UserDTO)req.getSession().getAttribute("user");
            userService.addPhotoToUser(req, sessionUserDTO);
            req.getSession().removeAttribute("user");
            req.getSession().setAttribute("user", userService.findUser(sessionUserDTO.getLogin()));

            resp.sendRedirect((String) req.getAttribute("toRedirect"));


        }
    }
    private void executeGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageToInclude", req.getAttribute("toInclude"));
        req.getRequestDispatcher((String) req.getAttribute("toForward")).forward(req, resp);
    }


    private User formUser(HttpServletRequest req) {
        UserBuilder userBuilder = DirectorBuilder.buildStudent(
                        ((UserDTO) req.getSession().getAttribute("user")).getLogin(),
                        "",
                        req.getParameter("name"),
                        req.getParameter("email"))
                .setSurname(req.getParameter("surname"));

        if (req.getParameter("age") != "") {
            userBuilder.setAge(Integer.parseInt(req.getParameter("age")));
        } else {
            userBuilder.setAge(-1);
        }

        if (req.getParameter("phone") != "") {
            userBuilder.setPhone(req.getParameter("phone"));
        } else {
            userBuilder.setPhone("");
        }


        return userBuilder.getUser();
    }

}