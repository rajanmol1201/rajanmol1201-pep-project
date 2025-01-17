package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message createMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Message Creation failed, no such rows affected.");
            }

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    message.setMessage_id(rs.getInt(1));
                } else {
                    throw new SQLException("Message Creation failed !!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return message;
    }

    
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int messageId = rs.getInt("message_id");
                    int postedBy = rs.getInt("posted_by");
                    String messageText = rs.getString("message_text");
                    long timePostedEpoch = rs.getLong("time_posted_epoch");

                    Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    
    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        Message message = null;

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, messageId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int postedBy = rs.getInt("posted_by");
                    String messageText = rs.getString("message_text");
                    long timePostedEpoch = rs.getLong("time_posted_epoch");

                    message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    
    public void deleteMessage(int messageId) {
        String sql = "DELETE FROM message WHERE message_id = ?";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void updateMessage(int messageId, String newMessageText) {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, messageId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";

        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int messageId = resultSet.getInt("message_id");
                    int postedBy = resultSet.getInt("posted_by");
                    String messageText = resultSet.getString("message_text");
                    long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                    Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
