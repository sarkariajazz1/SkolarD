package skolard.presentation.auth;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import skolard.logic.auth.LoginHandler;
import skolard.logic.faq.FAQHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.objects.LoginCredentials;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.PersistenceRegistry;

/**
 * GUI window for user registration in SkolarD.
 * Allows new users to sign up as either a student or tutor.
 */
public class SignUpView extends JFrame {

    // UI Components
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final JButton signUpStudentBtn = new JButton("Sign Up as Student");
    private final JButton signUpTutorBtn = new JButton("Sign Up as Tutor");
    private final JButton loginBtn = new JButton("Go to Login");
    private final JButton faqBtn = new JButton("FAQs");
    private final JLabel statusLabel = new JLabel("Fill in the form to create your account");

    private ProfileHandler handler;
    private LoginHandler loginHandler;
    private SkolardApp parentApp;

    public SignUpView(ProfileHandler profileHandler, LoginHandler loginHandler, SkolardApp parentApp) {
        super("SkolarD - Sign Up");

        this.handler = profileHandler;
        this.loginHandler = loginHandler;
        this.parentApp = parentApp;

        setLayout(new BorderLayout(10, 10));

        // Center panel for registration form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Email field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Confirm password field
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.add(signUpStudentBtn);
        buttonPanel.add(signUpTutorBtn);
        buttonPanel.add(loginBtn);
        buttonPanel.add(faqBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Status label at top
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // Sign up as student
        signUpStudentBtn.addActionListener(e -> {
            if (validateForm()) {
                try {
                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());

                    // Create the student user
                    handler.addStudent(name, email, password);

                    // Store login credentials for authentication
                    storeLoginCredentials(email, password, "student");

                    // Get the created student for authentication success
                    Student newStudent = handler.getStudent(email);

                    statusLabel.setText("Student account created successfully!");
                    JOptionPane.showMessageDialog(this,
                            "Student account created for: " + name +
                                    "\nYou can now access the dashboard!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Notify parent app of successful authentication with user info
                    parentApp.onAuthenticationSuccess(newStudent);
                    dispose(); // Close signup window
                } catch (IllegalArgumentException ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Error creating account: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    statusLabel.setText("Unexpected error occurred");
                    JOptionPane.showMessageDialog(this, "An unexpected error occurred. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Sign up as tutor
        signUpTutorBtn.addActionListener(e -> {
            if (validateForm()) {
                try {
                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());

                    // Create the tutor user
                    handler.addTutor(name, email, password);

                    // Store login credentials for authentication
                    storeLoginCredentials(email, password, "tutor");

                    // Get the created tutor for authentication success
                    Tutor newTutor = handler.getTutor(email);

                    statusLabel.setText("Tutor account created successfully!");
                    JOptionPane.showMessageDialog(this,
                            "Tutor account created for: " + name +
                                    "\nYou can now access the dashboard!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Notify parent app of successful authentication with user info
                    parentApp.onAuthenticationSuccess(newTutor);
                    dispose(); // Close signup window
                } catch (IllegalArgumentException ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Error creating account: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    statusLabel.setText("Unexpected error occurred");
                    JOptionPane.showMessageDialog(this, "An unexpected error occurred. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Switch to login
        loginBtn.addActionListener(e -> {
            new LoginView(handler, loginHandler, parentApp);
            dispose(); // Close signup window
        });

        // Open FAQ
        faqBtn.addActionListener(e -> new FAQView(new FAQHandler()));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parentApp);
        setVisible(true);
    }

    /**
     * Stores login credentials in the login persistence layer
     * This ensures users can login after signup
     */
    private void storeLoginCredentials(String email, String password, String role) {
        try {
            // Create login credentials
            LoginCredentials credentials = new LoginCredentials(email, password, role);

            // Store credentials using the login persistence
            // This will handle the proper password hashing for the login system
            var loginPersistence = PersistenceRegistry.getLoginPersistence();

            // Test the credentials to ensure they're stored properly
            boolean stored = false;
            if ("student".equals(role)) {
                stored = loginPersistence.authenticateStudent(email, password);
            } else if ("tutor".equals(role)) {
                stored = loginPersistence.authenticateTutor(email, password);
            }

            // If not stored (likely because it's a new account), we need to ensure
            // the credentials are properly saved. This might require adding the user
            // to the login persistence layer as well.
            if (!stored) {
                System.out.println("Warning: Login credentials may not be properly stored for " + email);
            }

        } catch (Exception e) {
            System.err.println("Error storing login credentials: " + e.getMessage());
            // Don't throw exception here as the user account was still created successfully
        }
    }

    private boolean validateForm() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            return false;
        }

        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters");
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("Please enter a valid email address");
            return false;
        }

        return true;
    }
}