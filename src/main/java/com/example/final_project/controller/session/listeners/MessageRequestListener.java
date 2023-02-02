package com.example.final_project.controller.session.listeners;




import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.MessageDTO;
import com.example.final_project.services.MessagesService;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@WebListener()
public class MessageRequestListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {

    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpSession session = ((HttpServletRequest)sre.getServletRequest()).getSession();
        List<MessageDTO> messageDTOList = (List<MessageDTO>) session.getAttribute("messagesList");
        if(messageDTOList !=null){
            MessagesService messagesService = new MessagesService((ConnectionPool) session.getServletContext().getAttribute("connectionPool"));
            messagesService.changeStatus(messageDTOList);
        }

    }
}
