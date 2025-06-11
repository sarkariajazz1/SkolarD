package skolard.objects;

import java.time.LocalDateTime;

/**
 * Represents a support ticket submitted by a student or tutor.
 */
public class SupportTicket {
    private final int ticketId;               // Unique identifier for the ticket
    private final User requester;             // User who submitted the ticket
    private final String title;               // Brief title describing the issue
    private final String description;         // Detailed description of the issue
    private final LocalDateTime createdAt;    // Timestamp when the ticket was created
    private LocalDateTime closedAt;           // Timestamp when the ticket was closed
    private boolean isHandled;                 // Status indicating if the ticket is handled

    // Constructor for new tickets without an assigned ID yet
    public SupportTicket(User requester, String title, String description) {
        this.ticketId = -1;
        this.requester = requester;
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.isHandled = false;
    }

    // Constructor for loading tickets from persistence with all fields
    public SupportTicket(int ticketId, User requester, String title, String description,
                         LocalDateTime createdAt, LocalDateTime closedAt, boolean isHandled) {
        this.ticketId = ticketId;
        this.requester = requester;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.closedAt = closedAt;
        this.isHandled = isHandled;
    }

    /** Returns the unique ticket ID */
    public int getTicketId() {
        return ticketId;
    }

    /** Returns the user who created the ticket */
    public User getRequester() {
        return requester;
    }

    /** Returns the ticket's title */
    public String getTitle() {
        return title;
    }

    /** Returns the detailed description of the issue */
    public String getDescription() {
        return description;
    }

    /** Returns the creation timestamp */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** Returns the closing timestamp, or null if not closed */
    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    /** Returns true if the ticket is handled (closed) */
    public boolean isHandled() {
        return isHandled;
    }

    /** Marks the ticket as handled and sets the closing timestamp */
    public void closeTicket() {
        this.isHandled = true;
        this.closedAt = LocalDateTime.now();
    }

    /** Returns a string representation of the ticket */
    @Override
    public String toString() {
        return "[" + ticketId + "] " + title + " by " + requester.getName()
               + (isHandled ? " (Handled)" : " (Pending)");
    }
}
