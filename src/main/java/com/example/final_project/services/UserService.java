package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.entities.course.Course;
import com.example.final_project.entities.user.Blocked_State;
import com.example.final_project.entities.user.Role;
import com.example.final_project.entities.user.User;
import com.example.final_project.validation.Validator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileExistsException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class UserService {
    private final UserDao userDao;
    private ConnectionPool connectionPool;

    public UserService(ConnectionPool connectionPool) {
        this.userDao = new UserDao(connectionPool);
        this.connectionPool = connectionPool;
    }

    public List<User> getUsersByRole(Role role) {
        return userDao.getUsersByRole(role);
    }

    public void blockOrUnlockStudent(String toDo, String login) {
        if (toDo != null && toDo.compareTo("block") == 0) {
            userDao.updateBlockedState(findUser(login), Blocked_State.BLOCKED);
        } else {
            userDao.updateBlockedState(findUser(login), Blocked_State.UNLOCKED);
        }
    }

    public Map<Integer, List<User>> getUsersMarksMap(Course course) {
        return userDao.selectUsersByCourse(course);
    }

    public Map<User, List<Course>> getUserToCourseMapByRole(Role role) {
        Map<User, List<Course>> map = new HashMap<>();
        List<User> users = getUsersByRole(role);
        CoursesDao coursesDao = new CoursesDao(connectionPool);
        for (var user : users) {
            map.put(user, coursesDao.getUserCourses(user));
        }

        return map;

    }

    public void addPhotoToUser(HttpServletRequest req, User user) {
        String file_name = null;

        boolean isMultipleContent = ServletFileUpload.isMultipartContent(req);
        if (!isMultipleContent) return;
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> fields = upload.parseRequest(req);
            Iterator<FileItem> it = fields.iterator();
            if (!it.hasNext()) return;

            while (it.hasNext()) {
                FileItem fileItem = it.next();
                boolean isFormField = fileItem.isFormField();

                if (isFormField) {
                    if (file_name == null) {
                        if (fileItem.getFieldName().equals("fileName")) {
                            file_name = fileItem.getString();
                        }
                    }
                } else {
                    if (fileItem.getSize() > 0) {
                        try {
                            fileItem.write(new File("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\userImages\\" + fileItem.getName()));
                        } catch (FileExistsException e) {

                        }
                        resizeImage("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\userImages\\" + fileItem.getName()
                        ,"C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\userImages\\"+"500x500." + fileItem.getName()
                                ,500,500);
                        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\userImages\\"+"500x500." + fileItem.getName());
                        user.setPhoto(fileInputStream);
                        userDao.updateUserPhoto(user);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> validateUser(String cPwd, User user) {
        List<String> errorList;
        Validator validator = new Validator();
        errorList = validator.validateUser(connectionPool, user);

        if (user.getAge() == 0) errorList.add("age");
        if (!cPwd.equals("")
                && cPwd.compareTo(user.getPassword()) != 0) errorList.add("cpwd");

        return errorList;
    }

    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    public User findUser(String userLogin) {
        return userDao.findUser(userLogin);
    }

    public User identifyUser(String login, String pwd) {
        return userDao.identifyUser(login, pwd);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    public String getUserRegisteredState(User user, Course course) {
        return userDao.getUserRegisteredState(user, course);
    }

    public int getUserGradeForCourse(String login, String courseTitle) {
        return userDao.getUserGradeForCourse(login, courseTitle);
    }

    public void saveMarks(Map<Integer, List<User>> students, String courseTitle, int[] newMarks) {
        userDao.saveMarks(students, courseTitle, newMarks);
    }

    public Map<Course, List<User>> getAllRegisteredUserToTeacherCourses(User teacher) {
        Map<Course, List<User>> resultMap = new HashMap<>();
        CoursesDao coursesDao = new CoursesDao(connectionPool);

        for (Course teacherCourse : coursesDao.getAllTeacherCourses(teacher, 0,100)) {
            resultMap.put(teacherCourse, userDao.getRegisteredUserToCourse(teacherCourse.getTitle()));

        }

        return resultMap;
    }

    public void resizeImage(String imagePathToRead,
                                  String imagePathToWrite, int resizeWidth, int resizeHeight) throws IOException {

        File fileToRead = new File(imagePathToRead);
        BufferedImage bufferedImageInput = ImageIO.read(fileToRead);

        BufferedImage bufferedImageOutput = new BufferedImage(resizeWidth,
                resizeHeight, bufferedImageInput.getType());

        Graphics2D g2d = bufferedImageOutput.createGraphics();
        g2d.drawImage(bufferedImageInput, 0, 0, resizeWidth, resizeHeight, null);
        g2d.dispose();

        String formatName = imagePathToWrite.substring(imagePathToWrite
                .lastIndexOf(".") + 1);
        try {
            ImageIO.write(bufferedImageOutput, formatName, new File(imagePathToWrite));
        } catch (FileExistsException e) {

        }

    }


}
