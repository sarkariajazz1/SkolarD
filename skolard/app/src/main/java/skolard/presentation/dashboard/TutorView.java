package skolard.presentation.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.session.SessionHandler;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;

public class TutorView extends JFrame {

    // Handles operations related to user profiles (students and tutors).
    private final ProfileHandler profileHandler;
    // Handles operations related to tutoring sessions.
    private final SessionHandler sessionHandler;
    // Handles operations related to messages between users.
    private final MessageHandler messageHandler;
    // The currently logged-in tutor.
    private final Tutor currentTutor;

    // Text field for entering or displaying a student's email.
    private final JTextField emailField = new JTextField(20);
    // Text area for displaying session details, profile info, or student lists.
    private final JTextArea displayArea = new JTextArea(15, 50);
    // Button to view sessions with a selected student.
    private final JButton viewSessionsBtn = new JButton("View Sessions");
    // Button to allow the tutor to update their biography.
    private final JButton updateBioBtn = new JButton("Update My Bio");
    // Button to view the tutor's own profile.
    private final JButton viewMyProfileBtn = new JButton("View My Profile");
    // Button to refresh and display the list of students the tutor has taught.
    private final JButton viewMyStudentsBtn = new JButton("View My Students");
    // Button to close the current view and return to the previous screen.
    private final JButton backBtn = new JButton("Back");

    // Model for the JList that displays student emails.
    private final DefaultListModel<String> studentListModel = new DefaultListModel<>();
    // JList component to show student emails.
    private final JList<String> studentList = new JList<>(studentListModel);

    // Stores the currently selected student object.
    private Student selectedStudent;

    /**
     * Constructs a new TutorView frame, displaying tutor-specific functionalities.
     *
     * @param profileHandler The handler for managing user profiles.
     * @param sessionHandler The handler for managing tutoring sessions.
     * @param messageHandler The handler for managing messages.
     * @param tutor The {@link Tutor} object representing the currently logged-in tutor.
     * @throws NullPointerException If any of the handler or tutor parameters are null.
     */
    public TutorView(ProfileHandler profileHandler, SessionHandler sessionHandler,
                     MessageHandler messageHandler, Tutor tutor) {
        super("My Students");
        this.profileHandler = profileHandler;
        this.sessionHandler = sessionHandler;
        this.messageHandler = messageHandler;
        this.currentTutor = tutor;

        // Initialize and arrange the UI components.
        initializeUI();
        // Set up event listeners for interactive elements.
        setupEventListeners();
        // Load the list of students associated with the current tutor.
        loadMyStudents();

        // Set the size of the JFrame.
        setSize(800, 600);
        // Center the JFrame on the screen.
        setLocationRelativeTo(null);
        // Set the default close operation for the JFrame.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Make the JFrame visible.
        setVisible(true);
    }

    /**
     * Initializes and arranges the UI components of the TutorView frame.
     * This includes setting up panels for buttons, input fields, student list, and display area.
     *
     * @return void
     */
    private void initializeUI() {
        // Set the layout for the main frame.
        setLayout(new BorderLayout());

        // Create and populate the button panel.
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.add(viewSessionsBtn);
        buttonPanel.add(viewMyProfileBtn);
        buttonPanel.add(updateBioBtn);
        buttonPanel.add(viewMyStudentsBtn);
        buttonPanel.add(backBtn);

        // Create and populate the input panel for student email.
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Student Email:"));
        inputPanel.add(emailField);

        // Configure the student list and wrap it in a scroll pane.
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane studentScrollPane = new JScrollPane(studentList);

        // Configure the display area and wrap it in a scroll pane.
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea);

        // Add panels to the main frame based on BorderLayout.
        add(buttonPanel, BorderLayout.WEST);
        add(inputPanel, BorderLayout.NORTH);
        add(studentScrollPane, BorderLayout.EAST);
        add(displayScrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets up event listeners for all interactive UI components in the TutorView.
     * This includes buttons and the student list selection.
     *
     * @return void
     */
    private void setupEventListeners() {
        // Add action listener for the 'View Sessions' button.
        viewSessionsBtn.addActionListener(e -> viewSessions());
        // Add action listener for the 'Update My Bio' button.
        updateBioBtn.addActionListener(e -> updateBio());
        // Add action listener for the 'View My Profile' button.
        viewMyProfileBtn.addActionListener(e -> viewMyProfile());
        // Add action listener for the 'View My Students' button.
        viewMyStudentsBtn.addActionListener(e -> loadMyStudents());
        // Add action listener for the 'Back' button to close the frame.
        backBtn.addActionListener(e -> dispose());

        // Add a ListSelectionListener to the student list.
        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Get the index of the selected item.
                int selectedIndex = studentList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    // Set the email field text to the selected student's email.
                    String selectedEmail = studentListModel.getElementAt(selectedIndex);
                    emailField.setText(selectedEmail);
                }
            }
        });
    }

    /**
     * Retrieves and displays a list of past and upcoming sessions with a specific student.
     * The student's email is taken from the `emailField`.
     *
     * @return void
     */
    private void viewSessions() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student email or select from the list.");
            return;
        }

        // Retrieve the student object.
        selectedStudent = profileHandler.getStudent(email);
        if (selectedStudent != null) {
            displayArea.setText("Sessions with " + selectedStudent.getName() + " (" + email + "):\n\n");

            // Fetch all sessions for this tutor and filter for the selected student.
            var allSessions = sessionHandler.getSessionsByTutor(currentTutor);
            var pastSessions = allSessions.stream()
                    .filter(s -> s.getStudent() != null && s.getStudent().getEmail().equals(email))
                    .filter(s -> s.getEndDateTime().isBefore(java.time.LocalDateTime.now()))
                    .toList();

            var upcomingSessions = allSessions.stream()
                    .filter(s -> s.getStudent() != null && s.getStudent().getEmail().equals(email))
                    .filter(s -> s.getStartDateTime().isAfter(java.time.LocalDateTime.now()))
                    .toList();

            // Display session summaries.
            displayArea.append("Total sessions: " + (pastSessions.size() + upcomingSessions.size()) + "\n");
            displayArea.append("Upcoming sessions: " + upcomingSessions.size() + "\n");
            displayArea.append("Past sessions: " + pastSessions.size() + "\n\n");

            // Append upcoming sessions if any.
            if (!upcomingSessions.isEmpty()) {
                displayArea.append("=== UPCOMING SESSIONS ===\n");
                for (var session : upcomingSessions) {
                    displayArea.append("• " + formatSession(session) + "\n");
                }
                displayArea.append("\n");
            }

            // Append past sessions if any.
            if (!pastSessions.isEmpty()) {
                displayArea.append("=== PAST SESSIONS ===\n");
                for (var session : pastSessions) {
                    displayArea.append("• " + formatSession(session) + "\n");
                }
            }

            // Handle case where no sessions are found.
            if (pastSessions.isEmpty() && upcomingSessions.isEmpty()) {
                displayArea.append("No sessions found with this student.\n");
            }
        } else {
            displayArea.setText("No student found with email: " + email);
            selectedStudent = null;
        }
    }

    /**
     * Prompts the tutor to update their biography and persists the change.
     *
     * @return void
     */
    private void updateBio() {
        // Get the current bio and prompt for a new one.
        String currentBio = currentTutor.getBio();
        String newBio = JOptionPane.showInputDialog(this, "Update your bio:", currentBio);

        // Update bio if a valid new bio is provided.
        if (newBio != null && !newBio.trim().isEmpty()) {
            currentTutor.setBio(newBio.trim());
            profileHandler.updateTutor(currentTutor);
            JOptionPane.showMessageDialog(this, "Bio updated successfully!");
        }
    }

    /**
     * Displays the full profile information of the current tutor in the display area.
     *
     * @return void
     */
    private void viewMyProfile() {
        displayArea.setText("My Profile:\n\n");
        // Retrieve and display full profile details.
        displayArea.append(profileHandler.viewFullProfile(currentTutor));
    }

    /**
     * Loads and displays a unique list of students the current tutor has had sessions with.
     * The student emails are populated in the {@code studentListModel}.
     *
     * @return void
     */
    private void loadMyStudents() {
        studentListModel.clear();

        // Get all sessions for the tutor and extract unique student emails.
        var sessions = sessionHandler.getSessionsByTutor(currentTutor);
        var studentEmails = sessions.stream()
                .filter(s -> s.getStudent() != null)
                .map(s -> s.getStudent().getEmail())
                .distinct()
                .sorted()
                .toList();

        // Add unique student emails to the list model.
        for (String email : studentEmails) {
            studentListModel.addElement(email);
        }

        displayArea.setText("My Students (" + studentEmails.size() + " total):\n\n");
        if (studentEmails.isEmpty()) {
            displayArea.append("No students found. You haven't had any sessions yet.\n");
        } else {
            // Provide instructions and list students by name and email.
            displayArea.append("Click on a student email from the list on the right to select them,\n");
            displayArea.append("then click 'View Sessions' to see your session history.\n\n");
            displayArea.append("Students you've worked with:\n");
            for (String email : studentEmails) {
                Student student = profileHandler.getStudent(email);
                if (student != null) {
                    displayArea.append("• " + student.getName() + " (" + email + ")\n");
                }
            }
        }
    }

    /**
     * Formats a {@link Session} object into a concise string representation for display.
     *
     * @param session The {@link Session} object to format.
     * @return A formatted {@link String} containing the course name, date, start time, and end time of the session.
     */
    private String formatSession(Session session) {
        return String.format("%s | %s | %s - %s",
                session.getCourseName(),
                session.getStartDateTime().toLocalDate(),
                session.getStartDateTime().toLocalTime(),
                session.getEndDateTime().toLocalTime());
    }
}