package skolard.presentation.matching;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

import skolard.logic.matching.MatchingHandler;
import skolard.logic.matching.MatchingHandler.SessionFilter;
import skolard.objects.Session;

/**
 * A simple GUI window to allow users to find available tutoring sessions for a specific course.
 */
public class MatchingView extends JFrame {
    private MatchingHandler handler; // Logic handler

    private final JTextField courseField = new JTextField(15); // User input for course name
    private final String[] columnNames = { "Tutor", "Start Time", "End Time"};
    private final javax.swing.table.DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(columnNames, 0);
    private final JTable sessionTable = new JTable(tableModel);
    private final JTextField startTimeField = new JTextField(16); // User input for startTime range
    private final JTextField endTimeField = new JTextField(16); // User input for endTime range
    private final JPanel timePanel = new JPanel(new FlowLayout()); //Panel to input time
    private final JLabel statusLabel = new JLabel(" "); // empty space initially
    private List<Session> currentResults;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MatchingView(MatchingHandler matchingHandler) {
        super("SkolarD - Matching View");
        this.handler = matchingHandler;

        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setupInputPanel();
        setupTablePanel();
        setupTableClickListener();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // First row: course + filter
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(new JLabel("Course:"));
        topRow.add(courseField);

        JButton searchBtn = new JButton("Find Tutors");
        topRow.add(searchBtn);

        JComboBox<String> filterDropdown = new JComboBox<>(new String[]{
            "", "Sort by Time", "Sort by Course Rating", "Sort by Overall Tutor Rating"
        });
        topRow.add(filterDropdown);
        inputPanel.add(topRow);

        // Second row: time input
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        JPanel timeFieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeFieldsRow.add(new JLabel("Start:"));
        timeFieldsRow.add(startTimeField);
        timeFieldsRow.add(new JLabel("End:"));
        timeFieldsRow.add(endTimeField);

        JLabel timeExampleLabel = new JLabel("Example: 2025-06-01 09:00");
        timePanel.add(timeFieldsRow);
        timePanel.add(timeExampleLabel);
        timePanel.setVisible(false);

        inputPanel.add(timePanel);
        add(inputPanel, BorderLayout.NORTH);

        // Filter dropdown listener
        filterDropdown.addActionListener(e -> {
            String selected = (String) filterDropdown.getSelectedItem();
            boolean showTime = "Sort by Time".equals(selected);
            timePanel.setVisible(showTime);
            startTimeField.setText("");
            endTimeField.setText("");
        });

        // Search button logic
        searchBtn.addActionListener(e -> {
            String course = courseField.getText().trim();
            String filter = (String) filterDropdown.getSelectedItem();

            if (course.isEmpty()) {
                statusLabel.setText("Please enter a course.");
                return;
            }

            List<Session> results = null;
            LocalDateTime start = null, end = null;

            try {
                if ("Sort by Time".equals(filter)) {
                    start = LocalDateTime.parse(startTimeField.getText().trim(), formatter);
                    end = LocalDateTime.parse(endTimeField.getText().trim(), formatter);
                    results = handler.getAvailableSessions(SessionFilter.TIME, course, start, end);
                } else if ("Sort by Course Rating".equals(filter)) {
                    results = handler.getAvailableSessions(SessionFilter.RATE, course, null, null);
                } else if ("Sort by Overall Tutor Rating".equals(filter)) {
                    results = handler.getAvailableSessions(SessionFilter.TUTOR, course, null, null);
                } else {
                    results = handler.getAvailableSessions(course);
                }
            } catch (DateTimeParseException ex) {
                statusLabel.setText("Invalid date-time format. Use yyyy-MM-dd HH:mm");
                return;
            }

            tableModel.setRowCount(0);
            currentResults = results;

            if (results == null || results.isEmpty()) {
                statusLabel.setText("No sessions found for the given criteria.");
            } else {
                statusLabel.setText("Results:");
                for (Session s : results) {
                    tableModel.addRow(new Object[]{
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

    private void setupTableClickListener() {
        sessionTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            if (!e.getValueIsAdjusting() && selectedRow >= 0 && currentResults != null && selectedRow < currentResults.size()) {
                Session session = currentResults.get(selectedRow);
                showSessionDetailsPopup(session);
            }
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