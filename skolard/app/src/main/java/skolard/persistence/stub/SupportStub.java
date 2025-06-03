package skolard.persistence.stub;

import skolard.objects.SupportTicket;
import skolard.persistence.SupportPersistence;

import java.util.*;

public class SupportStub implements SupportPersistence {
    private final Map<String, SupportTicket> ticketMap = new HashMap<>();

    @Override
    public List<SupportTicket> getAllTickets() {
        return new ArrayList<>(ticketMap.values());
    }

    @Override
    public List<SupportTicket> getActiveTickets() {
        return ticketMap.values().stream()
            .filter(t -> !t.isHandled())
            .toList();
    }

    @Override
    public List<SupportTicket> getHandledTickets() {
        return ticketMap.values().stream()
            .filter(SupportTicket::isHandled)
            .toList();
    }

    @Override
    public SupportTicket addTicket(SupportTicket ticket) {
        ticketMap.put(ticket.getTicketId(), ticket);
        return ticket;
    }

    @Override
    public void updateTicket(SupportTicket updatedTicket) {
        ticketMap.put(updatedTicket.getTicketId(), updatedTicket);
    }

    @Override
    public void deleteTicketById(String ticketId) {
        ticketMap.remove(ticketId);
    }

    @Override
    public SupportTicket getTicketById(String ticketId) {
        return ticketMap.get(ticketId);
    }
}
