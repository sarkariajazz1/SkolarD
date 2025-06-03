package skolard.persistence.sqlite;

import skolard.objects.SupportTicket;
import skolard.objects.User;
import skolard.persistence.StudentPersistence;
import skolard.persistence.SupportPersistence;
import skolard.persistence.TutorPersistence;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupportDB implements SupportPersistence {
    private final Connection conn;
    private final StudentPersistence studentDB;
    private final TutorPersistence tutorDB;

    public SupportDB(Connection conn, StudentPersistence studentDB, TutorPersistence tutorDB) {
        this.conn = conn;
        this.studentDB = studentDB;
        this.tutorDB = tutorDB;
    }

    @Override
    public List<SupportTicket> getAllTickets() {
        return getTicketsByQuery("SELECT * FROM support_ticket");
    }

    @Override
    public List<SupportTicket> getActiveTickets() {
        return getTicketsByQuery("SELECT * FROM support_ticket WHERE is_handled = 0");
    }

    @Override
    public List<SupportTicket> getHandledTickets() {
        return getTicketsByQuery("SELECT * FROM support_ticket WHERE is_handled = 1");
    }

    @Override
    public SupportTicket addTicket(SupportTicket ticket) {
        try (PreparedStatement stmt = conn.prepareStatement("""
            INSERT INTO support_ticket (ticket_id, requester_email, title, description, created_at, closed_at, is_handled)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """)) {
            stmt.setString(1, ticket.getTicketId());
            stmt.setString(2, ticket.getRequester().getEmail());
            stmt.setString(3, ticket.getTitle());
            stmt.setString(4, ticket.getDescription());
            stmt.setString(5, ticket.getCreatedAt().toString());
            stmt.setString(6, ticket.getClosedAt() == null ? null : ticket.getClosedAt().toString());
            stmt.setInt(7, ticket.isHandled() ? 1 : 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    @Override
    public void updateTicket(SupportTicket ticket) {
        try (PreparedStatement stmt = conn.prepareStatement("""
            UPDATE support_ticket
            SET closed_at = ?, is_handled = ?
            WHERE ticket_id = ?
        """)) {
            stmt.setString(1, ticket.getClosedAt() == null ? null : ticket.getClosedAt().toString());
            stmt.setInt(2, ticket.isHandled() ? 1 : 0);
            stmt.setString(3, ticket.getTicketId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTicketById(String ticketId) {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM support_ticket WHERE ticket_id = ?")) {
            stmt.setString(1, ticketId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SupportTicket getTicketById(String ticketId) {
        List<SupportTicket> result = getTicketsByQuery("SELECT * FROM support_ticket WHERE ticket_id = '" + ticketId + "'");
        return result.isEmpty() ? null : result.get(0);
    }

    private List<SupportTicket> getTicketsByQuery(String sql) {
        List<SupportTicket> tickets = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String email = rs.getString("requester_email");
                String role = rs.getString("requester_role");
                String title = rs.getString("title");
                String desc = rs.getString("description");
                LocalDateTime createdAt = LocalDateTime.parse(rs.getString("created_at"));
                LocalDateTime closedAt = rs.getString("closed_at") != null ? LocalDateTime.parse(rs.getString("closed_at")) : null;
                boolean isHandled = rs.getInt("is_handled") == 1;

                User requester = switch (role) {
                    case "student" -> studentDB.getStudentByEmail(email);
                    case "tutor" -> tutorDB.getTutorByEmail(email);
                    default -> null;
                };

                if (requester != null) {
                    SupportTicket ticket = new SupportTicket(requester, title, desc);
                    if (isHandled) ticket.closeTicket();
                    tickets.add(ticket);
                } else {
                    System.err.println("Warning: requester not found for ticket email: " + email);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }
}
