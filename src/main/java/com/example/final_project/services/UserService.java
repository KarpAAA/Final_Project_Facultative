package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.database.entities.task.Task;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.dto.TaskDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.MailManager;
import com.example.final_project.utilities.mappers.CourseMapper;
import com.example.final_project.utilities.mappers.TaskMapper;
import com.example.final_project.utilities.mappers.UserMapper;
import com.example.final_project.validation.Validator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileExistsException;
import org.mapstruct.factory.Mappers;

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
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private ConnectionPool connectionPool;

    public UserService(ConnectionPool connectionPool) {
        this.userDao = new UserDao(connectionPool);
        this.connectionPool = connectionPool;
    }

    public List<UserDTO> getUsersByRole(Role role) {
        return userDao.getUsersByRole(role).stream().map(userMapper::userToUserDTO).collect(Collectors.toList());
    }

    public UserDTO blockOrUnlockStudent(String toDo, String login) {
        UserDTO userDTO = findUser(login);
        if (toDo != null && toDo.compareTo("block") == 0) {
            userDao.updateBlockedState(userMapper.userDTOToUser(userDTO), Blocked_State.BLOCKED);
        } else {
            userDao.updateBlockedState(userMapper.userDTOToUser(userDTO), Blocked_State.UNLOCKED);
        }
        return userDTO;
    }

    public Map<UserDTO, Integer> getUsersMarksMap(CourseDTO courseDTO) {
        Map<User, Integer> map = userDao.selectUsersByCourse(Mappers.getMapper(CourseMapper.class).courseDTOToCourse(courseDTO));
        Map<UserDTO, Integer> resultMap = new HashMap<>();
        for (var entry : map.entrySet()) {
            resultMap.put(userMapper.userToUserDTO(entry.getKey())
                    , entry.getValue());

        }
        return resultMap;
    }

    public Map<UserDTO, List<CourseDTO>> getUserToCourseMapByRole(Role role) {
        Map<UserDTO, List<CourseDTO>> map = new HashMap<>();
        List<UserDTO> userDTOS = getUsersByRole(role);

        CoursesDao coursesDao = new CoursesDao(connectionPool);
        for (var user : userDTOS) {
            map.put(user,
                    coursesDao.getUserCourses(userMapper.userDTOToUser(user))
                            .stream()
                            .map(Mappers.getMapper(CourseMapper.class)::courseToCourseDTO)
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
                                , "C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\userImages\\" + "500x500." + fileItem.getName()
                                , 500, 500);
                        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\userImages\\" + "500x500." + fileItem.getName());
                        userDTO.setPhoto(fileInputStream);
                        userDao.updateUserPhoto(userMapper.userDTOToUser(userDTO));
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
        UserDTO userDTO = userMapper.userToUserDTO(user);
        userDao.insertUser(user);
        new MessagesService(connectionPool)
                .notifyStudentsAboutSuccessfullyRegistration(userDTO);
        messageToEmail(userDTO);

    }

    private void messageToEmail(UserDTO user) {
        String text = "Dear " + user.getLogin() + "!" +
                "\nWelcome to our site!" +
                "\nWish you good using of provided resources." +
                "\nFeel free to contact us!";
        MailManager.writeMessage(text, "Registration", user.getEmail());

    }

    public UserDTO findUser(String userLogin) {
        return userMapper.userToUserDTO(userDao.findUser(userLogin));
    }

    public UserDTO identifyUser(String login, String pwd) {
        return userMapper.userToUserDTO(userDao.identifyUser(login, pwd));
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public void deleteUser(UserDTO userDTO) {
        userDao.deleteUser(userMapper.userDTOToUser(userDTO));
    }

    public String getUserRegisteredState(UserDTO userDTO, CourseDTO courseDTO) {
        return userDao.getUserRegisteredState(userMapper.userDTOToUser(userDTO), Mappers.getMapper(CourseMapper.class).courseDTOToCourse(courseDTO));
    }

    public int getUserGradeForCourse(String login, String courseTitle) {
        return userDao.getUserGradeForCourse(login, courseTitle);
    }

    public void saveMarks(Map<UserDTO, Map<TaskDTO,Integer>> newMarksMap, String courseTitle) {
        Map<User, Map<Task,Integer>> resultMap = new HashMap<>();
        for (var entry : newMarksMap.entrySet()) {
            Map<Task,Integer> taskMap = new HashMap<>();
            for (var entry1 : entry.getValue().entrySet()) {
                taskMap.put(Mappers.getMapper(TaskMapper.class).taskDTOToTask(entry1.getKey()),entry1.getValue());
            }
            resultMap.put(userMapper.userDTOToUser(entry.getKey()),taskMap);
        }
        userDao.saveTaskMarks(resultMap);
    }

    public Map<CourseDTO, List<UserDTO>> getAllRegisteredUserToTeacherCourses(UserDTO teacher) {
        Map<CourseDTO, List<UserDTO>> resultMap = new HashMap<>();
        CoursesDao coursesDao = new CoursesDao(connectionPool);

        for (var course : coursesDao.getAllTeacherCourses(userMapper.userDTOToUser(teacher), 0, 100)) {
            resultMap.put(Mappers.getMapper(CourseMapper.class).courseToCourseDTO(course), userDao.getRegisteredUserToCourse(course.getTitle())
                    .stream()
                    .map(userMapper::userToUserDTO)
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
        userDao.updateUserPassword(userMapper.userDTOToUser(user), pwd);
    }


    public Map<UserDTO, Map<TaskDTO, Integer>> getUserTasksMarksMap(CourseDTO courseDTO) {
        TaskService taskService = new TaskService(connectionPool);
        Map<UserDTO, Map<TaskDTO, Integer>> resultMap = new HashMap<>();

        Map<UserDTO, Integer> courseStudents = getUsersMarksMap(courseDTO);
        List<TaskDTO> tasksList = taskService.getTaskToCourse(courseDTO);

        for (var entry : courseStudents.entrySet()) {
            Map<TaskDTO, Integer> map = new HashMap<>();
            for (var task : tasksList) {
                map.put(task,taskService.getUserGradeForTask(entry.getKey(), task));
            }
            resultMap.put(entry.getKey(), map);
        }
        return resultMap;
    }
    public Map<UserDTO, Map<TaskDTO, byte[]>> getUserTasksSolutionMap(CourseDTO courseDTO) {
        TaskService taskService = new TaskService(connectionPool);
        Map<UserDTO, Map<TaskDTO, byte[]>> resultMap = new HashMap<>();

        Map<UserDTO, Integer> courseStudents = getUsersMarksMap(courseDTO);
        List<TaskDTO> tasksList = taskService.getTaskToCourse(courseDTO);

        for (var entry : courseStudents.entrySet()) {
            Map<TaskDTO, byte[]> map = new HashMap<>();
            for (var task : tasksList) {
                map.put(task,taskService.getUserSolution(entry.getKey(),task));
            }
            resultMap.put(entry.getKey(), map);
        }
        return resultMap;
    }

    public void updateBalance(UserDTO user, int sum) {
        userDao.updateUserBalance(userMapper.userDTOToUser(user),sum);
    }
}
