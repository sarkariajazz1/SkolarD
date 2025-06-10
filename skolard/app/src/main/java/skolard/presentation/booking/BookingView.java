package skolard.presentation.booking;

import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.booking.BookingHandler;
import skolard.logic.payment.PaymentHandler;
import skolard.objects.Session;
import skolard.objects.Student;

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
    private final JButton closeButton = new JButton("Close");

    private final JLabel statusLabel = new JLabel(" ");
    private final JLabel timeRangeLabel = new JLabel("Preferred time range:");
    private final JPanel timePanel = new JPanel();

    private final JTable sessionTable;
    private final DefaultTableModel tableModel;

    private List<Session> currentResults;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final BookingController controller;

    public BookingView(BookingHandler bookingHandler, SessionHandler sessionHandler,
                       RatingHandler ratingHandler, PaymentHandler paymentHandler, Student student) {

        super("SkolarD - Booking View");
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
        buttonPanel.add(closeButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(infoButton);
        add(buttonPanel, BorderLayout.SOUTH);
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

        bookButton.addActionListener(e -> controller.onBook(sessionTable.getSelectedRow()));

        infoButton.addActionListener(e -> controller.onViewInfo(sessionTable.getSelectedRow()));

        closeButton.addActionListener(e -> dispose());

        sessionTable.getSelectionModel().addListSelectionListener(e -> {
            boolean valid = !e.getValueIsAdjusting() && sessionTable.getSelectedRow() >= 0;
            bookButton.setEnabled(valid);
            infoButton.setEnabled(valid);
        });
    }

    public void updateSessionTable(List<Session> sessions) {
        tableModel.setRowCount(0);
        this.currentResults = sessions;

        if (sessions == null || sessions.isEmpty()) {
            showStatus("No sessions found.");
            bookButton.setEnabled(false);
            infoButton.setEnabled(false);
            return;
        }

        for (Session s : sessions) {
            tableModel.addRow(new Object[]{
                    s.getTutor().getName(),
                    s.getStartDateTime().format(formatter),
                    s.getEndDateTime().format(formatter)
            });
        }

        showStatus("Results:");
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
