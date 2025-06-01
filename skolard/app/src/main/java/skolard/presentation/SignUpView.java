package skolard.presentation;

import javax.swing.*;
import java.awt.*;
import skolard.logic.ProfileHandler;
import skolard.logic.FAQHandler;

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
    private SkolardApp parentApp;

    public SignUpView(ProfileHandler profileHandler, SkolardApp parentApp) {
        super("SkolarD - Sign Up");

        this.handler = profileHandler;
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
                    handler.addStudent(nameField.getText().trim(), emailField.getText().trim());
                    statusLabel.setText("Student account created successfully!");
                    JOptionPane.showMessageDialog(this,
                            "Student account created for: " + nameField.getText().trim() +
                                    "\nYou can now access the dashboard!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Notify parent app of successful authentication
                    parentApp.onAuthenticationSuccess();
                    dispose(); // Close signup window
                } catch (IllegalArgumentException ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            }
        });

        // Sign up as tutor
        signUpTutorBtn.addActionListener(e -> {
            if (validateForm()) {
                try {
                    handler.addTutor(nameField.getText().trim(), emailField.getText().trim());
                    statusLabel.setText("Tutor account created successfully!");
                    JOptionPane.showMessageDialog(this,
                            "Tutor account created for: " + nameField.getText().trim() +
                                    "\nYou can now access the dashboard!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Notify parent app of successful authentication
                    parentApp.onAuthenticationSuccess();
                    dispose(); // Close signup window
                } catch (IllegalArgumentException ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            }
        });

        // Switch to login
        loginBtn.addActionListener(e -> {
            new LoginView(handler, parentApp);
            dispose(); // Close signup window
        });

        // Open FAQ
        faqBtn.addActionListener(e -> new FAQView(new FAQHandler()));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parentApp);
        setVisible(true);
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

        return true;
    }
}