
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

    private final ProfileHandler profileHandler;
    private final SessionHandler sessionHandler;
    private final MessageHandler messageHandler;
    private final Tutor currentTutor;

    private final JTextField emailField = new JTextField(20);
    private final JTextArea displayArea = new JTextArea(15, 50);
    private final JButton viewSessionsBtn = new JButton("View Sessions");
    private final JButton updateBioBtn = new JButton("Update My Bio");
    private final JButton viewMyProfileBtn = new JButton("View My Profile");
    private final JButton viewMyStudentsBtn = new JButton("View My Students");
    private final JButton backBtn = new JButton("Back");

    private final DefaultListModel<String> studentListModel = new DefaultListModel<>();
    private final JList<String> studentList = new JList<>(studentListModel);

    private Student selectedStudent;

    public TutorView(ProfileHandler profileHandler, SessionHandler sessionHandler,
                     MessageHandler messageHandler, Tutor tutor) {
        super("My Students");
        this.profileHandler = profileHandler;
        this.sessionHandler = sessionHandler;
        this.messageHandler = messageHandler;
        this.currentTutor = tutor;

        initializeUI();
        setupEventListeners();
        loadMyStudents();

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create the main button panel (5 buttons instead of 6)
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.add(viewSessionsBtn);
        buttonPanel.add(viewMyProfileBtn);
        buttonPanel.add(updateBioBtn);
        buttonPanel.add(viewMyStudentsBtn);
        buttonPanel.add(backBtn);

        // Input panel for student email
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Student Email:"));
        inputPanel.add(emailField);

        // Student list
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane studentScrollPane = new JScrollPane(studentList);

        // Display area
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea);

        // Layout
        add(buttonPanel, BorderLayout.WEST);
        add(inputPanel, BorderLayout.NORTH);
        add(studentScrollPane, BorderLayout.EAST);
        add(displayScrollPane, BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        viewSessionsBtn.addActionListener(e -> viewSessions());
        updateBioBtn.addActionListener(e -> updateBio());
        viewMyProfileBtn.addActionListener(e -> viewMyProfile());
        viewMyStudentsBtn.addActionListener(e -> loadMyStudents());
        backBtn.addActionListener(e -> dispose());

        // Student list selection listener
        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = studentList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String selectedEmail = studentListModel.getElementAt(selectedIndex);
                    emailField.setText(selectedEmail);
                }
            }
        });
    }

    private void viewSessions() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student email or select from the list.");
            return;
        }

        selectedStudent = profileHandler.getStudent(email);
        if (selectedStudent != null) {
            displayArea.setText("Sessions with " + selectedStudent.getName() + " (" + email + "):\n\n");

            // Fetch all sessions for this tutor with the selected student
            var allSessions = sessionHandler.getSessionsByTutor(currentTutor);
            var pastSessions = allSessions.stream()
                    .filter(s -> s.getStudent() != null && s.getStudent().getEmail().equals(email))
                    .filter(s -> s.getEndDateTime().isBefore(java.time.LocalDateTime.now()))
                    .toList();

            var upcomingSessions = allSessions.stream()
                    .filter(s -> s.getStudent() != null && s.getStudent().getEmail().equals(email))
                    .filter(s -> s.getStartDateTime().isAfter(java.time.LocalDateTime.now()))
                    .toList();

            // Show session summary
            displayArea.append("Total sessions: " + (pastSessions.size() + upcomingSessions.size()) + "\n");
            displayArea.append("Upcoming sessions: " + upcomingSessions.size() + "\n");
            displayArea.append("Past sessions: " + pastSessions.size() + "\n\n");

            if (!upcomingSessions.isEmpty()) {
                displayArea.append("=== UPCOMING SESSIONS ===\n");
                for (var session : upcomingSessions) {
                    displayArea.append("• " + formatSession(session) + "\n");
                }
                displayArea.append("\n");
            }

            if (!pastSessions.isEmpty()) {
                displayArea.append("=== PAST SESSIONS ===\n");
                for (var session : pastSessions) {
                    displayArea.append("• " + formatSession(session) + "\n");
                }
            }

            if (pastSessions.isEmpty() && upcomingSessions.isEmpty()) {
                displayArea.append("No sessions found with this student.\n");
            }
        } else {
            displayArea.setText("No student found with email: " + email);
            selectedStudent = null;
        }
    }

    private void updateBio() {
        String currentBio = currentTutor.getBio();
        String newBio = JOptionPane.showInputDialog(this, "Update your bio:", currentBio);

        if (newBio != null && !newBio.trim().isEmpty()) {
            currentTutor.setBio(newBio.trim());
            profileHandler.updateTutor(currentTutor);
            JOptionPane.showMessageDialog(this, "Bio updated successfully!");
        }
    }

    private void viewMyProfile() {
        displayArea.setText("My Profile:\n\n");
        displayArea.append(profileHandler.viewFullProfile(currentTutor));
    }

    private void loadMyStudents() {
        studentListModel.clear();

        // Get all sessions for this tutor and extract unique student emails
        var sessions = sessionHandler.getSessionsByTutor(currentTutor);
        var studentEmails = sessions.stream()
                .filter(s -> s.getStudent() != null)
                .map(s -> s.getStudent().getEmail())
                .distinct()
                .sorted()
                .toList();

        for (String email : studentEmails) {
            studentListModel.addElement(email);
        }

        displayArea.setText("My Students (" + studentEmails.size() + " total):\n\n");
        if (studentEmails.isEmpty()) {
            displayArea.append("No students found. You haven't had any sessions yet.\n");
        } else {
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

    private String formatSession(Session session) {
        return String.format("%s | %s | %s - %s",
                session.getCourseName(),
                session.getStartDateTime().toLocalDate(),
                session.getStartDateTime().toLocalTime(),
                session.getEndDateTime().toLocalTime());
    }
}