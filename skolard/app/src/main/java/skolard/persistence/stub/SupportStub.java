package skolard.persistence.stub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skolard.objects.SupportTicket;
import skolard.persistence.SupportPersistence;

public class SupportStub implements SupportPersistence {
    private final Map<Integer, SupportTicket> ticketMap;
    private int uniqueID = 0;

    public SupportStub() {
        ticketMap = new HashMap<>();
    }

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
        SupportTicket newTicket = new SupportTicket(uniqueID++, ticket.getRequester(), ticket.getTitle(),
            ticket.getDescription(), ticket.getCreatedAt(), ticket.getClosedAt(), ticket.isHandled());
        ticketMap.put(newTicket.getTicketId(), newTicket);
        return newTicket;
    }

    @Override
    public void updateTicket(SupportTicket updatedTicket) {
        ticketMap.put(updatedTicket.getTicketId(), updatedTicket);
    }

    @Override
    public void deleteTicketById(int ticketId) {
        ticketMap.remove(ticketId);
    }

    @Override
    public SupportTicket getTicketById(int ticketId) {
        return ticketMap.get(ticketId);
    }
}
