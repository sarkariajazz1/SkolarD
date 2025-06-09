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
import javax.swing.JTextArea;
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

public class SessionView extends JFrame {
    private final SessionHandler sessionHandler;
    private final User currentUser;

    private final JTextField courseNameField = new JTextField(20);
    private final JTextField startTimeField = new JTextField(20);
    private final JTextField endTimeField = new JTextField(20);
    private final JButton createSessionBtn = new JButton("Create Session");

    private DefaultTableModel upcomingModel;
    private DefaultTableModel pastModel;
    private JTable upcomingTable;
    private JTable pastTable;

    private final JButton infoBtn = new JButton("Show Info");
    private final JButton unbookBtn = new JButton("Unbook Session");
    private final JButton backButton = new JButton("Back");

    private final JLabel statusLabel = new JLabel("Session Management");
    private final JTextArea instructionsArea = new JTextArea(4, 40);

    public SessionView(SessionHandler sessionHandler, User currentUser) {
        super("SkolarD - Session Management");
        this.sessionHandler = sessionHandler;
        this.currentUser = currentUser;

        initializeUI();
        setupEventHandlers();
        refreshSessionTables();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setupStatusAndInstructions();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        add(mainPanel, BorderLayout.CENTER);

        if (currentUser instanceof Tutor) {
            JPanel createPanel = createSessionCreationPanel();
            mainPanel.add(createPanel, BorderLayout.NORTH);
        }

        JPanel sessionsPanel = createSessionsPanel();
        mainPanel.add(sessionsPanel, BorderLayout.CENTER);

        // Add back button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupStatusAndInstructions() {
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 16f));
        statusLabel.setForeground(Color.BLACK);
        add(statusLabel, BorderLayout.NORTH);

        instructionsArea.setEditable(false);
        instructionsArea.setBackground(getBackground());
        instructionsArea.setText("Date/Time format: yyyy-MM-dd HH:mm (e.g., 2025-12-25 14:30)\n");
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);

        // Move instructions to a separate panel to make room for back button
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.add(new JScrollPane(instructionsArea), BorderLayout.CENTER);
        add(instructionsPanel, BorderLayout.EAST);
    }

    private JPanel createSessionCreationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Create Session (Tutor Only)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        panel.add(courseNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1;
        panel.add(startTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("End Time:"), gbc);
        gbc.gridx = 1;
        panel.add(endTimeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(createSessionBtn, gbc);

        return panel;
    }

    private JPanel createSessionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Sessions"));

        upcomingModel = new DefaultTableModel(new String[]{"ID", "Course", "Start", "End"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        pastModel = new DefaultTableModel(new String[]{"ID", "Course", "Start", "End"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        upcomingTable = new JTable(upcomingModel);
        pastTable = new JTable(pastModel);

        upcomingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pastTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel upcomingPanel = new JPanel(new BorderLayout());
        upcomingPanel.setBorder(BorderFactory.createTitledBorder("Upcoming Sessions"));
        upcomingPanel.add(new JScrollPane(upcomingTable), BorderLayout.CENTER);
        tablesPanel.add(upcomingPanel);

        JPanel pastPanel = new JPanel(new BorderLayout());
        pastPanel.setBorder(BorderFactory.createTitledBorder("Past Sessions"));
        pastPanel.add(new JScrollPane(pastTable), BorderLayout.CENTER);
        tablesPanel.add(pastPanel);

        panel.add(tablesPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        infoBtn.setEnabled(false);
        buttonsPanel.add(infoBtn);

        if (currentUser instanceof Student) {
            unbookBtn.setEnabled(false);
            buttonsPanel.add(unbookBtn);
        }

        panel.add(buttonsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void setupEventHandlers() {
        createSessionBtn.addActionListener(e -> createSession());

        ListSelectionListener selectionListener = e -> updateButtonsState();
        upcomingTable.getSelectionModel().addListSelectionListener(selectionListener);
        pastTable.getSelectionModel().addListSelectionListener(selectionListener);

        infoBtn.addActionListener(e -> showSelectedSessionInfo());
        unbookBtn.addActionListener(e -> unbookSelectedSession());

        // Back button handler
        backButton.addActionListener(e -> dispose());
    }

    private void updateButtonsState() {
        boolean upcomingSelected = upcomingTable.getSelectedRow() != -1;
        boolean pastSelected = pastTable.getSelectedRow() != -1;
        infoBtn.setEnabled(upcomingSelected || pastSelected);

        if (currentUser instanceof Student) {
            unbookBtn.setEnabled(upcomingSelected);
        }
    }

    private void createSession() {
        try {
            String courseName = CourseUtil.normalizeCourseCode(courseNameField.getText().trim());
            String startStr = startTimeField.getText().trim();
            String endStr = endTimeField.getText().trim();

            if (courseName.isEmpty() || startStr.isEmpty() || endStr.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(startStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endStr, formatter);

            if (!endTime.isAfter(startTime)) {
                showError("End time must be after start time");
                return;
            }

            sessionHandler.createSession((Tutor) currentUser, startTime, endTime, courseName);
            showSuccess("Session created successfully!");
            clearCreateSessionFields();
            refreshSessionTables();
        } catch (DateTimeParseException e) {
            showError("Invalid date/time format. Use: yyyy-MM-dd HH:mm");
        } catch (IllegalArgumentException e) {
            showError("Error creating session: " + e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }

    private void clearCreateSessionFields() {
        courseNameField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");
    }

    private void refreshSessionTables() {
        upcomingModel.setRowCount(0);
        pastModel.setRowCount(0);
        List<Session> pastSessions = new ArrayList<>();
        List<Session> upcomingSessions = new ArrayList<>();

        if (currentUser instanceof Student) {
            sessionHandler.setStudentSessionLists((Student) currentUser);
            pastSessions = ((Student) currentUser).getPastSessions();
            upcomingSessions = ((Student) currentUser).getUpcomingSessions();
        } else if (currentUser instanceof Tutor) {
            sessionHandler.setTutorSessionLists((Tutor) currentUser);
            pastSessions = ((Tutor) currentUser).getPastSessions();
            upcomingSessions = ((Tutor) currentUser).getUpcomingSessions();
        }

        for (Session pastS : pastSessions) {
            Object[] pastRow = {
                    pastS.getSessionId(),
                    pastS.getCourseName(),
                    pastS.getStartDateTime(),
                    pastS.getEndDateTime()
            };
            pastModel.addRow(pastRow);
        }

        for (Session upcomingS : upcomingSessions) {
            // Skip unbooked sessions for Student
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

        upcomingTable.clearSelection();
        pastTable.clearSelection();
        infoBtn.setEnabled(false);
        unbookBtn.setEnabled(false);
    }


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
        info.append("Tutor: ").append(selectedSession.getTutor().getEmail()).append("\n");
        info.append("Student: ").append(
                selectedSession.getStudent() != null ? selectedSession.getStudent().getEmail() : "[unbooked]"
        ).append("\n");

        instructionsArea.setText(info.toString());
    }

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

    private void unbookSelectedSession() {
        int row = upcomingTable.getSelectedRow();
        if (row == -1) {
            showError("Please select an upcoming session to unbook.");
            return;
        }

        int sessionId = (int) upcomingModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to unbook this session?", "Confirm Unbook", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                sessionHandler.unbookASession((Student) currentUser, sessionId);
                showSuccess("Session unbooked successfully!");
                refreshSessionTables();
            } catch (IllegalArgumentException e) {
                showError("Error unbooking session: " + e.getMessage());
            } catch (Exception e) {
                showError("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void showError(String msg) {
        statusLabel.setText("\u274C " + msg);
        statusLabel.setForeground(Color.RED);
    }

    private void showSuccess(String msg) {
        statusLabel.setText("\u2705 " + msg);
        statusLabel.setForeground(new Color(0, 128, 0));
    }
}