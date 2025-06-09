package skolard.presentation;

import skolard.logic.ProfileHandler;
import skolard.objects.User;

import javax.swing.*;
import java.awt.*;

/**
 * GUI window for viewing and managing user profiles in SkolarD.
 */
public class ProfileView extends JFrame {

    // UI Components
    private final JTextField emailField = new JTextField(20);
    private final JTextArea profileArea = new JTextArea(15, 40);
    private final JButton fetchTutorBtn = new JButton("View Tutor Profile");
    private final JButton fetchStudentBtn = new JButton("View Student Profile");
    private final JButton updateBioBtn = new JButton("Update Bio");
    private final JButton addStudentBtn = new JButton("Add Student");
    private final JButton addTutorBtn = new JButton("Add Tutor");

    private ProfileHandler handler;
    private User currentUser; // Stores the currently loaded user

    public ProfileView(ProfileHandler profileHandler) {
        super("SkolarD - Profile Viewer");

        // Load persistence handlers
        this.handler = profileHandler;

        setLayout(new BorderLayout(10, 10)); // Layout with spacing

        // Top panel for email input and fetch
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Email:"));
        topPanel.add(emailField);
        topPanel.add(fetchTutorBtn);
        topPanel.add(fetchStudentBtn);
        add(topPanel, BorderLayout.NORTH);

        // Center area displays the user profile
        profileArea.setEditable(false);
        add(new JScrollPane(profileArea), BorderLayout.CENTER);

        // Bottom panel for action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(updateBioBtn);
        bottomPanel.add(addStudentBtn);
        bottomPanel.add(addTutorBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Fetch tutor profile by email
        fetchTutorBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (!email.isEmpty()) {
                currentUser = handler.getTutor(email);
                profileArea.setText(currentUser != null
                    ? handler.viewFullProfile(currentUser)
                    : "No tutor found with that email.");
            }
        });

        // Fetch student profile by email
        fetchStudentBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (!email.isEmpty()) {
                currentUser = handler.getStudent(email);
                profileArea.setText(currentUser != null
                    ? handler.viewFullProfile(currentUser)
                    : "No found found with that email.");
            }
        });

        // Allow updating tutor bios
        updateBioBtn.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Load a tutor profile first.");
                return;
            }
            if (!(currentUser instanceof skolard.objects.Tutor)) {
                JOptionPane.showMessageDialog(this, "Only tutors can update bios.");
                return;
            }
            String newBio = JOptionPane.showInputDialog(this, "Enter new bio:");
            if (newBio != null && !newBio.isBlank()) {
                handler.updateBio(currentUser, newBio.trim());
                profileArea.setText(handler.viewFullProfile(currentUser));
            }
        });

        // Add new student
        addStudentBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            Object[] fields = {"Name:", nameField, "Email:", emailField};
            int option = JOptionPane.showConfirmDialog(this, fields, "Add Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                handler.addStudent(nameField.getText().trim(), emailField.getText().trim());
                JOptionPane.showMessageDialog(this, "Student added!");
            }
        });

        // Add new tutor
        addTutorBtn.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            Object[] fields = {"Name:", nameField, "Email:", emailField};
            int option = JOptionPane.showConfirmDialog(this, fields, "Add Tutor", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                handler.addTutor(nameField.getText().trim(), emailField.getText().trim());
                JOptionPane.showMessageDialog(this, "Tutor added!");
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only closes this window
        pack();
        setLocationRelativeTo(null); // Center screen
        setVisible(true);
    }
}
