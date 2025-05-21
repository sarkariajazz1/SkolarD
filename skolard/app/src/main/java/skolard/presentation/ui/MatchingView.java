// src/main/java/skolard/presentation/ui/MatchingView.java
package skolard.presentation.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import skolard.presentation.MatchingController;
import skolard.objects.Session;

public class MatchingView extends JFrame {
    private final MatchingController ctrl = new MatchingController();
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public MatchingView() {
        super("Session Matching");
        setContentPane(createMainPane());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createMainPane() {
        JPanel panel = new JPanel(new BorderLayout(8,8));

        // Top: course input + button
        JTextField courseField = new JTextField();
        JButton findBtn = new JButton("Find Matches");
        JPanel top = new JPanel(new BorderLayout(4,4));
        top.add(new JLabel("Course Code:"), BorderLayout.WEST);
        top.add(courseField, BorderLayout.CENTER);
        top.add(findBtn, BorderLayout.EAST);

        // Center: results list
        JList<String> list = new JList<>(model);
        JScrollPane scroll = new JScrollPane(list);

        findBtn.addActionListener(e -> {
            model.clear();
            String course = courseField.getText().trim();
            if (!course.isEmpty()) {
                List<Session> matches = ctrl.getMatches(course);
                for (Session s : matches) {
                    model.addElement(
                            String.format("[%s] %s → %s @ %s (%d min)",
                                    s.getSessionId(),
                                    s.getTutor().getName(),
                                    s.getStudent().getName(),
                                    s.getStartDateTime(),
                                    s.getEndDateTime().getMinute() - s.getStartDateTime().getMinute())
                    );
                }
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
}
