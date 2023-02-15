package com.example.final_project.controller.session.tags;


import com.example.final_project.dto.CourseDTO;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Tag which show course in card container
 */
public class CourseTag extends SimpleTagSupport {
    private CourseDTO course;
    private ResourceBundle bundle;

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public void setBundle(ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }


    public void doTag() throws JspException, IOException {
        if (course != null) {
            JspWriter out = getJspContext().getOut();
            out.println("<div class=\"card\" style=\"border-radius:6px; background-color: white; box-shadow: 5px 5px 10px 2px rgba(22,48,64,0.49);\">\n" +
                    "                    <div class=\"card-body\">\n" +
                    "                        <img class=\"img-rounded\" src=\"data:image/jpg;base64,"+course.getBase64String()+"\"\n" +
                    "                             style=\"width:100%;height:20%; margin-bottom: 10px;\">\n" +
                    "                        <p class=\"card-text\" style=\"color: #a3adaa;margin-left: 15px\">"+ bundle.getString("topic")+":"+ course.getTopic() + "</p>\n" +
                    "                        <h5 class=\"card-title\" style=\"color: black;margin-left: 15px\">" + bundle.getString("title")+":" +course.getTitle()+"</h5>\n" +
                    "                        <p class=\"card-text\" style=\"color: #1b4560;margin-left: 15px;margin-bottom: 15px\">"+ bundle.getString("price")+": " +course.getPrice()+"</p>\n" +
                    "                    </div>\n" +
                    "                </div>");
        }
    }
}