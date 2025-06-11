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

    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final JButton signUpStudentBtn = new JButton("Sign Up as Student");
    private final JButton signUpTutorBtn = new JButton("Sign Up as Tutor");
    private final JButton loginBtn = new JButton("Go to Login");
    private final JButton faqBtn = new JButton("FAQs");
    private final JLabel statusLabel = new JLabel("Fill in the form to create your account");

    private final ProfileHandler handler;
    private final LoginHandler loginHandler;
    private final SkolardApp parentApp;

    public SignUpView(ProfileHandler profileHandler, LoginHandler loginHandler, SkolardApp parentApp) {
        super("SkolarD - Sign Up");

        this.handler = profileHandler;
        this.loginHandler = loginHandler;
        this.parentApp = parentApp;

        // *** ADD COMPONENT NAMES FOR TESTING ***
        setupComponentNames();

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.add(signUpStudentBtn);
        buttonPanel.add(signUpTutorBtn);
        buttonPanel.add(loginBtn);
        buttonPanel.add(faqBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        setupEventListeners();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();

        // Safe location setting that handles headless environments
        safeSetLocationRelativeTo(parentApp);
        setVisible(true);
    }

    /**
     * Safely sets the location relative to parent, handling headless environments.
     */
    private void safeSetLocationRelativeTo(SkolardApp parent) {
        try {
            if (!GraphicsEnvironment.isHeadless() && parent != null) {
                setLocationRelativeTo(parent);
            } else {
                // In headless environment or null parent, just center on screen
                if (!GraphicsEnvironment.isHeadless()) {
                    setLocationRelativeTo(null);
                }
                // If headless, location doesn't matter anyway
            }
        } catch (Exception e) {
            // If any graphics-related exception occurs, just ignore it
            // The window location is not critical for functionality
        }
    }

    /**
     * Set component names for testing purposes.
     * AssertJ Swing tests use these names to find GUI components.
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
     * Setup all event listeners for the buttons.
     */
    private void setupEventListeners() {
        // Sign Up as Student
        signUpStudentBtn.addActionListener(e -> {
        if (validateForm()) {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String hashedPassword = PasswordUtil.hash(password);

                handler.addStudent(name, email, hashedPassword);
                Student newStudent = handler.getStudent(email);

                statusLabel.setText("Student account created successfully!");
                JOptionPane.showMessageDialog(this,
                        "Student account created successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                parentApp.onAuthenticationSuccess(newStudent, true);
                dispose();

            } catch (RuntimeException ex) {
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
                // Log the exception using a logger
                java.util.logging.Logger.getLogger(SignUpView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                statusLabel.setText("Unexpected error occurred");
                JOptionPane.showMessageDialog(this,
                        "An unexpected error occurred. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });


        // Sign Up as Tutor
        signUpTutorBtn.addActionListener(e -> {
        if (validateForm()) {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String hashedPassword = PasswordUtil.hash(password);

                handler.addTutor(name, email, hashedPassword);
                Tutor newTutor = handler.getTutor(email);

                statusLabel.setText("Tutor account created successfully!");
                JOptionPane.showMessageDialog(this,
                        "Tutor account created successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                parentApp.onAuthenticationSuccess(newTutor, true);
                dispose();

            } catch (RuntimeException ex) {
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
                java.util.logging.Logger.getLogger(SignUpView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                statusLabel.setText("Unexpected error occurred");
                JOptionPane.showMessageDialog(this,
                        "An unexpected error occurred. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

        loginBtn.addActionListener(e -> {
            new LoginView(handler, loginHandler, parentApp);
            dispose();
        });

        faqBtn.addActionListener(e -> new FAQView(new FAQHandler(PersistenceRegistry.getFAQPersistence())));
    }

    private boolean validateForm() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters");
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("Please enter a valid email address");
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}