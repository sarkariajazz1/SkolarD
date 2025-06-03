package skolard.logic.support;

import java.util.List;

import skolard.objects.SupportTicket;
import skolard.persistence.SupportPersistence;

/**
 * Handles support ticket operations through a persistence interface.
 * Does not store any state internally.
 */
public class SupportHandler {
    private final SupportPersistence supportDB;

    public SupportHandler(SupportPersistence supportDB) {
        if (supportDB == null) {
            throw new IllegalArgumentException("SupportPersistence cannot be null.");
        }
        this.supportDB = supportDB;
    }

    /**
     * Submit a new support ticket to the system.
     */
    public void submitTicket(SupportTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Support ticket cannot be null.");
        }
        supportDB.addTicket(ticket);
    }

    /**
     * Retrieve all active (unresolved) support tickets.
     */
    public List<SupportTicket> getActiveTickets() {
        return supportDB.getActiveTickets();
    }

    /**
     * Retrieve all handled (resolved) support tickets.
     */
    public List<SupportTicket> getHandledTickets() {
        return supportDB.getHandledTickets();
    }

    /**
     * Mark a ticket as handled and persist the change.
     */
    public void closeTicket(SupportTicket ticket) {
        if (ticket == null || ticket.isHandled()) {
            throw new IllegalArgumentException("Ticket is null or already handled.");
        }
        ticket.closeTicket();
        supportDB.updateTicket(ticket);
    }

    /**
     * Look up a specific ticket by ID.
     */
    public SupportTicket getTicketById(String ticketId) {
        return supportDB.getTicketById(ticketId);
    }
}
