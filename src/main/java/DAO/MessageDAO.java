package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    // Get all messages
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message;";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    // Get all messages from a user given an account id
    public List<Message> getAllMessagesByAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    // Get one message given a message id
    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Create a new message
    public Message addMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {


            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);

            int affectedRow = ps.executeUpdate();

            if (affectedRow > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int generated_message_id = rs.getInt(1);
                    return new Message(
                            generated_message_id,
                            message.getPosted_by(),
                            message.getMessage_text(),
                            message.getTime_posted_epoch());
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Update message given a message id
    public Message updateMessage(int message_id, Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message_id);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // Delete a message given a message id
    public boolean deleteMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement ps2 = connection.prepareStatement(sql);

            ps2.setInt(1, message_id);
            int affectedRow = ps2.executeUpdate();

            return affectedRow > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
