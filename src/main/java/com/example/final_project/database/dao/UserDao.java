package com.example.final_project.database.dao;


import com.example.final_project.database.connection.ConnectionPool;

import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.task.Task;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.database.entities.user.UserBuilder;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserDao which implements functions to interact with database
 */
public class UserDao {
    private ConnectionPool connectionPool;

    /**
     * @param connectionPool pool of connections used to request to database
     */
    public UserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


    /**
     * @param resultSet received dataSet from database
     * @return formed user from Result Set
     */
    private User getUserFromDb(ResultSet resultSet) throws SQLException {
        Blob blob = resultSet.getBlob("photo");
        UserBuilder userBuilder = new UserBuilder();

        userBuilder.setLogin(resultSet.getString("login"))
                .setPassword(resultSet.getString("password"))
                .setName(resultSet.getString("name"))
                .setEmail(resultSet.getString("email"))
                .setRole(indentifyRole(resultSet.getInt("Role_id")))
                .setAge(resultSet.getInt("age"))
                .setSurname(resultSet.getString("surname"))
                .setRegistrationDate(resultSet.getDate("registration_date"))
                .setPhone(resultSet.getString("phone"))
                .setPhoto(blob == null ? null : blob.getBytes(1, (int) blob.length()))
                .setBlocked_state(indentifyBlockedState(resultSet.getInt("Block_state_id")))
                .setBalance(resultSet.getInt("balance"));

        return userBuilder.getUser();
    }

    /**
     * used to identify user for signing in
     * @param login user login
     * @param password user password
     * @return user if such exists else return null
     */
    public User identifyUser(String login, String password) {

        User user = null;
        try {

            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User \n" +
                    "LEFT JOIN additional_info ON User.login = additional_info.User_login\n" +
                    "LEFT JOIN role ON User.role_id = role.id\n" +
                    "WHERE login  = ? AND password = ?");
            statement.setString(1, login);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                user = getUserFromDb(resultSet);
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    /**
     * @param login user login
     * @return user by stated login(PK)
     */
    public User getUser(String login) {

        User user = null;
        try {

            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User \n" +
                    "LEFT JOIN additional_info ON User.login = additional_info.User_login\n" +
                    "LEFT JOIN role ON User.role_id = role.id\n" +
                    "WHERE login  = ?");
            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                user = getUserFromDb(resultSet);
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    /**
     * @param user to be inserted
     */
    public void insertUser(User user) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT into User (login,password,name,email,role_id) VALUES(?,?,?,?,?)");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getEmail());
            statement.setInt(5, user.getRole().ordinal());
            statement.executeUpdate();
            addAdditionalFieldsToUser(user);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * @param user to be updated
     */
    public void updateUser(User user) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE user " +
                    "SET name = ? , email = ? WHERE login = ?");


            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getLogin());
            statement.executeUpdate();

            updateAdditionalFieldsToUser(connection, user);
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    /**
     * @param user to be deleted
     */
    public void deleteUser(User user) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE from user where login = ?");
            statement.setString(1, user.getLogin());
            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * @param user whose photo to beb updated
     */
    public void updateUserPhoto(User user) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE additional_info SET photo = ? " +
                    "WHERE User_login = ?");
            statement.setBinaryStream(1, new ByteArrayInputStream(user.getPhoto()));
            statement.setString(2, user.getLogin());
            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param user whose additional fields to be added
     */
    private void addAdditionalFieldsToUser(User user) throws SQLException {
        Connection connection = connectionPool.getConnection();

        PreparedStatement statement = connection.prepareStatement("INSERT into Additional_Info (User_login,age,surname,registration_date,phone, photo) VALUES(?,?,?,?,?,?)");
        statement.setString(1, user.getLogin());
        statement.setInt(2, user.getAge());
        statement.setString(3, user.getSurname());
        statement.setDate(4, user.getRegistrationDate());
        statement.setString(5, user.getPhone());
        statement.setBlob(6, new ByteArrayInputStream(user.getPhoto()));
        statement.executeUpdate();

        connectionPool.releaseConnection(connection);
    }
    /**
     * @param user whose additional fields to be updated
     */
    private void updateAdditionalFieldsToUser(Connection connection, User user) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE Additional_Info " +
                "SET age = ?, " +
                "surname = ?, " +
                "phone = ? " +
                "WHERE user_login = ?");

        statement.setInt(1, user.getAge());
        statement.setString(2, user.getSurname());
        statement.setString(3, user.getPhone());
        statement.setString(4, user.getLogin());


        statement.executeUpdate();

    }

    /**
     * @param user whose password to be updated
     * @param pwd new password
     */
    public void updateUserPassword(User user, String pwd) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE User SET password = ? " +
                    "WHERE login = ?");
            statement.setString(1, pwd);
            statement.setString(2, user.getLogin());
            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param role of users to be selected
     * @return list of users with stated role
     */
    public List<User> getUsersByRole(Role role) {

        List<User> userList = new ArrayList<>();
        try {

            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM User \n" +
                    "LEFT JOIN additional_info ON User.login = additional_info.User_login\n" +
                    "LEFT JOIN role ON User.role_id = role.id\n" +
                    "WHERE Role_id  = ?");
            statement.setInt(1, role.ordinal());


            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userList.add(getUserFromDb(resultSet));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }

    /**
     * @return list of all existing users
     */
    public List<User> selectAllUsers() {
        Connection connection = connectionPool.getConnection();
        List<User> userList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM User \n" +
                    "LEFT JOIN additional_info ON User.login = additional_info.User_login\n" +
                    "LEFT JOIN role ON User.role_id = role.id\n");

            while (resultSet.next()) {
                userList.add(getUserFromDb(resultSet));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    /**
     * @param course to which students will be selected
     * @return map of <student -> mark for course>
     */
    public Map<User, Integer> selectUsersByCourse(Course course) {

        Connection connection = connectionPool.getConnection();
        Map<User, Integer> userMap = new HashMap<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT user_login, grade FROM user_has_course " +
                    "WHERE course_title = ? AND user_state = 'Allowed'");
            statement.setString(1, course.getTitle());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userMap.put(getUser(resultSet.getString(1)), resultSet.getInt(2));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userMap;
    }

    /**
     * @param courseTitle course title(PK) to which will be selected registered users
     * @return registered users for course
     */
    public List<User> getRegisteredUserToCourse(String courseTitle) {
        Connection con = connectionPool.getConnection();
        List<User> userList = new ArrayList<>();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT (user_login) FROM user_has_course " +
                    "WHERE user_state = 'Registered' AND course_title = ?");
            statement.setString(1, courseTitle);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                userList.add(getUser(resultSet.getString(1)));
            }

            connectionPool.releaseConnection(con);
            return userList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param course for which mark will be placed
     * @param user whose mark will be placed
     * @param taskGrades map of <Task -> mark for task>
     */
    private void setCourseMarkToUser(Course course, User user, Map<Task, Integer> taskGrades) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE user_has_course SET grade = ?" +
                    " WHERE user_login = ? AND course_title = ?");

            statement.setInt(1, calculateGrade(taskGrades));
            statement.setString(2, user.getLogin());
            statement.setString(3, course.getTitle());


            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int calculateGrade(Map<Task, Integer> taskGrades) {
        double generalMark = 0;
        for (var mark : taskGrades.values()) {
            generalMark += mark;
        }
        return (int) Math.round(generalMark / taskGrades.size());
    }

    /**
     * @param userMap map of <User to map of <Task -> mark for task> >
     */
    public void saveTaskMarks(Map<User, Map<Task, Integer>> userMap) {

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE user_has_task SET grade = ?" +
                    " WHERE user_login = ? AND task_id = ?");
            Course course = null;
            for (var entry : userMap.entrySet()) {
                for (var entry1 : entry.getValue().entrySet()) {
                    course = entry1.getKey().getCourse();
                    statement.setInt(1, entry1.getValue());
                    statement.setString(2, entry.getKey().getLogin());
                    statement.setInt(3, entry1.getKey().getId());
                    statement.addBatch();

                }
                if (course != null) setCourseMarkToUser(course, entry.getKey(), entry.getValue());
            }

            statement.executeBatch();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param user whose blocked state will be updated
     * @param blockedState new user blocked state
     */
    public void updateBlockedState(User user, Blocked_State blockedState) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE additional_info SET Block_state_id = ?" +
                    " WHERE User_login = ?");

            statement.setInt(1, blockedState.ordinal());
            statement.setString(2, user.getLogin());

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param login which will be checked
     * @return if login available
     */
    public boolean checkIfLoginAvailable(String login) {
        Connection con = connectionPool.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM User WHERE login=?");
            statement.setString(1, login);
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

    /**
     * @param user user whose state will be selected
     * @param course course to which state will be selected
     * @return user state to course
     */
    public String getUserRegisteredState(User user, Course course) {
        Connection con = connectionPool.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT (user_state) FROM user_has_course " +
                    "WHERE user_login = ? AND course_title = ?");
            statement.setString(1, user.getLogin());
            statement.setString(2, course.getTitle());
            ResultSet resultSet = statement.executeQuery();
            String userState = null;
            while (resultSet.next()) {
                userState = resultSet.getString(1);
            }

            connectionPool.releaseConnection(con);
            return userState;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param userLogin whose grade will be chosen
     * @param courseTitle to which mark will be chosen
     * @return user grade for course
     */
    public int getUserGradeForCourse(String userLogin, String courseTitle) {
        Connection con = connectionPool.getConnection();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT (grade) FROM user_has_course " +
                    "WHERE user_login=? AND course_title = ?");
            statement.setString(1, userLogin);
            statement.setString(2, courseTitle);
            ResultSet resultSet = statement.executeQuery();
            int grade = 0;
            while (resultSet.next()) {
                grade = resultSet.getInt(1);
            }

            connectionPool.releaseConnection(con);
            return grade;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Role indentifyRole(int role) {

        if (Role.Teacher.ordinal() == role) return Role.Teacher;
        else if (Role.Admin.ordinal() == role) return Role.Admin;
        else return Role.Student;

    }

    private Blocked_State indentifyBlockedState(int state) {

        if (Blocked_State.BLOCKED.ordinal() == state) return Blocked_State.BLOCKED;
        else return Blocked_State.UNLOCKED;

    }


    /**
     * @param user whose balance will be updated
     * @param sum on which balance will be updated
     */
    public void updateUserBalance(User user, int sum) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Additional_Info " +
                    "SET balance = ? " +
                    "WHERE User_login = ?");

            statement.setInt(1, user.getBalance() + sum);
            statement.setString(2, user.getLogin());
            statement.executeUpdate();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
