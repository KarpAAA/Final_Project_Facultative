package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.task.Task;
import com.example.final_project.database.entities.task.TaskBuilder;
import com.example.final_project.database.entities.user.User;


import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskDao which implements functions to interact with database
 */
public class TaskDao {

    private final ConnectionPool connectionPool;
    /**
     * @param connectionPool pool of connections used to request to database
     */
    public TaskDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * @param user whose solution will be taken
     * @param task solution to which task will be taken
     * @return solution in file(as byte[])
     */
    public byte[] getUserSolution(User user, Task task) {
        Connection connection = connectionPool.getConnection();
        byte[] solution = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT (file) FROM user_has_task " +
                    "WHERE User_login = ? AND Task_id = ?");
            statement.setString(1, user.getLogin());
            statement.setInt(2, task.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Blob blob = resultSet.getBlob(1);
                if (blob != null) solution = blob.getBinaryStream().readAllBytes();
            }
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return solution;
    }


    /**
     * @param course course to which task will be  taken
     * @return list of tasks related to course
     */
    public List<Task> getTaskByCourse(Course course) {
        List<Task> taskList = new ArrayList<>();
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Task WHERE Course_title = ?");
            statement.setString(1, course.getTitle());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                taskList.add(getTaskFromDb(resultSet));
            }
            connectionPool.releaseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return taskList;
    }

    /**
     * @param task item to be added to course
     * @param course to which task is related
     */
    public void addTaskToCourse(Task task, Course course) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Task VALUES(null,?,?,?)");
            statement.setString(1, course.getTitle());
            statement.setString(2, task.getTitle());
            statement.setString(3, task.getCondition());

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addStudentsToTask(lastInserterId(), task);
    }

    /**
     * function add item in table user_has_task
     * @param userLogin login of user(PK)
     * @param task task for which we create item in table
     */
    public void addStudentToTask(String userLogin, Task task) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user_has_task VALUES(?,?,null,null)");


            statement.setString(1, userLogin);
            statement.setInt(2, task.getId());


            statement.executeUpdate();
            connectionPool.releaseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param task to be deleted from database
     */
    public void deleteTaskFromCourse(Task task) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Task WHERE id = ?");
            statement.setInt(1, task.getId());

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param id of task(PK)
     * @param task new task object
     */
    public void updateTask(int id, Task task) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Task " +
                    "SET title = ?, Task.condition = ?" +
                    "WHERE id = ?");
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getCondition());
            statement.setInt(3, id);

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param user whose solution will be added
     * @param task to which is solution
     * @param file user solution as file(byte[])
     */
    public void addAnswerToTask(User user, Task task, byte[] file) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE User_has_task SET file = ?" +
                    "WHERE user_login = ? AND task_id = ?");

            statement.setBlob(1, new ByteArrayInputStream(file));
            statement.setString(2, user.getLogin());
            statement.setInt(3, task.getId());

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);

        } catch (SQLIntegrityConstraintViolationException e) {
            updateAnswerToTask(user, task, file);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param user whose solution will be updated
     * @param task to which is solution
     * @param file new user solution as file(byte[])
     */
    public void updateAnswerToTask(User user, Task task, byte[] file) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE User_has_task " +
                    "SET file = ? " +
                    "WHERE User_login = ? AND Task_id = ?");
            statement.setBlob(1, new ByteArrayInputStream(file));
            statement.setString(2, user.getLogin());
            statement.setInt(3, task.getId());


            statement.executeUpdate();
            connectionPool.releaseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param taskId id of task(PK)
     * @return task by id
     */
    public Task getTask(int taskId) {

        Task task = null;
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Task \n" +
                    "WHERE id = ?");
            statement.setInt(1, taskId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                task = getTaskFromDb(resultSet);
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return task;
    }

    /**
     * @param user whose mark will be selected
     * @param task to which mark will be selected
     * @return mark(Integer)
     */
    public Integer getUserGradeForTask(User user, Task task) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT (grade) FROM User_has_Task \n" +
                    "WHERE User_login = ? AND Task_id = ?");
            statement.setString(1, user.getLogin());
            statement.setInt(2, task.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                connectionPool.releaseConnection(connection);
                return resultSet.getInt(1);
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


    /**
     * @param resultSet received dataSet from database
     * @return formed task from Result Set
     */
    private Task getTaskFromDb(ResultSet resultSet) throws SQLException {
        TaskBuilder taskBuilder = new TaskBuilder();
        CoursesDao coursesDao = new CoursesDao(connectionPool);


        taskBuilder.setId(resultSet.getInt("id"))
                .setCourse(coursesDao.findCourse(resultSet.getString("Course_title")))
                .setTitle(resultSet.getString("title"))
                .setCondition(resultSet.getString("condition"));


        return taskBuilder.buildTask();
    }

    /**
     * function add items in table user_has_task
     * @param id task id(PK of task)
     * @param task task for which we create items in table
     */
    private void addStudentsToTask(int id, Task task) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO user_has_task VALUES(?,?,null,null)");

            for (var student : (new UserDao(connectionPool).selectUsersByCourse(task.getCourse())).entrySet()) {
                statement.setString(1, student.getKey().getLogin());
                statement.setInt(2, id);
                statement.addBatch();
            }

            statement.executeBatch();
            connectionPool.releaseConnection(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return last inserted id in database
     */
    private int lastInserterId() {
        int res = -1;
        try {
            Connection connection = connectionPool.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
            connectionPool.releaseConnection(connection);
            resultSet.next();
            res = resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
