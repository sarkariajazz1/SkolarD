package skolard.presentation.auth;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
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
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.PersistenceRegistry;
import skolard.presentation.SkolardApp;
import skolard.presentation.faq.FAQView;
import skolard.utils.PasswordUtil;

public class SignUpView extends JFrame {

    // Input fields for user details
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);

    // Buttons for sign up and navigation
    private final JButton signUpStudentBtn = new JButton("Sign Up as Student");
    private final JButton signUpTutorBtn = new JButton("Sign Up as Tutor");
    private final JButton loginBtn = new JButton("Go to Login");
    private final JButton faqBtn = new JButton("FAQs");

    // Label to show form status messages
    private final JLabel statusLabel = new JLabel("Fill in the form to create your account");

    // Handlers and parent app reference
    private final ProfileHandler handler;
    private final LoginHandler loginHandler;
    private final SkolardApp parentApp;

    // Constructor sets up the UI and event listeners
    public SignUpView(ProfileHandler profileHandler, LoginHandler loginHandler, SkolardApp parentApp) {
        super("SkolarD - Sign Up");

        this.handler = profileHandler;
        this.loginHandler = loginHandler;
        this.parentApp = parentApp;

        // Assign names to components for UI testing frameworks
        setupComponentNames();

        // Use BorderLayout with spacing for main container
        setLayout(new BorderLayout(10, 10));

        // Form panel uses GridBagLayout for flexible positioning
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components

        // Add Full Name label and input field at grid position (0,0) and (1,0)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Add Email label and input field at (0,1) and (1,1)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Add Password label and input field at (0,2) and (1,2)
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Add Confirm Password label and input field at (0,3) and (1,3)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Add the form panel to the center of the frame
        add(formPanel, BorderLayout.CENTER);

        // Button panel with 2 rows and 2 columns, with 5px gaps
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.add(signUpStudentBtn);  // Sign up as student button
        buttonPanel.add(signUpTutorBtn);    // Sign up as tutor button
        buttonPanel.add(loginBtn);           // Go to login button
        buttonPanel.add(faqBtn);             // FAQs button
        add(buttonPanel, BorderLayout.SOUTH); // Add button panel at bottom

        // Center the status label horizontally and place it at the top
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // Attach event listeners to buttons
        setupEventListeners();

        // Set the frame to close on dispose (window close)
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();  // Pack to preferred size

        // Set window location safely relative to parent, handling headless environments
        safeSetLocationRelativeTo(parentApp);

        setVisible(true); // Show the window
    }

    /**
     * Safely sets the location relative to parent, handling headless environments.
     * If headless or no parent, centers on screen.
     */
    private void safeSetLocationRelativeTo(SkolardApp parent) {
        try {
            if (!GraphicsEnvironment.isHeadless() && parent != null) {
                setLocationRelativeTo(parent);
            } else {
                // Center on screen if not headless, else ignore
                if (!GraphicsEnvironment.isHeadless()) {
                    setLocationRelativeTo(null);
                }
            }
        } catch (Exception e) {
            // Ignore exceptions related to graphics environment
        }
    }

    /**
     * Assign unique names to UI components for automated UI testing.
     */
    private void setupComponentNames() {
        nameField.setName("nameField");
        emailField.setName("emailField");
        passwordField.setName("passwordField");
        confirmPasswordField.setName("confirmPasswordField");
        signUpStudentBtn.setName("signUpStudentBtn");
        signUpTutorBtn.setName("signUpTutorBtn");
        loginBtn.setName("loginBtn");
        faqBtn.setName("faqBtn");
        statusLabel.setName("statusLabel");
    }

    /**
     * Attach event handlers for buttons.
     * Handles sign up logic for students and tutors,
     * navigation to login, and FAQ view.
     */
    private void setupEventListeners() {
        // Sign Up as Student button handler
        signUpStudentBtn.addActionListener(e -> {
            if (validateForm()) {
                try {
                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    String hashedPassword = PasswordUtil.hash(password);

                    // Add new student via profile handler
                    handler.addStudent(name, email, hashedPassword);
                    Student newStudent = handler.getStudent(email);

                    // Show success message and update status label
                    statusLabel.setText("Student account created successfully!");
                    JOptionPane.showMessageDialog(this,
                            "Student account created successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Notify parent app of successful signup authentication
                    parentApp.onAuthenticationSuccess(newStudent, true);
                    dispose(); // Close sign-up window

                } catch (RuntimeException ex) {
                    // Handle duplicate email or other validation errors
                    String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
                    if (msg.contains("unique") || msg.contains("already")) {
                        statusLabel.setText("Email already registered");
                        JOptionPane.showMessageDialog(this,
                                "An account with this email already exists. Please use another email.",
                                "Duplicate Email", JOptionPane.WARNING_MESSAGE);
                    } else {
                        statusLabel.setText("Error: " + ex.getMessage());
                        JOptionPane.showMessageDialog(this,
                                "Error creating account: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    // Log unexpected exceptions and inform the user
                    java.util.logging.Logger.getLogger(SignUpView.class.getName())
                            .log(java.util.logging.Level.SEVERE, null, ex);
                    statusLabel.setText("Unexpected error occurred");
                    JOptionPane.showMessageDialog(this,
                            "An unexpected error occurred. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Sign Up as Tutor button handler
        signUpTutorBtn.addActionListener(e -> {
            if (validateForm()) {
                try {
                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    String hashedPassword = PasswordUtil.hash(password);

                    // Add new tutor via profile handler
                    handler.addTutor(name, email, hashedPassword);
                    Tutor newTutor = handler.getTutor(email);

                    // Show success message and update status label
                    statusLabel.setText("Tutor account created successfully!");
                    JOptionPane.showMessageDialog(this,
                            "Tutor account created successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Notify parent app of successful signup authentication
                    parentApp.onAuthenticationSuccess(newTutor, true);
                    dispose(); // Close sign-up window

                } catch (RuntimeException ex) {
                    // Handle duplicate email or other validation errors
                    String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
                    if (msg.contains("unique") || msg.contains("already")) {
                        statusLabel.setText("Email already registered");
                        JOptionPane.showMessageDialog(this,
                                "An account with this email already exists. Please use another email.",
                                "Duplicate Email", JOptionPane.WARNING_MESSAGE);
                    } else {
                        statusLabel.setText("Error: " + ex.getMessage());
                        JOptionPane.showMessageDialog(this,
                                "Error creating account: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    // Log unexpected exceptions and inform the user
                    java.util.logging.Logger.getLogger(SignUpView.class.getName())
                            .log(java.util.logging.Level.SEVERE, null, ex);
                    statusLabel.setText("Unexpected error occurred");
                    JOptionPane.showMessageDialog(this,
                            "An unexpected error occurred. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Go to Login button opens the LoginView and closes this window
        loginBtn.addActionListener(e -> {
            new LoginView(handler, loginHandler, parentApp);
            dispose();
        });

        // FAQ button opens the FAQ view window
        faqBtn.addActionListener(e -> new FAQView(new FAQHandler(PersistenceRegistry.getFAQPersistence())));
    }

    /**
     * Validates user input on the sign-up form.
     * Checks for non-empty fields, password matching,
     * password length, and simple email format.
     * @return true if the form is valid, false otherwise
     */
    private boolean validateForm() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Check for empty fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check password length requirement
        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters");
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Simple email format check for '@' and '.'
        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("Please enter a valid email address");
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true; // All validations passed
    }
}
