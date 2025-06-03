package skolard.presentation;

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

import skolard.logic.MessageHandler;
import skolard.logic.ProfileHandler;
import skolard.logic.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;

/**
 * GUI window for tutor-specific functionality in SkolarD.
 * Allows tutors to view student profiles, manage sessions, and send messages.
 */
public class TutorView extends JFrame {

    private final ProfileHandler profileHandler;
    private final SessionHandler sessionHandler;
    private final MessageHandler messageHandler;
    private final Tutor currentTutor;

    // UI Components
    private final JTextField emailField = new JTextField(20);
    private final JTextArea displayArea = new JTextArea(15, 50);
    private final JButton viewStudentBtn = new JButton("View Student Profile");
    private final JButton messageStudentBtn = new JButton("Message Student");
    private final JButton updateBioBtn = new JButton("Update My Bio");
    private final JButton viewMyProfileBtn = new JButton("View My Profile");
    private final JButton viewMyStudentsBtn = new JButton("View My Students");

    private final DefaultListModel<String> studentListModel = new DefaultListModel<>();
    private final JList<String> studentList = new JList<>(studentListModel);

    private Student selectedStudent; // Currently selected student

    public TutorView(ProfileHandler profileHandler, SessionHandler sessionHandler,
                     MessageHandler messageHandler, Tutor tutor) {
        super("SkolarD - Tutor Dashboard");

        this.profileHandler = profileHandler;
        this.sessionHandler = sessionHandler;
        this.messageHandler = messageHandler;
        this.currentTutor = tutor;

        initializeUI();
        setupEventListeners();
        loadMyStudents();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Welcome panel
        JPanel welcomePanel = new JPanel(new FlowLayout());
        welcomePanel.add(new JLabel("Welcome, " + currentTutor.getName() + " (Tutor)"));
        add(welcomePanel, BorderLayout.NORTH);

        // Left panel for student list and search
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("My Students:"), BorderLayout.NORTH);

        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftPanel.add(new JScrollPane(studentList), BorderLayout.CENTER);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Student Email:"));
        searchPanel.add(emailField);
        searchPanel.add(viewStudentBtn);
        leftPanel.add(searchPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // Center display area
        displayArea.setEditable(false);
        displayArea.setText("Welcome to your tutor dashboard!\n\n" +
                "• Select a student from the list to view their profile\n" +
                "• Enter a student email to search for specific students\n" +
                "• Manage your profile and bio using the buttons below");
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.add(messageStudentBtn);
        buttonPanel.add(updateBioBtn);
        buttonPanel.add(viewMyProfileBtn);
        buttonPanel.add(viewMyStudentsBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initially disable student-specific buttons
        messageStudentBtn.setEnabled(false);
    }

    private void setupEventListeners() {
        viewStudentBtn.addActionListener(e -> viewStudentProfile());
        messageStudentBtn.addActionListener(e -> messageStudent());
        updateBioBtn.addActionListener(e -> updateBio());
        viewMyProfileBtn.addActionListener(e -> viewMyProfile());
        viewMyStudentsBtn.addActionListener(e -> loadMyStudents());

        // Handle student list selection
        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedEmail = studentList.getSelectedValue();
                if (selectedEmail != null) {
                    // Extract email from the list item (assuming format: "Name (email)")
                    int startIndex = selectedEmail.lastIndexOf('(') + 1;
                    int endIndex = selectedEmail.lastIndexOf(')');
                    if (startIndex > 0 && endIndex > startIndex) {
                        String email = selectedEmail.substring(startIndex, endIndex);
                        emailField.setText(email);
                        viewStudentProfile();
                    }
                }
            }
        });
    }

    private void viewStudentProfile() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student email.");
            return;
        }

        selectedStudent = profileHandler.getStudent(email);
        if (selectedStudent != null) {
            displayArea.setText("Student Profile:\n\n");
            displayArea.append(profileHandler.viewFullProfile(selectedStudent));

            // Show student's sessions with this tutor
            displayArea.append("\n\nSessions with this student:\n");
            var pastSessions = currentTutor.getPastSessions().stream()
                    .filter(session -> session.getStudent() != null && session.getStudent().getEmail().equals(email))
                    .toList();
            var upcomingSessions = currentTutor.getUpcomingSessions().stream()
                    .filter(session -> session.getStudent() != null && session.getStudent().getEmail().equals(email))
                    .toList();

            if (!pastSessions.isEmpty()) {
                displayArea.append("Past sessions:\n");
                pastSessions.forEach(session -> displayArea.append("- " + session.toString() + "\n"));
            }

            if (!upcomingSessions.isEmpty()) {
                displayArea.append("Upcoming sessions:\n");
                upcomingSessions.forEach(session -> displayArea.append("- " + session.toString() + "\n"));
            }

            if (pastSessions.isEmpty() && upcomingSessions.isEmpty()) {
                displayArea.append("No sessions found with this student.\n");
            }

            messageStudentBtn.setEnabled(true);
        } else {
            displayArea.setText("No student found with email: " + email);
            selectedStudent = null;
            messageStudentBtn.setEnabled(false);
        }
    }

    private void messageStudent() {
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Please select a student first.");
            return;
        }

        String message = JOptionPane.showInputDialog(this,
                "Enter your message to " + selectedStudent.getName() + ":");

        if (message != null && !message.trim().isEmpty()) {
            // Here you would typically send the message using MessageHandler
            JOptionPane.showMessageDialog(this,
                    "Message sent to " + selectedStudent.getName() + "!");

            displayArea.append("\n\nMessage sent to " + selectedStudent.getName());
        }
    }

    private void updateBio() {
        String currentBio = currentTutor.getBio();
        String newBio = JOptionPane.showInputDialog(this, "Enter your new bio:", currentBio);

        if (newBio != null && !newBio.trim().isEmpty()) {
            profileHandler.updateBio(currentTutor, newBio.trim());
            JOptionPane.showMessageDialog(this, "Bio updated successfully!");
            displayArea.append("\n\nBio updated.");
        }
    }

    private void viewMyProfile() {
        displayArea.setText("My Profile:\n\n");
        displayArea.append(profileHandler.viewFullProfile(currentTutor));

        selectedStudent = null;
        messageStudentBtn.setEnabled(false);
    }

    private void loadMyStudents() {
        studentListModel.clear();
        displayArea.setText("Loading students you have tutored...\n\n");

        // Get students from past and upcoming sessions
        var allSessions = currentTutor.getPastSessions();
        allSessions.addAll(currentTutor.getUpcomingSessions());

        var studentEmails = allSessions.stream()
                .filter(session -> session.getStudent() != null)
                .map(session -> session.getStudent().getEmail())
                .distinct()
                .toList();

        if (studentEmails.isEmpty()) {
            displayArea.append("You haven't tutored any students yet.\n");
            displayArea.append("Students will appear here once you have scheduled sessions.");
            return;
        }

        displayArea.append("Students you have tutored or will tutor:\n\n");

        for (String email : studentEmails) {
            Student student = profileHandler.getStudent(email);
            if (student != null) {
                String listItem = student.getName() + " (" + email + ")";
                studentListModel.addElement(listItem);
                displayArea.append("- " + student.getName() + " (" + email + ")\n");
            }
        }

        displayArea.append("\nClick on a student from the list to view their profile.");
    }
}