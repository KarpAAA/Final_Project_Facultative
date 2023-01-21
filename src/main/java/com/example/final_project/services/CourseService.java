package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.database.entities.course.State;

import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.CourseMapper;
import com.example.final_project.utilities.CoursesFilter;
import com.example.final_project.utilities.UserMapper;
import com.example.final_project.validation.Validator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileExistsException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class CourseService {
    private final CoursesDao courseDao;
    private ConnectionPool connectionPool;

    public CourseService(ConnectionPool connectionPool) {
        this.courseDao = new CoursesDao(connectionPool);
        this.connectionPool = connectionPool;
    }

    public void addCourse(Course course) {
        courseDao.addCourse(course);
    }

    public CourseDTO getCourseByTitle(String courseTitle) {
        return CourseMapper.courseToCourseDTO(courseDao.findCourse(courseTitle));
    }

    public List<String> validateCourse(Course course) {
        List<String> errorList = new ArrayList<>();
        Validator validator = new Validator();
        errorList.addAll(validator.validateCourse(connectionPool, course));
        if (course.getStartDate() == null) {
            errorList.add("startDate");
        }
        if (course.getFinishDate() == null) {
            errorList.add("finishDate");
        }
        if (course.getStartDate() != null && course.getFinishDate() != null) {
            if (course.getStartDate().toString().compareToIgnoreCase(course.getFinishDate().toString()) > 0) {
                if (!errorList.contains("finishDate")) errorList.add("finishDate");
            } else {
                State state = State.NotStarted;
                java.util.Date date = new java.util.Date();
                if (date.after(course.getStartDate()) && date.after(course.getFinishDate())) {
                    state = State.Finished;
                } else if (date.after(course.getFinishDate()) && date.before(course.getStartDate())) {
                    state = State.NotStarted;
                } else if (date.after(course.getStartDate()) && date.before(course.getFinishDate())) {
                    state = State.InProgress;
                }
                course.setState(state);
            }
        }

        return errorList;
    }

    public void addPhotoToCourse(HttpServletRequest req, CourseDTO courseDTO) {
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
                        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\ivank\\IdeaProjects\\Final_Project\\src\\main\\webapp\\userImages\\" + fileItem.getName());
                        courseDTO.setPhoto(fileInputStream.readAllBytes());
                        courseDao.setPhotoToCourse(CourseMapper.courseDTOToCourse(courseDTO));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public State validateCourseState(String courseState) {
        if (courseState == null) return State.NotStarted;
        if (courseState.compareTo(State.NotStarted.toString()) == 0) return State.NotStarted;
        else if (courseState.compareTo(State.InProgress.toString()) == 0) return State.InProgress;
        else return State.Finished;
    }

    public void addOrBanUserToCourse(String ifAdd, String userLogin, String courseTitle) {
        if (ifAdd.compareTo("true") == 0) {
            courseDao.addStudentToCourse(userLogin, courseTitle);
        } else {
            courseDao.blockStudentToCourse(userLogin, courseTitle);
        }
    }

    public void updateCourse(ConnectionPool connectionPool, Course course) {
        courseDao.updateCourse(course);
    }

    public CourseDTO findCourse(String title) {
        return CourseMapper.courseToCourseDTO(courseDao.findCourse(title));
    }

    public List<CourseDTO> selectCoursesByCondition(CoursesFilter coursesFilter) {
        return courseDao.selectCoursesByCondition(coursesFilter)
                .stream()
                .map(CourseMapper::courseToCourseDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllTopics() {
        return courseDao.getAllTopics();
    }

    public void deleteCourse(String title) {
        courseDao.deleteCourse(title);
    }

    public void registerStudentToCourse(String login, String course) {
        courseDao.registerStudentToCourse(login, course);
    }

    public int getCoursesAmount() {
        return courseDao.getCoursesAmount();
    }

    public void removeStudentFromCourse(String login, String courseTitle) {
        courseDao.removeStudentFromCourse(login, courseTitle);
    }

    public List<CourseDTO> getAllTeacherCourses(UserDTO userDTO, int i, int recordsPerPage) {
        return courseDao.getAllTeacherCourses(UserMapper.userDTOToUser(userDTO), i,recordsPerPage)
                .stream()
                .map(CourseMapper::courseToCourseDTO)
                .collect(Collectors.toList());
    }

    public void changeCoursesState() {
        courseDao.changeCoursesState();
    }

    public List<CourseDTO> selectStatedAmountOfUserCourses(UserDTO userDTO, int i, int recordsPerPage, State state) {
        return courseDao.selectStatedAmountOfUserCourses(UserMapper.userDTOToUser(userDTO), i, recordsPerPage, state)
                .stream()
                .map(CourseMapper::courseToCourseDTO)
                .collect(Collectors.toList());
    }

    public int getTeacherCoursesAmount() {
        return courseDao.getTeacherCoursesAmount();
    }
}