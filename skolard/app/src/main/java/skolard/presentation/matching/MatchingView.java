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

    public MatchingView(MatchingHandler matchingHandler) {
        super("SkolarD - Matching View");
        this.handler = matchingHandler;

        setLayout(new BorderLayout(10, 10)); // Window layout with spacing

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Top input panel with label and text field
        // Top-level vertical layout panel
        // Row 1: course input and filter options
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(new JLabel("Course:"));
        topRow.add(courseField);
        JButton searchBtn = new JButton("Find Tutors");
        topRow.add(searchBtn);
        JComboBox<String> filterDropdown = new JComboBox<>(new String[]{
            "",
            "Sort by Time",
            "Sort by Course Rating",
            "Sort by Overall Tutor Rating"
        });
        topRow.add(filterDropdown);
        add(inputPanel, BorderLayout.NORTH);

        // Row 2: Time input fields and example label
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



        // Add both rows to the input panel
        inputPanel.add(topRow);
        inputPanel.add(timePanel);

        //Add status label above table panel to indicate input status
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(statusLabel);
        tablePanel.add(new JScrollPane(sessionTable));
        add(tablePanel, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);

        // Center panel table showing sessions
        sessionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Show/hide timePanel based on dropdown
        filterDropdown.addActionListener(e -> {
            String selected = (String) filterDropdown.getSelectedItem();
            timePanel.setVisible("Sort by Time".equals(selected));

            // Clear time input fields every time the filter changes
            startTimeField.setText("");
            endTimeField.setText("");
        });

        // Action: when "Find Tutors" is clicked
        searchBtn.addActionListener(e -> {
            String course = courseField.getText().trim(); // get input
            String filter = (String) filterDropdown.getSelectedItem();

            if (!course.isEmpty()) {
                List<Session> results = null; 
                LocalDateTime start = null;
                LocalDateTime end = null;
                // fetch sessions
                if(filter.equals("Sort by Time")){
                    try {
                        start = LocalDateTime.parse(startTimeField.getText().trim(), formatter);
                        end = LocalDateTime.parse(endTimeField.getText().trim(), formatter);
                    } catch (DateTimeParseException ex) {
                        statusLabel.setText("Invalid date-time format or empty input. Use yyyy-MM-dd HH:mm");
                        return;
                    }
                    results = handler.getAvailableSessions(SessionFilter.TIME, course, start, end);
                } else if(filter.equals("Sort by Course Rating")){
                    results = handler.getAvailableSessions(SessionFilter.RATE, course, start, end);
                } else if(filter.equals("Sort by Overall Tutor Rating")){
                    results = handler.getAvailableSessions(SessionFilter.TUTOR, course, start, end);
                } else{
                    results = handler.getAvailableSessions(course);
                }

                //Clears old data displayed
                tableModel.setRowCount(0);
                if(!results.isEmpty()){
                    for (Session s : results) {
                        Object[] row = {
                            s.getTutor().getName(),
                            s.getStartDateTime().format(formatter),
                            s.getEndDateTime().format(formatter)
                        };
                        tableModel.addRow(row);
                    }
                } else{
                    statusLabel.setText("No sessions found for the given criteria.");
                }
            } else{
                statusLabel.setText("Please enter a course.");
                return;
            }

        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        pack(); // Adjust size
        setLocationRelativeTo(null); // Center
        setVisible(true); // Show window
    }
}