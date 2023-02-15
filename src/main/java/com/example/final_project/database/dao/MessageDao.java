package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.message.Message;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.message.MessageBuilder;
import com.example.final_project.database.entities.message.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageDao which implements functions to interact with database
 */
public class MessageDao {
    private ConnectionPool connectionPool;

    /**
     * @param connectionPool pool of connections used to request to database
     */
    public MessageDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Select messages by user who received
     *
     * @param receiver user who received message
     * @return user messages
     */
    public List<Message> findMessagesByReceiver(User receiver) {
        Connection connection = connectionPool.getConnection();
        List<Message> messagesList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Message WHERE receiver = ?");
            statement.setString(1, receiver.getLogin());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                messagesList.add(getMessage(resultSet));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messagesList;
    }

    /**
     * Select messages by user who sent
     *
     * @param sender user who sent message
     * @return user messages
     */
    public List<Message> findMessagesBySender(User sender) {
        Connection connection = connectionPool.getConnection();
        List<Message> messagesList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Message WHERE sender = ?");
            statement.setString(1, sender.getLogin());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                messagesList.add(getMessage(resultSet));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messagesList;
    }

    /**
     * Add meeting object to databse
     *
     * @param message which will be added to database
     */
    public void sendMessage(Message message) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT into Message (id,sender,receiver,text,subject,status) VALUES(null,?,?,?,?,?)");


            statement.setString(1, message.getSender().getLogin());
            statement.setString(2, message.getReceiver().getLogin());
            statement.setString(3, message.getText());
            statement.setString(4, message.getSubject());
            statement.setString(5, message.getStatus());

            statement.executeUpdate();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Delete all user(receiver) messages
     *
     * @param user whose messages will be deleted
     */
    public void clearUserMessages(User user) {

        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection
                    .prepareStatement("DELETE FROM Message WHERE receiver = ?");

            statement.setString(1, user.getLogin());

            statement.executeUpdate();
            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Change messages status to READ
     *
     * @param messageList messages state of which will be changed
     */
    public void changeStatus(List<Message> messageList) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE message SET status = 'read' WHERE id = ?");


            for (var message : messageList) {
                if (message.getStatus().compareToIgnoreCase("unread") == 0) {
                    statement.setInt(1, message.getId());
                    statement.executeUpdate();
                }
            }


            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param resultSet received dataSet from database
     * @return formed message from Result Set
     */
    private Message getMessage(ResultSet resultSet) throws SQLException {
        UserDao userDao = new UserDao(connectionPool);
        User sender = userDao.getUser(resultSet.getString("sender"));
        User receiver = userDao.getUser(resultSet.getString("receiver"));


        return new Message(
                resultSet.getInt("id"),
                resultSet.getString("text"),
                resultSet.getString("subject"),
                sender,
                receiver,
                validateStatus(resultSet.getString("status")).name()

        );
    }

    /**
     * @param status string from of status
     * @return Status option by string
     */
    private static Status validateStatus(String status) {
        if (status.compareToIgnoreCase("read") == 0) return Status.READ;
        else return Status.UNREAD;
    }

}
