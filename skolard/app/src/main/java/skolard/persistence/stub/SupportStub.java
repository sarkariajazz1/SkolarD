package skolard.persistence.stub;

import skolard.objects.SupportTicket;
import skolard.persistence.SupportPersistence;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class SupportStub implements SupportPersistence {
    private Map<Integer, SupportTicket> ticketMap;
    private int uniqueID = 0;

    public SupportStub() {
        confirmCreation();
    }

    private void confirmCreation() {
        if(ticketMap == null) {
            ticketMap = new HashMap<>();
        }
    }

    @Override
    public List<SupportTicket> getAllTickets() {
        confirmCreation();
        return new ArrayList<>(ticketMap.values());
    }

    @Override
    public List<SupportTicket> getActiveTickets() {
        confirmCreation();
        return ticketMap.values().stream()
            .filter(t -> !t.isHandled())
            .toList();
    }

    @Override
    public List<SupportTicket> getHandledTickets() {
        confirmCreation();
        return ticketMap.values().stream()
            .filter(SupportTicket::isHandled)
            .toList();
    }

    @Override
    public SupportTicket addTicket(SupportTicket ticket) {
        confirmCreation();
        SupportTicket newTicket = new SupportTicket(uniqueID++, ticket.getRequester(), ticket.getTitle(),
            ticket.getDescription(), ticket.getCreatedAt(), ticket.getClosedAt(), ticket.isHandled());
        ticketMap.put(newTicket.getTicketId(), newTicket);
        return newTicket;
    }

    @Override
    public void updateTicket(SupportTicket updatedTicket) {
        confirmCreation();
        ticketMap.put(updatedTicket.getTicketId(), updatedTicket);
    }

    @Override
    public void deleteTicketById(int ticketId) {
        confirmCreation();
        ticketMap.remove(ticketId);
    }

    @Override
    public SupportTicket getTicketById(int ticketId) {
        confirmCreation();
        return ticketMap.get(ticketId);
    }
}
