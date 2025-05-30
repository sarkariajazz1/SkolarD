package skolard.objects;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a support ticket submitted by a student or tutor.
 */
public class SupportTicket {
    private final String ticketId;
    private final User requester;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private boolean isHandled;

    public SupportTicket(User requester, String title, String description) {
        this.ticketId = UUID.randomUUID().toString();
        this.requester = requester;
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.isHandled = false;
    }

    public String getTicketId() {
        return ticketId;
    }

    public User getRequester() {
        return requester;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public boolean isHandled() {
        return isHandled;
    }

    public void closeTicket() {
        this.isHandled = true;
        this.closedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + ticketId + "] " + title + " by " + requester.getName()
               + (isHandled ? " (Handled)" : " (Pending)");
    }
}
