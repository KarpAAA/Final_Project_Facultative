package com.example.final_project.utilities;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class MailManager {
    private static final String fromEmail = "bbetostudy@gmail.com"; //requires valid gmail id
    private static final String password = "qgevpzsdqvgewgei"; // correct password for gmail id
    private static final Properties props;

    static {
        try {
            props = new Properties();
            props.load(new FileInputStream(
                    "C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\resources\\mail.properties"));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeMessage(String text, String subject, String mailTo){
        Authenticator auth = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        try
        {
            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));
            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");
            msg.setText(text, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo, false));

            Transport.send(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
