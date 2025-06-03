package skolard.presentation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import skolard.logic.matching.MatchingHandler;
import skolard.objects.Session;

/**
 * A simple GUI window to allow users to find available tutoring sessions for a specific course.
 */
public class MatchingView extends JFrame {
    private MatchingHandler handler; // Logic handler

    private final JTextField courseField = new JTextField(15); // User input for course name
    private final DefaultListModel<String> sessionModel = new DefaultListModel<>(); // Backing list model for UI
    private final JList<String> sessionList = new JList<>(sessionModel); // Visual list of available sessions

    public MatchingView(MatchingHandler matchingHandler) {
        super("SkolarD - Matching View");
        this.handler = matchingHandler;

        setLayout(new BorderLayout(10, 10)); // Window layout with spacing

        // Top input panel with label and text field
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Course:"));
        inputPanel.add(courseField);
        JButton searchBtn = new JButton("Find Tutors");
        inputPanel.add(searchBtn);
        add(inputPanel, BorderLayout.NORTH);

        // Scrollable center panel showing sessions
        sessionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(sessionList), BorderLayout.CENTER);

        // Action: when "Find Tutors" is clicked
        searchBtn.addActionListener(e -> {
            String course = courseField.getText().trim(); // get input
            sessionModel.clear(); // clear old results
            if (!course.isEmpty()) {
                List<Session> results = handler.getAvailableSessions(course); // fetch sessions
                for (Session s : results) {
                    sessionModel.addElement(s.toString()); // display them
                }
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        pack(); // Adjust size
        setLocationRelativeTo(null); // Center
        setVisible(true); // Show window
    }
}
