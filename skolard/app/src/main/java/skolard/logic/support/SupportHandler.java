package skolard.logic.support;

import java.util.List;

import skolard.objects.SupportTicket;
import skolard.persistence.SupportPersistence;

/**
 * Provides high-level operations for managing support tickets.
 * Relies on a persistence layer for storage and retrieval.
 */
public class SupportHandler {
    // Persistence interface for accessing support ticket data
    private final SupportPersistence supportDB;

    /**
     * Constructs the SupportHandler with a valid persistence implementation.
     *
     * @param supportDB the persistence layer for support tickets
     * @throws IllegalArgumentException if supportDB is null
     */
    public SupportHandler(SupportPersistence supportDB) {
        if (supportDB == null) {
            throw new IllegalArgumentException("SupportPersistence cannot be null.");
        }
        this.supportDB = supportDB;
    }

    /**
     * Adds a new support ticket to the system.
     *
     * @param ticket the support ticket to submit
     * @throws IllegalArgumentException if the ticket is null
     */
    public void submitTicket(SupportTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Support ticket cannot be null.");
        }
        supportDB.addTicket(ticket);
    }

    /**
     * Returns a list of all active (unresolved) support tickets.
     *
     * @return list of active tickets
     */
    public List<SupportTicket> getActiveTickets() {
        return supportDB.getActiveTickets();
    }

    /**
     * Returns a list of all handled (resolved) support tickets.
     *
     * @return list of handled tickets
     */
    public List<SupportTicket> getHandledTickets() {
        return supportDB.getHandledTickets();
    }

    /**
     * Marks the given ticket as handled and updates it in the database.
     *
     * @param ticket the ticket to close
     * @throws IllegalArgumentException if the ticket is null or already handled
     */
    public void closeTicket(SupportTicket ticket) {
        if (ticket == null || ticket.isHandled()) {
            throw new IllegalArgumentException("Ticket is null or already handled.");
        }
        ticket.closeTicket();
        supportDB.updateTicket(ticket);
    }

    /**
     * Retrieves a support ticket by its unique ID.
     *
     * @param ticketId the ID of the ticket
     * @return the matching SupportTicket, or null if not found
     */
    public SupportTicket getTicketById(int ticketId) {
        return supportDB.getTicketById(ticketId);
    }
}
