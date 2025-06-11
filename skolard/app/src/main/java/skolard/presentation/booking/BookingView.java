
package skolard.presentation.booking;

import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.booking.BookingHandler;
import skolard.logic.payment.PaymentHandler;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.presentation.payment.PaymentView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingView extends JFrame {
    private final JTextField courseField = new JTextField(15);
    private final JTextField startTimeField = new JTextField(16);
    private final JTextField endTimeField = new JTextField(16);
    private final JComboBox<String> filterDropdown = new JComboBox<>(new String[]{
            "", "Sort by Time", "Sort by Tutor Course Grade", "Sort by Overall Tutor Rating"
    });

    private final JButton searchBtn = new JButton("Find Sessions");
    private final JButton bookButton = new JButton("Book");
    private final JButton infoButton = new JButton("View Info");
    private final JButton backButton = new JButton("Back");

    private final JLabel statusLabel = new JLabel(" ");
    private final JLabel timeRangeLabel = new JLabel("Preferred time range:");
    private final JPanel timePanel = new JPanel();

    private final JTable sessionTable;
    private final DefaultTableModel tableModel;

    private List<Session> currentResults;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final BookingController controller;
    private final PaymentHandler paymentHandler;
    private final SessionHandler sessionHandler;
    private final RatingHandler ratingHandler;
    private final Student student;

    public BookingView(BookingHandler bookingHandler, SessionHandler sessionHandler,
                       RatingHandler ratingHandler, PaymentHandler paymentHandler, Student student) {

        super("SkolarD - Booking View");
        this.paymentHandler = paymentHandler;
        this.sessionHandler = sessionHandler;
        this.ratingHandler = ratingHandler;
        this.student = student;
        this.controller = new BookingController(this, bookingHandler, sessionHandler, ratingHandler, paymentHandler, student);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Tutor", "Start Time", "End Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        sessionTable = new JTable(tableModel);
        sessionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setupInputPanel();
        setupTablePanel();
        setupButtonPanel();
        setupListeners();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(new JLabel("Course:"));
        topRow.add(courseField);
        topRow.add(searchBtn);

        JComboBox<String> filterDropdown = new JComboBox<>(new String[] {
                "", "Sort by Time", "Sort by Tutor Course Grade", "Sort by Overall Tutor Rating"
        });
        topRow.add(filterDropdown);

        inputPanel.add(topRow);

        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        JPanel timeFieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeFieldsRow.add(new JLabel("Start:"));
        timeFieldsRow.add(startTimeField);
        timeFieldsRow.add(new JLabel("End:"));
        timeFieldsRow.add(endTimeField);

        JLabel timeExample = new JLabel("Format Example: 2025-06-01 09:00");

        timePanel.add(timeRangeLabel);
        timePanel.add(timeFieldsRow);
        timePanel.add(timeExample);

        timePanel.setVisible(false);
        timeRangeLabel.setVisible(false);

        inputPanel.add(timePanel);
        add(inputPanel, BorderLayout.NORTH);
    }

    private void setupTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(statusLabel);
        tablePanel.add(new JScrollPane(sessionTable));
        add(tablePanel, BorderLayout.CENTER);
    }

    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bookButton.setEnabled(false);
        infoButton.setEnabled(false);

        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(infoButton);
        add(buttonPanel, BorderLayout.SOUTH);

        bookButton.addActionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < currentResults.size()) {
                Session session = currentResults.get(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Do you want to book this session with " + session.getTutor().getName() + "? " + "Pre-payment is required.",
                        "Confirm Booking",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // Show payment window
                    PaymentView paymentDialog = new PaymentView(SwingUtilities.getWindowAncestor(this), paymentHandler, student);
                    paymentDialog.setVisible(true); // this blocks until dialog is closed

                    if (paymentDialog.wasPaid()) {
                        sessionHandler.bookASession(student, session.getSessionId());
                        ratingHandler.createRatingRequest(session, student); // enqueue for future rating

                        currentResults.remove(selectedRow);
                        tableModel.removeRow(selectedRow);
                        sessionTable.clearSelection();
                        bookButton.setEnabled(false);
                        infoButton.setEnabled(false);

                        JOptionPane.showMessageDialog(this,
                                "Session booked successfully! Payment will be finalized and rating survey will open after session ends at " + session.getEndDateTime(),
                                "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Payment was not completed. Session not booked.",
                                "Payment Cancelled", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        infoButton.addActionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < currentResults.size()) {
                Session session = currentResults.get(selectedRow);
                showSessionDetailsPopup(session);
            }
        });

        backButton.addActionListener(e -> dispose());
    }

    private void setupListeners() {
        filterDropdown.addActionListener(e -> {
            boolean show = "Sort by Time".equals(filterDropdown.getSelectedItem());
            timePanel.setVisible(show);
            timeRangeLabel.setVisible(show);
        });

        searchBtn.addActionListener(e ->
                controller.onSearch(courseField.getText(), (String) filterDropdown.getSelectedItem(),
                        startTimeField.getText(), endTimeField.getText()));


        infoButton.addActionListener(e -> controller.onViewInfo(sessionTable.getSelectedRow()));

        sessionTable.getSelectionModel().addListSelectionListener(e -> {
            boolean valid = !e.getValueIsAdjusting() && sessionTable.getSelectedRow() >= 0;
            bookButton.setEnabled(valid);
            infoButton.setEnabled(valid);
        });
    }

    private void showSessionDetailsPopup(Session session) {
        String message = String.format(
                "<html><b>Tutor:</b> %s<br><b>Email:</b> %s<br><b>Start:</b> %s<br><b>End:</b> %s<br><b>Course:</b> %s<br><b>Bio:</b> %s<br><b>Tutor course grade:</b> %s</html>",
                session.getTutor().getName(),
                session.getTutor().getEmail(),
                session.getStartDateTime().format(formatter),
                session.getEndDateTime().format(formatter),
                session.getCourseName(),
                session.getTutor().getBio(),
                session.getTutor().getGradeForCourse(session.getCourseName()) // Make sure this method exists
        );
        JOptionPane.showMessageDialog(this, message, "Session Details", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateSessionTable(List<Session> sessions) {
        tableModel.setRowCount(0); // Clear existing rows
        currentResults = sessions;

        for (Session session : sessions) {
            Object[] row = {
                    session.getTutor().getName(),
                    session.getStartDateTime().format(formatter),
                    session.getEndDateTime().format(formatter)
            };
            tableModel.addRow(row);
        }

        sessionTable.clearSelection();
        bookButton.setEnabled(false);
        infoButton.setEnabled(false);

        if (sessions.isEmpty()) {
            showStatus("No sessions found for the specified criteria.");
        } else {
            showStatus("Found " + sessions.size() + " available session(s).");
        }
    }

    public void removeSessionFromTable(int rowIndex) {
        if (currentResults != null && rowIndex >= 0 && rowIndex < currentResults.size()) {
            currentResults.remove(rowIndex);
            tableModel.removeRow(rowIndex);
        }
    }

    public Session getSelectedSession(int rowIndex) {
        if (currentResults != null && rowIndex >= 0 && rowIndex < currentResults.size()) {
            return currentResults.get(rowIndex);
        }
        return null;
    }

    public void showStatus(String message) {
        statusLabel.setText(message);
    }

    public void showSessionDetails(Session session) {
        String msg = String.format(
                "<html><b>Tutor:</b> %s<br><b>Email:</b> %s<br><b>Start:</b> %s<br><b>End:</b> %s<br><b>Course:</b> %s<br><b>Bio:</b> %s<br><b>Grade:</b> %s</html>",
                session.getTutor().getName(),
                session.getTutor().getEmail(),
                session.getStartDateTime().format(formatter),
                session.getEndDateTime().format(formatter),
                session.getCourseName(),
                session.getTutor().getBio(),
                session.getTutor().getGradeForCourse(session.getCourseName())
        );
        JOptionPane.showMessageDialog(this, msg, "Session Details", JOptionPane.INFORMATION_MESSAGE);
    }
}