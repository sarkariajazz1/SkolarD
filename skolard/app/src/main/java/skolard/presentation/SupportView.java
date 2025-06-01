package skolard.presentation;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import skolard.logic.SupportHandler;
import skolard.objects.SupportTicket;
import skolard.objects.User;
import skolard.objects.Student;

/**
 * GUI window for managing support tickets in SkolarD.
 * Allows users to submit tickets and view existing ones.
 */
public class SupportView extends JFrame {
    
    // UI Components
    private final JTextField titleField = new JTextField(30);
    private final JTextArea descriptionArea = new JTextArea(8, 30);
    private final JButton submitTicketBtn = new JButton("Submit Ticket");
    private final JButton viewActiveBtn = new JButton("View Active Tickets");
    private final JButton viewHandledBtn = new JButton("View Handled Tickets");
    private final JButton closeTicketBtn = new JButton("Close Selected Ticket");
    private final DefaultListModel<String> ticketListModel = new DefaultListModel<>();
    private final JList<String> ticketList = new JList<>(ticketListModel);
    
    private SupportHandler handler;
    private List<SupportTicket> currentTickets;
    
    public SupportView(SupportHandler supportHandler) {
        super("SkolarD - Support Center");
        
        this.handler = supportHandler;
        
        setLayout(new BorderLayout(10, 10));
        
        // Top panel for ticket submission
        JPanel submitPanel = new JPanel(new BorderLayout(5, 5));
        submitPanel.setBorder(BorderFactory.createTitledBorder("Submit New Support Ticket"));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(new JLabel("Title:"));
        titlePanel.add(titleField);
        submitPanel.add(titlePanel, BorderLayout.NORTH);
        
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        submitPanel.add(descPanel, BorderLayout.CENTER);
        
        JPanel submitBtnPanel = new JPanel(new FlowLayout());
        submitBtnPanel.add(submitTicketBtn);
        submitPanel.add(submitBtnPanel, BorderLayout.SOUTH);
        
        add(submitPanel, BorderLayout.NORTH);
        
        // Center panel for viewing tickets
        JPanel viewPanel = new JPanel(new BorderLayout(5, 5));
        viewPanel.setBorder(BorderFactory.createTitledBorder("Support Tickets"));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(viewActiveBtn);
        buttonPanel.add(viewHandledBtn);
        buttonPanel.add(closeTicketBtn);
        viewPanel.add(buttonPanel, BorderLayout.NORTH);
        
        ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        viewPanel.add(new JScrollPane(ticketList), BorderLayout.CENTER);
        
        add(viewPanel, BorderLayout.CENTER);
        
        // Submit new ticket
        submitTicketBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            
            if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter both title and description", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // Create a dummy user for demonstration - in real app, this would be the logged-in user
                User dummyUser = new Student("Current User", "user@example.com");
                SupportTicket ticket = new SupportTicket(dummyUser, title, description);
                handler.submitTicket(ticket);
                
                JOptionPane.showMessageDialog(this, 
                    "Support ticket submitted successfully!\nTicket ID: " + ticket.getTicketId(), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                titleField.setText("");
                descriptionArea.setText("");
                
                // Refresh active tickets if currently viewing them
                if (currentTickets != null && !currentTickets.isEmpty() && 
                    !currentTickets.get(0).isHandled()) {
                    loadActiveTickets();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error submitting ticket: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // View active tickets
        viewActiveBtn.addActionListener(e -> loadActiveTickets());
        
        // View handled tickets
        viewHandledBtn.addActionListener(e -> loadHandledTickets());
        
        // Close selected ticket
        closeTicketBtn.addActionListener(e -> {
            int selectedIndex = ticketList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a ticket to close", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (currentTickets == null || selectedIndex >= currentTickets.size()) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid ticket selection", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            SupportTicket selectedTicket = currentTickets.get(selectedIndex);
            if (selectedTicket.isHandled()) {
                JOptionPane.showMessageDialog(this, 
                    "This ticket is already closed", 
                    "Already Closed", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            try {
                handler.closeTicket(selectedTicket);
                JOptionPane.showMessageDialog(this, 
                    "Ticket closed successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh the current view
                if (currentTickets.get(0).isHandled()) {
                    loadHandledTickets();
                } else {
                    loadActiveTickets();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error closing ticket: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void loadActiveTickets() {
        try {
            currentTickets = handler.getActiveTickets();
            ticketListModel.clear();
            
            if (currentTickets.isEmpty()) {
                ticketListModel.addElement("No active tickets found");
            } else {
                for (SupportTicket ticket : currentTickets) {
                    ticketListModel.addElement(ticket.toString());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading active tickets: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadHandledTickets() {
        try {
            currentTickets = handler.getHandledTickets();
            ticketListModel.clear();
            
            if (currentTickets.isEmpty()) {
                ticketListModel.addElement("No handled tickets found");
            } else {
                for (SupportTicket ticket : currentTickets) {
                    ticketListModel.addElement(ticket.toString());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading handled tickets: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}