
package skolard.presentation.profile;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import skolard.logic.profile.ProfileHandler;
import skolard.objects.Tutor;
import skolard.objects.User;

/**
 * GUI window for viewing and managing user profiles in SkolarD.
 * Fixed version with improved error handling, validation, and user experience.
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
    private final JButton clearBtn = new JButton("Clear");
    private final JLabel statusLabel = new JLabel("Enter an email to view a profile", SwingConstants.CENTER);

    private ProfileHandler handler;
    private User currentUser; // Stores the currently loaded user

    public ProfileView(ProfileHandler profileHandler) {
        super("SkolarD - Profile Management");

        // Load persistence handlers
        this.handler = profileHandler;

        initializeUI();
        setupEventHandlers();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only closes this window
        pack();
        setLocationRelativeTo(null); // Center screen
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10)); // Layout with spacing

        // Title label
        JLabel titleLabel = new JLabel("Profile Management", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for email input and search
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Profile"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        searchPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        searchPanel.add(fetchTutorBtn, gbc);
        gbc.gridx = 1;
        searchPanel.add(fetchStudentBtn, gbc);
        gbc.gridx = 2;
        searchPanel.add(clearBtn, gbc);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Center area displays the user profile
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder("Profile Information"));

        profileArea.setEditable(false);
        profileArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        profileArea.setBackground(getBackground());
        profileArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        profilePanel.add(new JScrollPane(profileArea), BorderLayout.CENTER);

        mainPanel.add(profilePanel, BorderLayout.CENTER);

        // Bottom panel for action buttons
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionPanel.add(updateBioBtn);
        actionPanel.add(addStudentBtn);
        actionPanel.add(addTutorBtn);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Status label at bottom
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        add(statusLabel, BorderLayout.SOUTH);

        // Initially disable action buttons
        updateBioBtn.setEnabled(false);
        clearProfileDisplay();
    }

    private void setupEventHandlers() {
        // Fetch tutor profile by email
        fetchTutorBtn.addActionListener(e -> fetchTutorProfile());

        // Fetch student profile by email
        fetchStudentBtn.addActionListener(e -> fetchStudentProfile());

        // Clear profile display
        clearBtn.addActionListener(e -> clearProfileDisplay());

        // Allow updating tutor bios
        updateBioBtn.addActionListener(e -> updateTutorBio());

        // Add new student
        addStudentBtn.addActionListener(e -> addNewStudent());

        // Add new tutor
        addTutorBtn.addActionListener(e -> addNewTutor());

        // Enable Enter key for search
        emailField.addActionListener(e -> {
            if (!emailField.getText().trim().isEmpty()) {
                fetchTutorProfile(); // Default to tutor search
            }
        });
    }

    private void fetchTutorProfile() {
        String email = emailField.getText().trim();
        if (!validateEmail(email)) {
            return;
        }

        try {
            currentUser = handler.getTutor(email);
            if (currentUser != null) {
                profileArea.setText(handler.viewFullProfile(currentUser));
                statusLabel.setText("Tutor profile loaded successfully");
                updateBioBtn.setEnabled(true);
            } else {
                profileArea.setText("No tutor found with email: " + email);
                statusLabel.setText("Tutor not found");
                updateBioBtn.setEnabled(false);
                currentUser = null;
            }
        } catch (Exception ex) {
            showError("Error loading tutor profile: " + ex.getMessage());
            updateBioBtn.setEnabled(false);
            currentUser = null;
        }
    }

    private void fetchStudentProfile() {
        String email = emailField.getText().trim();
        if (!validateEmail(email)) {
            return;
        }

        try {
            currentUser = handler.getStudent(email);
            if (currentUser != null) {
                profileArea.setText(handler.viewFullProfile(currentUser));
                statusLabel.setText("Student profile loaded successfully");
                updateBioBtn.setEnabled(false); // Students don't have bios
            } else {
                profileArea.setText("No student found with email: " + email);
                statusLabel.setText("Student not found");
                updateBioBtn.setEnabled(false);
                currentUser = null;
            }
        } catch (Exception ex) {
            showError("Error loading student profile: " + ex.getMessage());
            updateBioBtn.setEnabled(false);
            currentUser = null;
        }
    }

    private void clearProfileDisplay() {
        profileArea.setText("No profile loaded.\n\nEnter an email address above and click either " +
                "'View Tutor Profile' or 'View Student Profile' to load profile information.");
        statusLabel.setText("Ready to search");
        currentUser = null;
        updateBioBtn.setEnabled(false);
    }

    private void updateTutorBio() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please load a tutor profile first.",
                    "No Profile Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!(currentUser instanceof Tutor)) {
            JOptionPane.showMessageDialog(this, "Only tutors can update bios.\nStudents do not have bio fields.",
                    "Invalid Operation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Tutor tutor = (Tutor) currentUser;
        String currentBio = tutor.getBio() != null ? tutor.getBio() : "";

        JTextArea bioArea = new JTextArea(5, 30);
        bioArea.setText(currentBio);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(bioArea);
        Object[] message = {"Current bio:", scrollPane, "Enter new bio:"};

        int option = JOptionPane.showConfirmDialog(this, message, "Update Bio",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String newBio = bioArea.getText().trim();
            if (!newBio.isEmpty()) {
                try {
                    handler.updateBio(tutor, newBio);
                    profileArea.setText(handler.viewFullProfile(currentUser));
                    statusLabel.setText("Bio updated successfully");
                    JOptionPane.showMessageDialog(this, "Bio updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    showError("Error updating bio: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Bio cannot be empty.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void addNewStudent() {
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add New Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (validateStudentInput(name, email, password)) {
                try {
                    handler.addStudent(name, email, password);
                    statusLabel.setText("Student added successfully");
                    JOptionPane.showMessageDialog(this, "Student '" + name + "' added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Clear the email field and load the new student
                    this.emailField.setText(email);
                    fetchStudentProfile();
                } catch (Exception ex) {
                    showError("Error adding student: " + ex.getMessage());
                }
            }
        }
    }

    private void addNewTutor() {
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);

        int option = JOptionPane.showConfirmDialog(this, panel, "Add New Tutor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (validateTutorInput(name, email, password)) {
                try {
                    handler.addTutor(name, email, password);
                    statusLabel.setText("Tutor added successfully");
                    JOptionPane.showMessageDialog(this, "Tutor '" + name + "' added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Clear the email field and load the new tutor
                    this.emailField.setText(email);
                    fetchTutorProfile();
                } catch (Exception ex) {
                    showError("Error adding tutor: " + ex.getMessage());
                }
            }
        }
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            statusLabel.setText("Please enter an email address");
            JOptionPane.showMessageDialog(this, "Please enter an email address.",
                    "Missing Email", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("Invalid email format");
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                    "Invalid Email", JOptionPane.WARNING_MESSAGE);
            emailField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateStudentInput(String name, String email, String password) {
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!validateEmailForAdd(email)) {
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.",
                    "Invalid Password", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validateTutorInput(String name, String email, String password) {
        return validateStudentInput(name, email, password); // Same validation for now
    }

    private boolean validateEmailForAdd(String email) {
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email cannot be empty.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                    "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void showError(String message) {
        statusLabel.setText("Error occurred");
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        profileArea.setText("Error loading profile. Please try again.");
    }
}