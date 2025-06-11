package skolard.presentation.rating;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
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
    // Handler for rating-related business logic.
    private final RatingHandler ratingHandler;
    // The Student object representing the currently logged-in student.
    private final Student currentStudent;

    // UI Components
    private final DefaultListModel<String> requestModel = new DefaultListModel<>();
    private final JList<String> requestList = new JList<>(requestModel);
    private final JSlider tutorRatingSlider = new JSlider(1, 5, 3);
    private final JTextArea feedbackArea = new JTextArea(5, 30);
    private final JButton submitRatingBtn = new JButton("Submit Rating");
    private final JButton skipRatingBtn = new JButton("Skip Rating");
    private final JButton refreshBtn = new JButton("Refresh Requests");
    private final JButton backButton = new JButton("Back");

    private final JLabel statusLabel = new JLabel("Select a rating request to proceed");
    // Currently selected rating request.
    private RatingRequest selectedRequest = null;

    /**
     * Constructs a new RatingView window.
     *
     * @param ratingHandler The {@link RatingHandler} instance for managing ratings.
     * @param currentStudent The {@link Student} object for the logged-in student.
     */
    public RatingView(RatingHandler ratingHandler, Student currentStudent) {
        super("SkolarD - Rating Management");
        this.ratingHandler = ratingHandler;
        this.currentStudent = currentStudent;

        // Initialize the graphical user interface components.
        initializeUI();
        // Setup event handlers for user interactions.
        setupEventHandlers();
        // Load pending rating requests for the current student.
        loadRatingRequests();

        // Set default close operation, pack components, and center the window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        // Make the window visible to the user.
        setVisible(true);
    }

    /**
     * Initializes and arranges all UI components within the rating management frame.
     * This includes the list of rating requests, rating form, and action buttons.
     *
     * @return void
     */
    private void initializeUI() {
        // Set the layout manager for the main frame.
        setLayout(new BorderLayout(10, 10));

        // Status label at the top.
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 14f));
        add(statusLabel, BorderLayout.NORTH);

        // Left panel - Rating requests list.
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Pending Rating Requests"));

        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftPanel.add(new JScrollPane(requestList), BorderLayout.CENTER);
        leftPanel.add(refreshBtn, BorderLayout.SOUTH);

        // Right panel - Rating form.
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Submit Rating"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Tutor rating.
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(new JLabel("Tutor Rating (1-5):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        setupRatingSlider(tutorRatingSlider);
        rightPanel.add(tutorRatingSlider, gbc);

        // Buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitRatingBtn);
        buttonPanel.add(skipRatingBtn);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(buttonPanel, gbc);

        // Split pane to divide left and right panels.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        // Back button panel at bottom.
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(backButton);
        add(backPanel, BorderLayout.SOUTH);

        // Initially disable rating form until a request is selected.
        enableRatingForm(false);
    }

    /**
     * Configures the appearance and behavior of the rating slider.
     *
     * @param slider The {@link JSlider} to be configured.
     * @return void
     */
    private void setupRatingSlider(JSlider slider) {
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
    }

    /**
     * Sets up event handlers for user interactions such as list selection,
     * button clicks, and refreshing the list of rating requests.
     *
     * @return void
     */
    private void setupEventHandlers() {
        // List selection handler.
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

        // Submit rating handler.
        submitRatingBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                submitRating();
            }
        });

        // Skip rating handler.
        skipRatingBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                skipRating();
            }
        });

        // Refresh handler.
        refreshBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loadRatingRequests();
            }
        });

        // Back button handler.
        backButton.addActionListener(e -> dispose());
    }

    /**
     * Loads the pending rating requests for the current student and displays them in the list.
     * If no requests are found, it displays a message indicating that.
     *
     * @return void
     */
    private void loadRatingRequests() {
        requestModel.clear();
        List<RatingRequest> requests = ratingHandler.getPendingRequestsForStudent(currentStudent);
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

    /**
     * Submits the rating for the selected rating request.
     * It checks if a request is selected and if the session has ended before submitting.
     *
     * @return void
     */
    private void submitRating() {
        if (selectedRequest == null) {
            showError("Please select a rating request first");
            return;
        }
        /* Add specific time when session ends */
        if (LocalDateTime.now().isBefore(selectedRequest.getSession().getEndDateTime())) {
            showError("You can only rate the session after it has ended.");
            return;
        }

        try {
            int tutorRating = tutorRatingSlider.getValue();

            ratingHandler.processRatingSubmission(selectedRequest, tutorRating);

            showSuccess("Rating submitted successfully!");
            clearForm();
            loadRatingRequests(); // Refresh the list

        } catch (Exception e) {
            showError("Error submitting rating: " + e.getMessage());
        }
    }

    /**
     * Skips the rating for the selected rating request.
     * It checks if a request is selected before skipping.
     *
     * @return void
     */
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

    /**
     * Clears the rating form and resets the UI components to their initial state.
     *
     * @return void
     */
    private void clearForm() {
        tutorRatingSlider.setValue(3);
        feedbackArea.setText("");
        selectedRequest = null;
        enableRatingForm(false);
        statusLabel.setText("Select a rating request to proceed");
    }

    /**
     * Enables or disables the rating form components based on whether a rating request is selected.
     *
     * @param enabled A boolean indicating whether to enable or disable the form.
     * @return void
     */
    private void enableRatingForm(boolean enabled) {
        tutorRatingSlider.setEnabled(enabled);
        feedbackArea.setEnabled(enabled);
        submitRatingBtn.setEnabled(enabled);
        skipRatingBtn.setEnabled(enabled);
    }

    /**
     * Updates the status label to display information about the selected rating request.
     *
     * @return void
     */
    private void updateStatusForSelectedRequest() {
        if (selectedRequest != null) {
            Session session = selectedRequest.getSession();
            statusLabel.setText("Rating session: " + session.getCourseName() +
                    " with " + session.getTutor().getName());
        }
    }

    /**
     * Displays an error message dialog.
     *
     * @param message The error message to be displayed.
     * @return void
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a success message dialog.
     *
     * @param message The success message to be displayed.
     * @return void
     */
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}