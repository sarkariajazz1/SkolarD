package skolard.presentation.rating;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import skolard.logic.rating.RatingHandler;
import skolard.objects.RatingRequest;
import skolard.objects.Session;
import skolard.objects.Student;

/**
 * GUI window for rating management in SkolarD.
 * Allows students to submit ratings for completed sessions.
 */
public class RatingView extends JFrame {
    private RatingHandler ratingHandler;

    // UI Components
    private final DefaultListModel<String> requestModel = new DefaultListModel<>();
    private final JList<String> requestList = new JList<>(requestModel);
    private final JSlider tutorRatingSlider = new JSlider(1, 5, 3);
    private final JSlider courseRatingSlider = new JSlider(1, 5, 3);
    private final JTextArea feedbackArea = new JTextArea(5, 30);
    private final JButton submitRatingBtn = new JButton("Submit Rating");
    private final JButton skipRatingBtn = new JButton("Skip Rating");
    private final JButton refreshBtn = new JButton("Refresh Requests");
    private final JButton backButton = new JButton("Back");

    private final JLabel statusLabel = new JLabel("Select a rating request to proceed");
    private RatingRequest selectedRequest = null;

    public RatingView(RatingHandler ratingHandler) {
        super("SkolarD - Rating Management");
        this.ratingHandler = ratingHandler;

        initializeUI();
        setupEventHandlers();
        loadRatingRequests();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Status label at top
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 14f));
        add(statusLabel, BorderLayout.NORTH);

        // Left panel - Rating requests list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Pending Rating Requests"));

        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftPanel.add(new JScrollPane(requestList), BorderLayout.CENTER);
        leftPanel.add(refreshBtn, BorderLayout.SOUTH);

        // Right panel - Rating form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Submit Rating"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Tutor rating
        gbc.gridx = 0; gbc.gridy = 0;
        rightPanel.add(new JLabel("Tutor Rating (1-5):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        setupRatingSlider(tutorRatingSlider);
        rightPanel.add(tutorRatingSlider, gbc);

        // Course rating
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        rightPanel.add(new JLabel("Course Rating (1-5):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        setupRatingSlider(courseRatingSlider);
        rightPanel.add(courseRatingSlider, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitRatingBtn);
        buttonPanel.add(skipRatingBtn);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(buttonPanel, gbc);

        // Split pane to divide left and right panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        // Back button panel at bottom
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        // Initially disable rating form until a request is selected
        enableRatingForm(false);
    }

    private void setupRatingSlider(JSlider slider) {
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
    }

    private void setupEventHandlers() {
        // List selection handler
        requestList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = requestList.getSelectedIndex();
                String selectedValue = requestList.getSelectedValue();

                if (selectedIndex >= 0 && !"No pending rating requests".equals(selectedValue)) {
                    List<RatingRequest> requests = ratingHandler.getAllRequests();
                    int actualRequestIndex = 0;
                    for (RatingRequest r : requests) {
                        if (!r.isCompleted()) {
                            if (actualRequestIndex == selectedIndex) {
                                selectedRequest = r;
                                break;
                            }
                            actualRequestIndex++;
                        }
                    }
                    enableRatingForm(true);
                    updateStatusForSelectedRequest();
                } else {
                    selectedRequest = null;
                    enableRatingForm(false);
                    statusLabel.setText("Select a rating request to proceed");
                }
            }
        });

        // Submit rating handler
        submitRatingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRating();
            }
        });

        // Skip rating handler
        skipRatingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skipRating();
            }
        });

        // Refresh handler
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRatingRequests();
            }
        });

        // Back button handler
        backButton.addActionListener(e -> dispose());
    }

    private void loadRatingRequests() {
        requestModel.clear();
        List<RatingRequest> requests = ratingHandler.getAllRequests();
        int added = 0;

        for (RatingRequest request : requests) {
            if (!request.isCompleted()) {
                Session session = request.getSession();
                Student student = request.getStudent();
                String displayText = String.format("Session %d - %s (%s) - Student: %s",
                        session.getSessionId(),
                        session.getCourseName(),
                        session.getTutor().getName(),
                        student.getName());
                requestModel.addElement(displayText);
                added++;
            }
        }

        if (added == 0) {
            requestModel.addElement("No pending rating requests");
            requestList.setEnabled(false); // prevent selection
            statusLabel.setText("No pending requests available");
        } else {
            requestList.setEnabled(true);
            statusLabel.setText("Loaded " + added + " pending request" + (added > 1 ? "s" : ""));
        }

        selectedRequest = null;
        enableRatingForm(false);
    }

    private void submitRating() {
        if (selectedRequest == null) {
            showError("Please select a rating request first");
            return;
        }

        try {
            int tutorRating = tutorRatingSlider.getValue();
            int courseRating = courseRatingSlider.getValue();


            ratingHandler.processRatingSubmission(selectedRequest, tutorRating, courseRating);

            showSuccess("Rating submitted successfully!");
            clearForm();
            loadRatingRequests(); // Refresh the list

        } catch (Exception e) {
            showError("Error submitting rating: " + e.getMessage());
        }
    }

    private void skipRating() {
        if (selectedRequest == null) {
            showError("Please select a rating request first");
            return;
        }

        try {
            ratingHandler.processRatingSkip(selectedRequest);
            showSuccess("Rating request skipped");
            clearForm();
            loadRatingRequests(); // Refresh the list

        } catch (Exception e) {
            showError("Error skipping rating: " + e.getMessage());
        }
    }

    private void clearForm() {
        tutorRatingSlider.setValue(3);
        courseRatingSlider.setValue(3);
        feedbackArea.setText("");
        selectedRequest = null;
        enableRatingForm(false);
        statusLabel.setText("Select a rating request to proceed");
    }

    private void enableRatingForm(boolean enabled) {
        tutorRatingSlider.setEnabled(enabled);
        courseRatingSlider.setEnabled(enabled);
        feedbackArea.setEnabled(enabled);
        submitRatingBtn.setEnabled(enabled);
        skipRatingBtn.setEnabled(enabled);
    }

    private void updateStatusForSelectedRequest() {
        if (selectedRequest != null) {
            Session session = selectedRequest.getSession();
            statusLabel.setText("Rating session: " + session.getCourseName() +
                    " with " + session.getTutor().getName());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}