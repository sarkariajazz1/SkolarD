package skolard.presentation.matching;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.booking.BookingHandler;
import skolard.logic.booking.BookingHandler.SessionFilter;
import skolard.logic.payment.PaymentHandler;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.presentation.payment.PaymentView;
import skolard.utils.CourseUtil;
/**
 * A simple GUI window to allow users to find available tutoring sessions for a specific course and book it.
 */
public class MatchingView extends JFrame {
    private final BookingHandler matchingHandler; // Logic handler
    private final SessionHandler sessionHandler;

    private final JTextField courseField = new JTextField(15);
    private final String[] columnNames = { "Tutor", "Start Time", "End Time" };
    private final javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0);
    private final JTable sessionTable = new JTable(tableModel);
    private final JTextField startTimeField = new JTextField(16);
    private final JTextField endTimeField = new JTextField(16);
    private final JPanel timePanel = new JPanel(new FlowLayout());
    private final JLabel statusLabel = new JLabel(" ");
    private List<Session> currentResults;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final JLabel timeRangeLabel = new JLabel("Preferred time range:");

    private final JButton bookButton = new JButton("Book");
    private final JButton infoButton = new JButton("View Info");
    private final JButton closeButton = new JButton("Close");

private final RatingHandler ratingHandler;

    public MatchingView(BookingHandler matchingHandler, SessionHandler sessionHandler, RatingHandler ratingHandler,PaymentHandler paymentHandler, Student student) {
        super("SkolarD - Matching View");
        this.matchingHandler = matchingHandler;
        this.sessionHandler = sessionHandler;
        this.ratingHandler = ratingHandler;

        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setupInputPanel(student);
        setupTablePanel();
        setupButtonPanel(paymentHandler, student);
        setupTableClickListener();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupInputPanel(Student student) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(new JLabel("Course:"));
        topRow.add(courseField);

        JButton searchBtn = new JButton("Find Tutors");
        topRow.add(searchBtn);

        JComboBox<String> filterDropdown = new JComboBox<>(new String[] {
            "", "Sort by Time", "Sort by Course Rating", "Sort by Overall Tutor Rating"
        });
        topRow.add(filterDropdown);
        inputPanel.add(topRow);

        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timeRangeLabel.setAlignmentX(LEFT_ALIGNMENT);
        timePanel.add(timeRangeLabel);

        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        JPanel timeFieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeFieldsRow.add(new JLabel("Start:"));
        timeFieldsRow.add(startTimeField);
        timeFieldsRow.add(new JLabel("End:"));
        timeFieldsRow.add(endTimeField);

        JLabel timeExampleLabel = new JLabel("Format Example: 2025-06-01 09:00");
        timePanel.add(timeFieldsRow);
        timePanel.add(timeExampleLabel);
        timePanel.setVisible(false);
        timeRangeLabel.setVisible(false);

        inputPanel.add(timePanel);
        add(inputPanel, BorderLayout.NORTH);

        filterDropdown.addActionListener(e -> {
            String selected = (String) filterDropdown.getSelectedItem();
            boolean showTime = "Sort by Time".equals(selected);
            timePanel.setVisible(showTime);
            timeRangeLabel.setVisible(showTime);
            startTimeField.setText("");
            endTimeField.setText("");
        });

        searchBtn.addActionListener(e -> {
            String course = CourseUtil.normalizeCourseCode(courseField.getText().trim());
            String filter = (String) filterDropdown.getSelectedItem();

            if (course.isEmpty()) {
                statusLabel.setText("Please enter a course.");
                tableModel.setRowCount(0);
                return;
            }

            List<Session> results = null;
            LocalDateTime start = null, end = null;

            try {
                if ("Sort by Time".equals(filter)) {
                    start = LocalDateTime.parse(startTimeField.getText().trim(), formatter);
                    end = LocalDateTime.parse(endTimeField.getText().trim(), formatter);
                    results = matchingHandler.getAvailableSessions(SessionFilter.TIME, course, start, end, student.getEmail());
                } else if ("Sort by Course Rating".equals(filter)) {
                    results = matchingHandler.getAvailableSessions(SessionFilter.RATE, course, null, null, student.getEmail());
                } else if ("Sort by Overall Tutor Rating".equals(filter)) {
                    results = matchingHandler.getAvailableSessions(SessionFilter.TUTOR, course, null, null, student.getEmail());
                } else {
                    results = matchingHandler.getAvailableSessions(course, student.getEmail());
                }
            } catch (DateTimeParseException ex) {
                statusLabel.setText("Invalid date-time format. Use yyyy-MM-dd HH:mm");
                return;
            }

            tableModel.setRowCount(0);
            currentResults = results;

            bookButton.setEnabled(false);
            infoButton.setEnabled(false);

            if (results == null || results.isEmpty()) {
                statusLabel.setText("No sessions found for the given criteria.");
            } else {
                statusLabel.setText("Results:");
                for (Session s : results) {
                    tableModel.addRow(new Object[] {
                        s.getTutor().getName(),
                        s.getStartDateTime().format(formatter),
                        s.getEndDateTime().format(formatter)
                    });
                }
            }
        });
    }

    private void setupTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(statusLabel);
        tablePanel.add(new JScrollPane(sessionTable));
        sessionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void setupButtonPanel(PaymentHandler paymentHandler, Student student) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bookButton.setEnabled(false);
        infoButton.setEnabled(false);

        buttonPanel.add(closeButton);
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
                            "Session booked successfully! Payment will be finalized and rating survey will open after session ends.",
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

        closeButton.addActionListener(e -> dispose());
    }

    private void setupTableClickListener() {
        sessionTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            boolean valid = !e.getValueIsAdjusting() && selectedRow >= 0 && currentResults != null && selectedRow < currentResults.size();
            bookButton.setEnabled(valid);
            infoButton.setEnabled(valid);
        });
    }

    private void showSessionDetailsPopup(Session session) {
        String message = String.format(
            "<html><b>Tutor:</b> %s<br><b>Email:</b> %s<br><b>Start:</b> %s<br><b>End:</b> %s<br><b>Course:</b> %s<br><b>Bio:</b> %s</html>",
            session.getTutor().getName(),
            session.getTutor().getEmail(),
            session.getStartDateTime().format(formatter),
            session.getEndDateTime().format(formatter),
            session.getCourseName(),
            session.getTutor().getBio()
        );
        JOptionPane.showMessageDialog(this, message, "Session Details", JOptionPane.INFORMATION_MESSAGE);
    }
}