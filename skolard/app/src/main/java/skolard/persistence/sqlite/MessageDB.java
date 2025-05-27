package skolard.persistence.sqlite;

import skolard.objects.Message;
import skolard.persistence.MessagePersistence;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite-based implementation of MessagePersistence for storing and managing messages
 * between students and tutors in the SkolarD application.
 */
public class MessageDB implements MessagePersistence {

    private final Connection connection;

    /**
     * Constructor that accepts a SQLite database connection.
     * @param connection active database connection
     */
    public MessageDB(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserts a new message into the messages table.
     * Uses auto-increment to generate a new ID and returns the full message object.
     * 
     * @param message the message to insert
     * @return the message with the generated ID included
     */
    @Override
    public Message addMessage(Message message) {
        String sql = "INSERT INTO messages (timeSent, senderEmail, receiverEmail, message) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, message.getTimeSent().toString());
            stmt.setString(2, message.getSenderEmail());
            stmt.setString(3, message.getReceiverEmail());
            stmt.setString(4, message.getMessage());
            stmt.executeUpdate();

            // Retrieve the auto-generated message ID from the database
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new Message(id, message.getTimeSent(), message.getSenderEmail(),
                            message.getReceiverEmail(), message.getMessage());
                } else {
                    throw new RuntimeException("Failed to retrieve generated message ID.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding message", e);
        }
    }

    /**
     * Retrieves the entire message history between a student and tutor.
     * Messages are ordered chronologically by time sent.
     * 
     * @param studentEmail the student's email
     * @param tutorEmail the tutor's email
     * @return list of messages exchanged between the two users
     */
    @Override
    public List<Message> getMessageHistory(String studentEmail, String tutorEmail) {
        String sql = "SELECT * FROM messages WHERE " +
                "(senderEmail = ? AND receiverEmail = ?) OR (senderEmail = ? AND receiverEmail = ?) " +
                "ORDER BY timeSent ASC";

        List<Message> messages = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentEmail);
            stmt.setString(2, tutorEmail);
            stmt.setString(3, tutorEmail);
            stmt.setString(4, studentEmail);

            // Execute query and construct message list from result set
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("id"),
                        LocalDateTime.parse(rs.getString("timeSent")),
                        rs.getString("senderEmail"),
                        rs.getString("receiverEmail"),
                        rs.getString("message")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving message history", e);
        }

        return messages;
    }

    /**
     * Deletes a specific message by its ID.
     * 
     * @param id the unique ID of the message to delete
     */
    @Override
    public void deleteMessageById(int id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting message", e);
        }
    }

    /**
     * Updates the content of a message using its ID.
     * 
     * @param updatedMessage the message object containing the new text and original ID
     */
    @Override
    public void updateMessage(Message updatedMessage) {
        String sql = "UPDATE messages SET message = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, updatedMessage.getMessage());
            stmt.setInt(2, updatedMessage.getMessageId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating message", e);
        }
    }

    /**
     * Deletes all messages exchanged between a student and tutor.
     * This is a bulk operation used for clearing an entire conversation.
     * 
     * @param studentEmail the student's email
     * @param tutorEmail the tutor's email
     */
    @Override
    public void deleteMessageHistory(String studentEmail, String tutorEmail) {
        String sql = "DELETE FROM messages WHERE " +
                "(senderEmail = ? AND receiverEmail = ?) OR (senderEmail = ? AND receiverEmail = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentEmail);
            stmt.setString(2, tutorEmail);
            stmt.setString(3, tutorEmail);
            stmt.setString(4, studentEmail);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting message history", e);
        }
    }
}
