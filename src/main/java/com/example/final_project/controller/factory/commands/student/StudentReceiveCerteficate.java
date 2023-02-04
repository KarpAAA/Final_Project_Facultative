package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.controller.session.exceptions.NoAccessProvidedException;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;


public class StudentReceiveCerteficate implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else {
            try {
                executeGet(request, response);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, URISyntaxException, DocumentException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        CourseService courseService = new CourseService(connectionPool);
        CourseDTO courseDTO = courseService.findCourse(request.getParameter("course"));
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");


        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(new File("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\iText.pdf")));
        document.open();
        addingElementsToPdf(courseDTO, user, document);
        document.close();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String filename = "Certificate_" + courseDTO.getTitle() + "_.pdf";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");


        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\iText.pdf"));
        int i;
        while ((i = fileInputStream.read()) != -1) {
            out.write(i);
        }
        fileInputStream.close();
        out.close();

    }

    @Override
    public boolean securityCheck(HttpServletRequest request, HttpServletResponse response) {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        CourseService courseService = new CourseService(connectionPool);
        CourseDTO courseDTO = courseService.findCourse(request.getParameter("course"));
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");

        if(courseDTO.getState()!= State.Finished ||
                (new UserService(connectionPool).getUserGradeForCourse(user.getLogin(), courseDTO.getTitle()))<50)
            return false;
        return true;
    }

    private static void addingElementsToPdf(CourseDTO courseDTO, UserDTO user, Document document) throws DocumentException, IOException {
        float fntSize, lineSpacing;
        fntSize = 20f;
        lineSpacing = 25f;
        Paragraph p = new Paragraph(new Phrase(lineSpacing, courseDTO.getTitle(),
                FontFactory.getFont(FontFactory.TIMES_ROMAN, fntSize, Font.BOLD)));
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(100f);
        document.add(p);


        Image img = Image.getInstance(courseDTO.getPhoto());
        img.scaleToFit(300f, 200f);
        float x = (PageSize.A4.getWidth() - img.getScaledWidth()) / 2;
        float y = (PageSize.A4.getHeight() - img.getScaledHeight()) / 2;
        img.setAbsolutePosition(x, PageSize.A4.getHeight() - 100 - 200);
        img.setPaddingTop(20f);

        document.add(img);
        document.addTitle(user.getLogin() + " certificate");


        fntSize = 15f;
        lineSpacing = 20f;
        p = new Paragraph(new Phrase(lineSpacing, "Certificate of Completion",
                FontFactory.getFont(FontFactory.TIMES_ROMAN, fntSize, Font.BOLD)));
        p.setSpacingBefore(270f);
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);


        fntSize = 20f;
        lineSpacing = 25f;
        p = new Paragraph(new Phrase(lineSpacing, "This is to certify that",
                FontFactory.getFont(FontFactory.TIMES_ROMAN, fntSize, Font.BOLD)));
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(20f);
        document.add(p);

        fntSize = 25f;
        lineSpacing = 30f;
        p = new Paragraph(new Phrase(lineSpacing, user.getLogin(),
                FontFactory.getFont(FontFactory.TIMES_ROMAN, fntSize, Font.BOLD)));
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(20f);
        document.add(p);

        fntSize = 20f;
        lineSpacing = 30f;
        p = new Paragraph(new Phrase(lineSpacing, "has successfully completed course",
                FontFactory.getFont(FontFactory.TIMES_ROMAN, fntSize, Font.BOLD)));
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(20f);
        document.add(p);
    }


}
