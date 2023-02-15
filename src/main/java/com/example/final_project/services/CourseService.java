package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.MessageDao;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.database.entities.course.State;

import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.LoggingManager;
import com.example.final_project.utilities.mappers.CourseMapper;
import com.example.final_project.utilities.CoursesFilter;
import com.example.final_project.utilities.mappers.MeetingMapper;
import com.example.final_project.utilities.mappers.UserMapper;
import com.example.final_project.validation.Validator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileExistsException;
import org.mapstruct.factory.Mappers;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Course services as additional layer to communicate with database and controlled
 */
public class CourseService {
    private final CoursesDao courseDao;
    private final ConnectionPool connectionPool;
    private final CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

    /**
     * @param connectionPool pool of connections used to request to database
     */
    public CourseService(ConnectionPool connectionPool) {
        this.courseDao = new CoursesDao(connectionPool);
        this.connectionPool = connectionPool;
    }

    /**
     * @param course to be inserted to database
     */
    public void addCourse(Course course) {
        LoggingManager.logAuditTrail(LoggingManager.Change.CREATED,courseMapper.courseToCourseDTO(course));
        courseDao.addCourse(course);
    }

    /**
     * @param courseTitle title of course to be found
     * @return found course
     */
    public CourseDTO getCourseByTitle(String courseTitle) {
        return courseMapper.courseToCourseDTO(courseDao.findCourse(courseTitle));
    }

    /**
     * @param course course to be validated
     * @return list of found errors as string list
     */
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

    /**
     * @param req source of file
     * @param courseDTO course to which photo is related
     */
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
                        courseDao.setPhotoToCourse(courseMapper.courseDTOToCourse(courseDTO));
                        LoggingManager.logAuditTrail(LoggingManager.Change.UPDATED,courseDTO);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param courseState state as string
     * @return state as State enum
     */
    public State validateCourseState(String courseState) {
        if (courseState == null) return State.NotStarted;
        if (courseState.compareTo(State.NotStarted.toString()) == 0) return State.NotStarted;
        else if (courseState.compareTo(State.InProgress.toString()) == 0) return State.InProgress;
        else return State.Finished;
    }

    /**
     * @param ifAdd string representation of choice
     * @param userLogin user to be interacted with
     * @param courseTitle title of course
     */
    public void addOrBanUserToCourse(String ifAdd, String userLogin, String courseTitle) {
        if (ifAdd.compareTo("true") == 0) {
            new MessagesService(connectionPool)
                    .notifyUserAddingToCourse( new UserService(connectionPool).findUser(userLogin), findCourse(courseTitle));
            courseDao.addStudentToCourse(userLogin, courseTitle);
        } else {
            courseDao.blockStudentToCourse(userLogin, courseTitle);
        }
    }

    /**
     * @param course new course object
     */
    public void updateCourse(Course course) {
        LoggingManager.logAuditTrail(LoggingManager.Change.UPDATED,courseMapper.courseToCourseDTO(course));
        courseDao.updateCourse(course);
    }

    /**
     * @param title course title(PK)
     * @return found course
     */
    public CourseDTO findCourse(String title) {
        return courseMapper.courseToCourseDTO(courseDao.findCourse(title));
    }

    /**
     * @param coursesFilter custom filter of courses
     * @return list of courses by entered filter
     */
    public List<CourseDTO> selectCoursesByCondition(CoursesFilter coursesFilter) {
        return courseDao.selectCoursesByCondition(coursesFilter)
                .stream()
                .map(courseMapper::courseToCourseDTO)
                .collect(Collectors.toList());
    }

    /**
     * @return list of all existing topics in database
     */
    public List<String> getAllTopics() {
        return courseDao.getAllTopics();
    }

    /**
     * @param title of course to be deleted
     */
    public void deleteCourse(String title) {
        LoggingManager.logAuditTrail(LoggingManager.Change.UPDATED,findCourse(title));
        courseDao.deleteCourse(title);
    }

    /**
     * @param login student login
     * @param course to which user will be registered
     */
    public void registerStudentToCourse(String login, String course) {
        courseDao.registerStudentToCourse(login, course);
    }

    public int getCoursesAmount() {
        return courseDao.getCoursesAmount();
    }

    /**
     * @param login user login to be removed from course
     * @param courseTitle course title(PK) user to be removed from
     */
    public void removeStudentFromCourse(String login, String courseTitle) {
        courseDao.removeStudentFromCourse(login, courseTitle);
    }

    /**
     * @param userDTO teacher whose courses will be selected
     * @param i start position of selection
     * @param recordsPerPage range of selection
     * @return list of courses
     */
    public List<CourseDTO> getAllTeacherCourses(UserDTO userDTO, int i, int recordsPerPage) {
        return courseDao.getAllTeacherCourses(Mappers.getMapper(UserMapper.class).userDTOToUser(userDTO), i,recordsPerPage)
                .stream()
                .map(courseMapper::courseToCourseDTO)
                .collect(Collectors.toList());
    }

    public void changeCoursesState() {
        courseDao.changeCoursesState();
    }

    /**
     * @param userDTO user whose courses will be selected
     * @param i start position of selection
     * @param recordsPerPage range of selection
     * @param state course state
     * @return list of user courses with stated course state
     */
    public List<CourseDTO> selectStatedAmountOfUserCourses(UserDTO userDTO, int i, int recordsPerPage, State state) {
        return courseDao.selectStatedAmountOfUserCourses(Mappers.getMapper(UserMapper.class).userDTOToUser(userDTO), i, recordsPerPage, state)
                .stream()
                .map(courseMapper::courseToCourseDTO)
                .collect(Collectors.toList());
    }

    public int getTeacherCoursesAmount() {
        return courseDao.getTeacherCoursesAmount();
    }

    /**
     * @param courseDTO course to be bought by user
     * @param user who will buy course
     */
    public synchronized void userBuyCourse(CourseDTO courseDTO, UserDTO user) {
        courseDao.userBuyCourse(courseMapper.courseDTOToCourse(courseDTO),
                Mappers.getMapper(UserMapper.class).userDTOToUser(user));
    }
}