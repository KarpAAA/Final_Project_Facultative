package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.message.Message;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.message.MessageBuilder;
import com.example.final_project.database.entities.message.Status;
import com.example.final_project.dto.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    private ConnectionPool connectionPool;

    public MessageDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

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

    public void sendMessage(Message message) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT into Message (idMessage,value,subject,sender,receiver,status) VALUES(null,?,?,?,?,?)");

            statement.setString(1, message.getText());
            statement.setString(2, message.getSubject());
            statement.setString(3, message.getSender().getLogin());
            statement.setString(4, message.getReceiver().getLogin());
            statement.setString(5, message.getStatus());

            statement.executeUpdate();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void changeStatus(List<Message> messageList) {
        Connection connection = connectionPool.getConnection();


        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE message SET status = 'read'WHERE idMessage = ?");


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


    private Message getMessage(ResultSet resultSet) throws SQLException {
        MessageBuilder messageBuilder = new MessageBuilder();
        UserDao userDao = new UserDao(connectionPool);
        User sender = userDao.findUser(resultSet.getString(4));
        User receiver = userDao.findUser(resultSet.getString(5));

        return new Message(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getString(3),
                sender,
                receiver,
                validateStatus(resultSet.getString(6)).name()

        );
    }

    private static Status validateStatus(String status) {
        if (status.compareToIgnoreCase("read") == 0) return Status.READ;
        else return Status.UNREAD;
    }

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

}
