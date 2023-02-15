package com.example.final_project.utilities;


import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.dto.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Logging manager which is used to log some details
 * in 4 cases : User_Request, Audit Trail(some db changes),
 * Errors and Threads
 */
public class LoggingManager {
    public enum Change {
        CREATED, UPDATED, DELETED;
    }

    private static final Logger logRequest;
    private static final Logger logAuditTrail;
    private static final Logger logAvailability;
    private static final Logger logThreads;

    static {
        logRequest = LogManager.getLogger("LogRequest");
        logAuditTrail = LogManager.getLogger("LogAuditTrail");
        logAvailability = LogManager.getLogger("LogAvailability");
        logThreads = LogManager.getLogger("LogThreads");
    }

    public static void logRequest(HttpServletRequest request) {
        Logger logRequest = LogManager.getLogger("LogRequest");
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        StringBuilder stringBuilder = new StringBuilder();
        if (user != null) stringBuilder.append("User: ").append(user.getLogin());
        stringBuilder.append("\nIp: ").append(request.getRemoteAddr())
                .append("Endpoint url: ").append(request.getRequestURI())
                .append("Context: ").append("\n");

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            stringBuilder.append("key: ").append(key)
                    .append("value: ").append(request.getHeader(key));
        }

        logRequest.info(stringBuilder.toString());
    }

    public static void logAuditTrail(Change change, Object object) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(object.getClass().toString()).append(" to\n").append(object.toString())
                .append(" has been ").append(change.toString().toLowerCase());

        logAuditTrail.info(stringBuilder.toString());
    }

    public static void logAvailability(HttpServletRequest request,  Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        if(user!=null){
            stringBuilder.append("User: ").append(user.getLogin())
                    .append(" has faced an exception:");
        }
        stringBuilder.append("\n").append(exception.toString());

        logAvailability.error(stringBuilder.toString());
    }

    public static void logThreads(HttpServletRequest request, String command) {
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        StringBuilder stringBuilder = new StringBuilder();
        if(user!=null){
            stringBuilder.append("User ").append(user.getLogin());
        }
        else stringBuilder.append("Unidentified user");

        stringBuilder.append(" has tried to access: ")
                .append(command).append(" command");


        logThreads.warn(stringBuilder.toString());
    }
}
