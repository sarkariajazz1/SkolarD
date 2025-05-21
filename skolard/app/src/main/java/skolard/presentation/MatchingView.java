package skolard.presentation;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import skolard.logic.MatchingHandler;
import skolard.objects.Session;

public class MatchingView extends JFrame {
    private final MatchingHandler handler = new MatchingHandler();

    private final JTextField courseField = new JTextField(15);
    private final DefaultListModel<String> sessionModel = new DefaultListModel<>();
    private final JList<String> sessionList = new JList<>(sessionModel);

    public MatchingView() {
        super("SkolarD - Matching View");

        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Course:"));
        inputPanel.add(courseField);
        JButton searchBtn = new JButton("Find Tutors");
        inputPanel.add(searchBtn);
        add(inputPanel, BorderLayout.NORTH);

        sessionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(sessionList), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            String course = courseField.getText().trim();
            sessionModel.clear();
            if (!course.isEmpty()) {
                List<Session> results = handler.getAvailableSessions(course);
                for (Session s : results) {
                    sessionModel.addElement(s.toString());
                }
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
