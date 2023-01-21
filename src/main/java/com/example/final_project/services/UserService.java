package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.CourseMapper;
import com.example.final_project.utilities.UserMapper;
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
import java.util.stream.Collectors;

public class UserService {
    private final UserDao userDao;
    private ConnectionPool connectionPool;

    public UserService(ConnectionPool connectionPool) {
        this.userDao = new UserDao(connectionPool);
        this.connectionPool = connectionPool;
    }

    public List<UserDTO> getUsersByRole(Role role) {
        return userDao.getUsersByRole(role).stream().map(UserMapper::userToUserDTO).collect(Collectors.toList());
    }

    public void blockOrUnlockStudent(String toDo, String login) {
        if (toDo != null && toDo.compareTo("block") == 0) {
            userDao.updateBlockedState(UserMapper.userDTOToUser(findUser(login)), Blocked_State.BLOCKED);
        } else {
            userDao.updateBlockedState(UserMapper.userDTOToUser(findUser(login)), Blocked_State.UNLOCKED);
        }
    }

    public Map<Integer, List<UserDTO>> getUsersMarksMap(CourseDTO courseDTO) {
        Map<Integer, List<User>> map = userDao.selectUsersByCourse(CourseMapper.courseDTOToCourse(courseDTO));
        Map<Integer, List<UserDTO>> resultMap = new HashMap<>();
        for(var entry:map.entrySet()){
            resultMap.put(entry.getKey(), entry.getValue()
                    .stream()
                    .map(UserMapper::userToUserDTO).collect(Collectors.toList()));
        }
        return resultMap;
    }

    public Map<UserDTO, List<CourseDTO>> getUserToCourseMapByRole(Role role) {
        Map<UserDTO, List<CourseDTO>> map = new HashMap<>();
        List<UserDTO> userDTOS = getUsersByRole(role);
        CoursesDao coursesDao = new CoursesDao(connectionPool);
        for (var user : userDTOS) {
            map.put(user,
                    coursesDao.getUserCourses(UserMapper.userDTOToUser(user))
                            .stream()
                            .map(CourseMapper::courseToCourseDTO)
                            .collect(Collectors.toList()));
        }

        return map;

    }

    public void addPhotoToUser(HttpServletRequest req, UserDTO userDTO) {
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
                        userDTO.setPhoto(fileInputStream);
                        userDao.updateUserPhoto(UserMapper.userDTOToUser(userDTO));
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


        return errorList;
    }

    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    public UserDTO findUser(String userLogin) {
        return UserMapper.userToUserDTO(userDao.findUser(userLogin));
    }

    public UserDTO identifyUser(String login, String pwd) {
        return UserMapper.userToUserDTO(userDao.identifyUser(login, pwd));
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public void deleteUser(UserDTO userDTO) {
        userDao.deleteUser(UserMapper.userDTOToUser(userDTO));
    }

    public String getUserRegisteredState(UserDTO userDTO, CourseDTO courseDTO) {
        return userDao.getUserRegisteredState(UserMapper.userDTOToUser(userDTO), CourseMapper.courseDTOToCourse(courseDTO));
    }

    public int getUserGradeForCourse(String login, String courseTitle) {
        return userDao.getUserGradeForCourse(login, courseTitle);
    }

    public void saveMarks(Map<Integer, List<UserDTO>> students, String courseTitle, int[] newMarks) {
        Map<Integer, List<User>> resultMap = new HashMap<>();
        for(var entry:students.entrySet()){
            resultMap.put(entry.getKey(), entry.getValue().stream().map(UserMapper::userDTOToUser).collect(Collectors.toList()));
        }
        userDao.saveMarks(resultMap, courseTitle, newMarks);
    }

    public Map<CourseDTO, List<UserDTO>> getAllRegisteredUserToTeacherCourses(UserDTO teacher) {
        Map<CourseDTO, List<UserDTO>> resultMap = new HashMap<>();
        CoursesDao coursesDao = new CoursesDao(connectionPool);

        for (var course : coursesDao.getAllTeacherCourses(UserMapper.userDTOToUser(teacher), 0,100)) {
            resultMap.put(CourseMapper.courseToCourseDTO(course), userDao.getRegisteredUserToCourse(course.getTitle())
                    .stream()
                    .map(UserMapper::userToUserDTO)
                    .collect(Collectors.toList()));

        }

        return resultMap;
    }

    private void resizeImage(String imagePathToRead,
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


    public void updateUserPassword(UserDTO user, String pwd) {
        userDao.updateUserPassword(UserMapper.userDTOToUser(user),pwd);
    }
}
