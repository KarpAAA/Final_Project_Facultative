package com.example.final_project.database.dao;


import com.example.final_project.controller.session.exceptions.NoAccessProvidedException;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.utilities.CoursesFilter;


import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CoursesDao {
    private int coursesAmount;
    private int teacherCoursesAmount;
    private final ConnectionPool connectionPool;

    public CoursesDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public int getCoursesAmount() {
        return coursesAmount;
    }
    public int getTeacherCoursesAmount() {
        return teacherCoursesAmount;
    }


    public void addCourse(Course course) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Course " +
                    "(title, start_date,finish_date,max_students_amount,topic,price,students_amount, description, State_id, teacher, duration, photo) "+
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ");

            statement.setString(1, course.getTitle());
            statement.setDate(2, course.getStartDate());
            statement.setDate(3, course.getFinishDate());
            statement.setInt(4, course.getMaxStudentsAmount());
            statement.setString(5, course.getTopic());
            statement.setInt(6, course.getPrice());
            statement.setInt(7, course.getCurrentStudentsAmount());
            statement.setString(8, course.getDescription());

            statement.setInt(9, course.getState().ordinal());
            statement.setString(10, course.getTeacher().getLogin());
            statement.setInt(11, course.duration());
            statement.setBinaryStream(12, new ByteArrayInputStream(course.getPhoto()));

            statement.executeUpdate();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateCourse(Course course) {

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Course" +
                    " SET start_date = ?, finish_date = ?, max_students_amount = ?, topic = ?, price = ?," +
                    " description = ?, teacher = ?, duration = ?, State_id = ?" +
                    " WHERE title = ?");


            statement.setDate(1, course.getStartDate());
            statement.setDate(2, course.getFinishDate());
            statement.setInt(3, course.getMaxStudentsAmount());
            statement.setString(4, course.getTopic());
            statement.setInt(5, course.getPrice());
            statement.setString(6, course.getDescription());
            statement.setString(7, course.getTeacher().getLogin());
            statement.setInt(8, course.duration());
            statement.setInt(9, course.getState().ordinal());
            statement.setString(10, course.getTitle());


            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteCourse(String courseTitle) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Course WHERE title = ?");

            statement.setString(1, courseTitle);
            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setPhotoToCourse(Course course) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Course" +
                    " SET photo = ? WHERE title = ?");


            statement.setBinaryStream(1, new ByteArrayInputStream(course.getPhoto()));
            statement.setString(2, course.getTitle());

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Course findCourse(String title) {

        Course course = null;
        try {

            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Course \n" +
                    "LEFT join state ON state.id = Course.state_id\n" +
                    "WHERE title = ?");
            statement.setString(1, title);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                course = getCourse(resultSet);
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return course;
    }

    public List<Course> selectAllCourses() {

        Connection connection = connectionPool.getConnection();
        List<Course> courseList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Course LEFT join state ON state.id = Course.state_id");

            while (resultSet.next()) {

                courseList.add(getCourse(resultSet));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }
    public List<Course> selectCoursesByCondition(CoursesFilter coursesFilter) {
        List<Course> list = new ArrayList<>();
        try {
            Connection connection = connectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SQL_CALC_FOUND_ROWS * FROM Course LEFT join state ON state.id = Course.state_id " + coursesFilter.formCondition());
            while (resultSet.next()) {

                list.add(getCourse(resultSet));
            }
            resultSet.close();
            resultSet = statement.executeQuery("SELECT FOUND_ROWS()");

            if (resultSet.next())
                this.coursesAmount = resultSet.getInt(1);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
    public List<Course> selectStatedAmountOfUserCourses(User user, int offset, int amount, State state) {
        List<Course> list = new ArrayList<>();
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT SQL_CALC_FOUND_ROWS * FROM Course " +
                    "LEFT join user_has_course ON user_has_course.course_title = Course.title " +
                    "WHERE user_login = ? AND user_state = 'Allowed' AND State_id = ?" +
                    " LIMIT " + offset + ", " + amount);
            statement.setString(1, user.getLogin());
            statement.setInt(2, state.ordinal());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                list.add(getCourse(resultSet));
            }
            resultSet.close();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void registerStudentToCourse(String userLogin, String courseTitle) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT into user_has_course (user_login, course_title, " +
                    "registration_date) VALUES(?,?,?)");
            statement.setString(1, userLogin);
            statement.setString(2, courseTitle);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            statement.setDate(3, java.sql.Date.valueOf(formatter.format(date)));

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public synchronized void userBuyCourse(Course course, User user){
        TaskDao taskDao = new TaskDao(connectionPool);
        try {
            if(course.getMaxStudentsAmount()<=course.getCurrentStudentsAmount()
            || course.getPrice()>user.getBalance()) throw new NoAccessProvidedException();

            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT into user_has_course  VALUES(?,?,?,?,?)");
            statement.setString(1, user.getLogin());
            statement.setString(2, course.getTitle());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = new java.util.Date();
            statement.setDate(3, java.sql.Date.valueOf(formatter.format(date)));
            statement.setInt(4,0);
            statement.setString(5,"Allowed");
            statement.executeUpdate();
            increaseStudentsAmount(course);
            taskDao.getTaskByCourse(course).forEach(task -> taskDao.addStudentToTask(user.getLogin(),task));
            connectionPool.releaseConnection(connection);
            new UserDao(connectionPool).updateUserBalance(user,(course.getPrice() * -1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean addStudentToCourse(String userLogin, String courseTitle) {
        Course course = findCourse(courseTitle);
        TaskDao taskDao = new TaskDao(connectionPool);
        if (course.getCurrentStudentsAmount() >= course.getMaxStudentsAmount()) return false;
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE user_has_course SET user_state = 'Allowed'" +
                    "WHERE user_login = ? AND course_title = ?");
            statement.setString(1, userLogin);
            statement.setString(2, courseTitle);

            statement.executeUpdate();
            increaseStudentsAmount(course);
            taskDao.getTaskByCourse(course).forEach(task -> taskDao.addStudentToTask(userLogin,task));
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public void removeStudentFromCourse(String userLogin, String courseTitle) {

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM user_has_course WHERE user_login = ? AND course_title = ?");
            statement.setString(1, userLogin);
            statement.setString(2, courseTitle);

            statement.executeUpdate();
            decreaseStudentsAmount(findCourse(courseTitle));
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void blockStudentToCourse(String userLogin, String courseTitle) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE user_has_course SET user_state = 'Blocked'" +
                    "WHERE user_login = ? AND course_title = ?");
            statement.setString(1, userLogin);
            statement.setString(2, courseTitle);

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void changeCoursesState() {
        List<Course> courseList = selectAllCourses();
        java.util.Date date = new java.util.Date();

        courseList = courseList.stream().filter(course -> {
            return !(
                    (date.after(course.getStartDate()) && date.after(course.getFinishDate()) && course.getState() == State.Finished)
                            || (date.before(course.getFinishDate()) && date.before(course.getStartDate()) && course.getState() == State.NotStarted)
                            || (date.after(course.getStartDate()) && date.before(course.getFinishDate()) && course.getState() == State.InProgress)
            );
        }).collect(Collectors.toList());

        for (Course value : courseList) {
            if (date.after(value.getStartDate()) && date.after(value.getFinishDate())) {
                value.setState(State.Finished);
            } else if (date.before(value.getFinishDate()) && date.before(value.getStartDate())) {
                value.setState(State.NotStarted);
            } else if (date.after(value.getStartDate()) && date.before(value.getFinishDate())) {
                value.setState(State.InProgress);
            }
        }

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Course SET State_id = ? WHERE title = ?");

            for (var course : courseList) {
                statement.setInt(1, course.getState().ordinal());
                statement.setString(2, course.getTitle());
                statement.addBatch();
            }
            statement.executeBatch();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> getAllTeacherCourses(User user, int offset, int amount) {
        Connection connection = connectionPool.getConnection();
        List<Course> courseList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT SQL_CALC_FOUND_ROWS * FROM Course LEFT join state ON state.id = Course.state_id WHERE Course.teacher = ?"
            + " LIMIT " + offset + ", " + amount);
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                courseList.add(getCourse(resultSet));
            }
            resultSet.close();
            resultSet = statement.executeQuery("SELECT FOUND_ROWS()");

            if (resultSet!=null && resultSet.next())
                this.teacherCoursesAmount = resultSet.getInt(1);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }
    public List<Course> getUserCourses(User user) {
        Connection connection = connectionPool.getConnection();
        List<Course> courseList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT (Course_title) FROM User_has_course " +
                    "WHERE User_login = ?");
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                courseList.add(findCourse(resultSet.getString(1)));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courseList;
    }
    public List<String> getAllTopics() {
        Connection connection = connectionPool.getConnection();
        Set<String> topicsList = new HashSet<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT (topic) FROM Course");

            while (resultSet.next()) {
                topicsList.add(resultSet.getString(1));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topicsList.stream().toList();
    }


    private Course getCourse(ResultSet resultSet) throws SQLException {

        Blob blob = resultSet.getBlob(12);
        UserDao userDao = new UserDao(connectionPool);
        User teacher = userDao.getUser(resultSet.getString(10));

        return new Course(
                resultSet.getString(1),
                resultSet.getString(5),
                resultSet.getString(8),
                teacher,
                resultSet.getDate(2),
                resultSet.getDate(3),
                resultSet.getInt(4),
                resultSet.getInt(7),
                resultSet.getInt(6),
                indentifyState(resultSet.getString(14)),
                blob == null ? null:blob.getBytes(1,(int) blob.length())

        );

    }

    public  boolean checkIfTitleAvailable(String title) {
        Connection con = connectionPool.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM Course WHERE title=?");
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            int counter = 0;
            while (resultSet.next()) {
                counter++;
            }

            connectionPool.releaseConnection(con);
            return counter == 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void increaseStudentsAmount(Course course) {

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Course SET students_amount = ?" +
                    " WHERE title = ?");
            statement.setInt(1, course.getCurrentStudentsAmount() + 1);
            statement.setString(2, course.getTitle());

            statement.executeUpdate();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void decreaseStudentsAmount(Course course) {

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Course SET students_amount = ?" +
                    " WHERE title = ?");
            statement.setInt(1, course.getCurrentStudentsAmount() - 1);
            statement.setString(2, course.getTitle());

            statement.executeUpdate();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private State indentifyState(String string) {
        if (string == null) return null;
        if (string.compareToIgnoreCase("In progress") == 0) return State.InProgress;
        else if (string.compareToIgnoreCase("Not begun") == 0) return State.NotStarted;
        else return State.Finished;
    }

}
