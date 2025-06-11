package skolard.presentation.session;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import skolard.logic.session.SessionHandler;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.utils.CourseUtil;

/**
 * GUI window for session management in SkolarD.
 * Allows tutors to create/delete sessions and students to view/unbook sessions.
 */
public class SessionView extends JFrame {
    // Handler for session-related business logic.
    private final SessionHandler sessionHandler;
    // The current logged-in user (can be a Student or a Tutor).
    private final User currentUser;

    // UI components for session creation (primarily for Tutors).
    private final JTextField courseNameField = new JTextField(20);
    private final JTextField startTimeField = new JTextField(20);
    private final JTextField endTimeField = new JTextField(20);
    private final JButton createSessionBtn = new JButton("Create Session");
    private final JButton deleteBtn = new JButton("Delete Session");

    // Table models and JTables for displaying upcoming and past sessions.
    private DefaultTableModel upcomingModel;
    private DefaultTableModel pastModel;
    private JTable upcomingTable;
    private JTable pastTable;

    // Buttons for session actions.
    private final JButton infoBtn = new JButton("Show Info");
    private final JButton unbookBtn = new JButton("Unbook Session");
    private final JButton backBtn = new JButton("Back");

    // Label for displaying status messages.
    private final JLabel statusLabel = new JLabel("Session Management");

    /**
     * Constructs a new SessionView window.
     *
     * @param sessionHandler The {@link SessionHandler} instance for managing sessions.
     * @param currentUser The {@link User} object (either a Student or Tutor) currently logged in.
     */
    public SessionView(SessionHandler sessionHandler, User currentUser) {
        super("SkolarD - Session Management");
        this.sessionHandler = sessionHandler;
        this.currentUser = currentUser;

        // Initialize the graphical user interface components.
        initializeUI();
        // Setup event handlers for user interactions.
        setupEventHandlers();
        // Refresh session tables to display current data.
        refreshSessionTables();

        // Set default close operation, pack components, and center the window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack(); // Adjusts window size to fit components.
        setLocationRelativeTo(null); // Centers the window on the screen.
        setVisible(true); // Makes the window visible.
    }

    /**
     * Initializes and arranges all UI components within the session management frame.
     * The layout adapts based on whether the current user is a Tutor or a Student.
     *
     * @return void
     */
    private void initializeUI() {
        // Set the main layout manager for the frame.
        setLayout(new BorderLayout(10, 10)); // BorderLayout with gaps.
        setupStatusAndInstructions(); // Setup the status label at the top.

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // If the current user is a Tutor, add the session creation panel.
        if (currentUser instanceof Tutor) {
            JPanel createPanel = createSessionCreationPanel();
            mainPanel.add(createPanel, BorderLayout.NORTH);
        }

        // Create and add the panel for displaying sessions.
        JPanel sessionsPanel = createSessionsPanel();
        mainPanel.add(sessionsPanel, BorderLayout.CENTER);
    }

    /**
     * Sets up the status label at the top of the window, providing visual feedback
     * to the user.
     *
     * @return void
     */
    private void setupStatusAndInstructions() {
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 16f));
        statusLabel.setForeground(Color.BLACK); // Default color.
        add(statusLabel, BorderLayout.NORTH);
    }

    /**
     * Creates and configures the panel for tutors to create new sessions.
     * This panel includes input fields for course name, start time, and end time,
     * along with a button to create the session.
     *
     * @return A {@link JPanel} containing the session creation form.
     */
    private JPanel createSessionCreationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Create Session")); // Titled border.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding.
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left.

        // Row 0: Course Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        panel.add(courseNameField, gbc);

        // Row 1: Start Time with format hint
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1;
        panel.add(startTimeField, gbc);
        gbc.gridx = 2; // For the hint label.
        panel.add(new JLabel("(e.g. 2025-06-10 14:30)"), gbc);

        // Row 2: End Time with format hint
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("End Time:"), gbc);
        gbc.gridx = 1;
        panel.add(endTimeField, gbc);
        gbc.gridx = 2; // For the hint label.
        panel.add(new JLabel("(format: yyyy-MM-dd HH:mm)"), gbc);

        // Row 3: Create Session Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3; // Span across all columns.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally.
        panel.add(createSessionBtn, gbc);

        return panel;
    }

    /**
     * Creates and configures the panel for displaying upcoming and past sessions.
     * This includes two tables (upcoming and past sessions) and action buttons
     * like "Show Info", "Unbook Session", and "Delete Session".
     *
     * @return A {@link JPanel} containing the session display tables and buttons.
     */
    private JPanel createSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Sessions")); // Titled border.

        // Initialize table models with column names and make cells non-editable.
        upcomingModel = new DefaultTableModel(new String[]{"ID", "Course", "Start", "End"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pastModel = new DefaultTableModel(new String[]{"ID", "Course", "Start", "End"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create custom DeselectableTable instances.
        upcomingTable = new DeselectableTable(upcomingModel);
        pastTable = new DeselectableTable(pastModel);

        // Allow only single row selection.
        upcomingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pastTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 10)); // Two tables side-by-side.

        // Panel for upcoming sessions table.
        JPanel upcomingPanel = new JPanel(new BorderLayout());
        upcomingPanel.setBorder(BorderFactory.createTitledBorder("Upcoming Sessions"));
        upcomingPanel.add(new JScrollPane(upcomingTable), BorderLayout.CENTER);
        tablesPanel.add(upcomingPanel);

        // Panel for past sessions table.
        JPanel pastPanel = new JPanel(new BorderLayout());
        pastPanel.setBorder(BorderFactory.createTitledBorder("Past Sessions"));
        pastPanel.add(new JScrollPane(pastTable), BorderLayout.CENTER);
        tablesPanel.add(pastPanel);

        panel.add(tablesPanel, BorderLayout.CENTER);

        // Panel for action buttons.
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        infoBtn.setEnabled(false); // Initially disabled.
        buttonsPanel.add(infoBtn);

        // Add "Unbook Session" button only for Students.
        if (currentUser instanceof Student) {
            unbookBtn.setEnabled(false); // Initially disabled.
            buttonsPanel.add(unbookBtn);
        }

        // Add "Delete Session" button only for Tutors.
        if (currentUser instanceof Tutor) {
            deleteBtn.setEnabled(false); // Initially disabled.
            buttonsPanel.add(deleteBtn);
        }
        buttonsPanel.add(backBtn); // Back button always present.

        panel.add(buttonsPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Sets up event handlers for all interactive UI components.
     * This includes listeners for buttons and table row selections.
     *
     * @return void
     */
    private void setupEventHandlers() {
        // ActionListener for the "Create Session" button.
        createSessionBtn.addActionListener(e -> createSession());

        // ListSelectionListener for both upcoming and past session tables.
        // This listener will update the enabled/disabled state of action buttons.
        ListSelectionListener selectionListener = e -> updateButtonsState();
        upcomingTable.getSelectionModel().addListSelectionListener(selectionListener);
        pastTable.getSelectionModel().addListSelectionListener(selectionListener);

        // ActionListeners for other functional buttons.
        infoBtn.addActionListener(e -> showSelectedSessionInfo());
        unbookBtn.addActionListener(e -> unbookSelectedSession());
        deleteBtn.addActionListener(e -> deleteSelectedSession());
        backBtn.addActionListener(e -> dispose()); // Closes the window.
    }

    /**
     * Updates the enabled state of the action buttons ("Show Info", "Unbook Session",
     * "Delete Session") based on whether a session is selected in either table.
     *
     * @return void
     */
    private void updateButtonsState() {
        boolean upcomingSelected = upcomingTable.getSelectedRow() != -1;
        boolean pastSelected = pastTable.getSelectedRow() != -1;
        infoBtn.setEnabled(upcomingSelected || pastSelected); // Info button is enabled if any session is selected.

        if (currentUser instanceof Student) {
            unbookBtn.setEnabled(upcomingSelected); // Students can only unbook upcoming sessions.
        }

        if (currentUser instanceof Tutor) {
            deleteBtn.setEnabled(upcomingSelected); // Tutors can only delete upcoming sessions.
        }
    }

    /**
     * Handles the creation of a new session when the "Create Session" button is clicked.
     * It validates input, parses date/time, and calls the session handler to create the session.
     *
     * @return void
     */
    private void createSession() {
        try {
            String courseName = CourseUtil.normalizeCourseCode(courseNameField.getText().trim());
            String startStr = startTimeField.getText().trim();
            String endStr = endTimeField.getText().trim();

            // Input validation.
            if (courseName.isEmpty() || startStr.isEmpty() || endStr.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }

            // Parse date/time strings.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(startStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endStr, formatter);

            // Validate time logic.
            if (!endTime.isAfter(startTime)) {
                showError("End time must be after start time");
                return;
            }

            // Call session handler to create the session (only for Tutors).
            sessionHandler.createSession((Tutor) currentUser, startTime, endTime, courseName);
            showSuccess("Session created successfully!");
            clearCreateSessionFields(); // Clear input fields.
            refreshSessionTables(); // Refresh session lists.
        } catch (DateTimeParseException e) {
            showError("Invalid date/time format. Use: yyyy-MM-dd HH:mm");
        } catch (IllegalArgumentException e) {
            showError("Error creating session: " + e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Clears the text fields used for creating a new session.
     *
     * @return void
     */
    private void clearCreateSessionFields() {
        courseNameField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");
    }

    /**
     * Refreshes the upcoming and past session tables by clearing existing data
     * and repopulating them with the latest session information from the session handler.
     * The sessions displayed depend on the current user (Student or Tutor).
     *
     * @return void
     */
    private void refreshSessionTables() {
        upcomingModel.setRowCount(0); // Clear upcoming table.
        pastModel.setRowCount(0);     // Clear past table.
        List<Session> pastSessions = new ArrayList<>();
        List<Session> upcomingSessions = new ArrayList<>();

        // Populate session lists based on user type.
        if (currentUser instanceof Student) {
            sessionHandler.setStudentSessionLists((Student) currentUser);
            pastSessions = ((Student) currentUser).getPastSessions();
            upcomingSessions = ((Student) currentUser).getUpcomingSessions();
        } else if (currentUser instanceof Tutor) {
            sessionHandler.setTutorSessionLists((Tutor) currentUser);
            pastSessions = ((Tutor) currentUser).getPastSessions();
            upcomingSessions = ((Tutor) currentUser).getUpcomingSessions();
        }

        // Add past sessions to the past table.
        for (Session pastS : pastSessions) {
            Object[] pastRow = {
                    pastS.getSessionId(),
                    pastS.getCourseName(),
                    pastS.getStartDateTime(),
                    pastS.getEndDateTime()
            };
            pastModel.addRow(pastRow);
        }

        // Add upcoming sessions to the upcoming table.
        for (Session upcomingS : upcomingSessions) {
            // Students only see sessions they have booked.
            if (currentUser instanceof Student && upcomingS.getStudent() == null) {
                continue;
            }

            Object[] upcomingRow = {
                    upcomingS.getSessionId(),
                    upcomingS.getCourseName(),
                    upcomingS.getStartDateTime(),
                    upcomingS.getEndDateTime()
            };
            upcomingModel.addRow(upcomingRow);
        }

        // Clear table selections and disable action buttons after refresh.
        upcomingTable.clearSelection();
        pastTable.clearSelection();
        infoBtn.setEnabled(false);
        unbookBtn.setEnabled(false);
        deleteBtn.setEnabled(false); // Ensure delete button is also reset.
    }

    /**
     * Displays detailed information about the currently selected session in a pop-up dialog.
     * The information includes session ID, course, times, and details about the
     * tutor (for students) or student (for tutors).
     *
     * @return void
     */
    private void showSelectedSessionInfo() {
        Session selectedSession = getSelectedSession();
        if (selectedSession == null) {
            showError("No session selected");
            return;
        }

        StringBuilder info = new StringBuilder();
        info.append("Session ID: ").append(selectedSession.getSessionId()).append("\n");
        info.append("Course: ").append(selectedSession.getCourseName()).append("\n");
        info.append("Start Time: ").append(selectedSession.getStartDateTime()).append("\n");
        info.append("End Time: ").append(selectedSession.getEndDateTime()).append("\n");

        // Display tutor or student specific details based on currentUser type.
        if (currentUser instanceof Student) {
            info.append("Tutor: ").append(selectedSession.getTutor().getName()).append("\n");
            info.append("Email: ").append(selectedSession.getTutor().getEmail()).append("\n");
            info.append("Bio: ").append(selectedSession.getTutor().getBio());
        } else if (currentUser instanceof Tutor && selectedSession.getStudent() != null) {
            info.append("Student: ").append(selectedSession.getStudent().getName()).append("\n");
            info.append("Email: ").append(selectedSession.getStudent().getEmail());
        } else {
            info.append("Student: ").append("none").append("\n");
            info.append("Email: ").append("none");
        }

        // Show the information in a message dialog.
        JOptionPane.showMessageDialog(this, info.toString(), "Session Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Retrieves the currently selected {@link Session} object from either the
     * upcoming or past sessions table.
     *
     * @return The selected {@link Session} object, or {@code null} if no session is selected.
     */
    private Session getSelectedSession() {
        int row = upcomingTable.getSelectedRow();
        if (row != -1) {
            int sessionId = (int) upcomingModel.getValueAt(row, 0);
            return sessionHandler.getSessionByID(sessionId);
        }
        row = pastTable.getSelectedRow();
        if (row != -1) {
            int sessionId = (int) pastModel.getValueAt(row, 0);
            return sessionHandler.getSessionByID(sessionId);
        }
        return null;
    }

    /**
     * Handles the unbooking of a selected session by a student.
     * It prompts for confirmation, performs the unbooking logic, and refreshes the tables.
     * A refund message is displayed upon successful unbooking.
     *
     * @return void
     */
    private void unbookSelectedSession() {
        int row = upcomingTable.getSelectedRow();
        if (row == -1) {
            showError("Please select an upcoming session to unbook.");
            return;
        }

        int sessionId = (int) upcomingModel.getValueAt(row, 0);
        // Confirmation dialog for unbooking.
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to unbook this session?", "Confirm Unbook", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Call session handler to unbook the session (only for Students).
                sessionHandler.unbookASession((Student) currentUser, sessionId);
                showSuccess("Session unbooked and refunded successfully! \n Refund will take a few days to process.");
                refreshSessionTables(); // Refresh tables after unbooking.
            } catch (IllegalArgumentException e) {
                showError("Error unbooking session: " + e.getMessage());
            } catch (Exception e) {
                showError("Unexpected error: " + e.getMessage());
            }
        }
    }

    /**
     * Displays an error message in the status label at the top of the window,
     * coloring the text red.
     *
     * @param msg The error message to display.
     * @return void
     */
    private void showError(String msg) {
        statusLabel.setText("\u274C " + msg); // Unicode 'X' mark.
        statusLabel.setForeground(Color.RED);
    }

    /**
     * Displays a success message in the status label at the top of the window,
     * coloring the text green.
     *
     * @param msg The success message to display.
     * @return void
     */
    private void showSuccess(String msg) {
        statusLabel.setText("\u2705 " + msg); // Unicode check mark.
        statusLabel.setForeground(new Color(0, 128, 0)); // Dark green.
    }

    /**
     * Custom {@link JTable} subclass that allows deselecting a row by clicking
     * on it again if it's already selected.
     */
    private static class DeselectableTable extends JTable {
        public DeselectableTable(DefaultTableModel model) {
            super(model);
        }

        @Override
        public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
            // If the clicked row is already selected, clear the selection.
            if (rowIndex == getSelectedRow()) {
                clearSelection();
            } else {
                // Otherwise, perform the default selection change.
                super.changeSelection(rowIndex, columnIndex, toggle, extend);
            }
        }
    }

    /**
     * Handles the deletion of a selected session by a tutor.
     * It prompts for confirmation, especially if the session is booked,
     * then performs the deletion and refreshes the tables.
     * A refund message is shown if a student was booked.
     *
     * @return void
     */
    private void deleteSelectedSession() {
        int row = upcomingTable.getSelectedRow();
        if (row == -1) {
            showError("Please select an upcoming session to delete.");
            return;
        }

        int sessionId = (int) upcomingModel.getValueAt(row, 0);
        Session session = sessionHandler.getSessionByID(sessionId);

        // Determine the warning message based on whether the session has a student.
        String warningMsg = session.getStudent() != null
                ? "This session has a student. Refund process will begin once deleted.\nContinue?"
                : "Are you sure you want to delete this session?";

        // Confirmation dialog for deletion.
        int confirm = JOptionPane.showConfirmDialog(this, warningMsg, "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Call session handler to delete the session (only for Tutors).
                sessionHandler.deleteSession((Tutor) currentUser, session);
                if (session.getStudent() != null) {
                    showSuccess("Session deleted. A refund will be processed for the student.");
                } else {
                    showSuccess("Session deleted successfully.");
                }
                refreshSessionTables(); // Refresh tables after deletion.
            } catch (Exception e) {
                showError("Failed to delete session: " + e.getMessage());
            }
        }
    }
}
