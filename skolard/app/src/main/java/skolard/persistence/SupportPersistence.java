package skolard.persistence;

import java.util.List;

import skolard.objects.SupportTicket;

/**
 * Interface for accessing and managing support tickets.
 * Supports CRUD operations and filtering by ticket status.
 */
public interface SupportPersistence {

    /**
     * Get all support tickets in the system (both handled and active).
     * 
     * @return list of all tickets
     */
    List<SupportTicket> getAllTickets();

    /**
     * Get all active (unresolved) support tickets.
     *
     * @return list of active tickets
     */
    List<SupportTicket> getActiveTickets();

    /**
     * Get all handled (resolved/closed) support tickets.
     *
     * @return list of handled tickets
     */
    List<SupportTicket> getHandledTickets();

    /**
     * Add a new support ticket to the system.
     *
     * @param ticket the ticket to add
     * @return the added ticket
     */
    SupportTicket addTicket(SupportTicket ticket);

    /**
     * Update a ticket's status or details (e.g. to mark it as handled).
     *
     * @param updatedTicket the modified ticket
     */
    void updateTicket(SupportTicket updatedTicket);

    /**
     * Delete a ticket from the system by its ID.
     *
     * @param ticketId the ID of the ticket to delete
     */
    void deleteTicketById(String ticketId);

    /**
     * Retrieve a ticket by its unique ID.
     *
     * @param ticketId the ID of the ticket
     * @return the ticket object, or null if not found
     */
    SupportTicket getTicketById(String ticketId);
}

