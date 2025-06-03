package skolard.presentation.auth;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.*;

import skolard.logic.auth.LoginHandler;
import skolard.logic.faq.FAQHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.objects.LoginCredentials;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.persistence.PersistenceRegistry;
import skolard.presentation.SkolardApp;
import skolard.presentation.faq.FAQView;

/**
 * GUI window for user registration in SkolarD.
 * Allows new users to sign up as either a student or tutor.
 */
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

        // Sign Up as Student
        signUpStudentBtn.addActionListener(e -> {
            if (validateForm()) {
                try {
                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());

                    handler.addStudent(name, email, password);
                    storeLoginCredentials(email, password, "student");

                    Student newStudent = handler.getStudent(email);

                    statusLabel.setText("Student account created successfully!");
                    JOptionPane.showMessageDialog(this,
                            "Student account created for: " + name,
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    parentApp.onAuthenticationSuccess(newStudent);
                    dispose();

                } catch (IllegalArgumentException ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this,
                            "Error creating account: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
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

                    handler.addTutor(name, email, password);
                    storeLoginCredentials(email, password, "tutor");

                    Tutor newTutor = handler.getTutor(email);

                    statusLabel.setText("Tutor account created successfully!");
                    JOptionPane.showMessageDialog(this,
                            "Tutor account created for: " + name,
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    parentApp.onAuthenticationSuccess(newTutor);
                    dispose();

                } catch (IllegalArgumentException ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this,
                            "Error creating account: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
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

        faqBtn.addActionListener(e -> new FAQView(new FAQHandler()));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parentApp);
        setVisible(true);
    }

    /**
     * Store credentials in the LoginPersistence layer.
     * Requires addLoginCredentials() to be implemented in the persistence layer.
     */
    private void storeLoginCredentials(String email, String password, String role) {
        try {
            LoginCredentials creds = new LoginCredentials(email, password, role);
            var loginPersistence = PersistenceRegistry.getLoginPersistence();
            loginPersistence.addLoginCredentials(creds); // ✅ Ensure this method exists!
        } catch (Exception e) {
            System.err.println("Warning: Could not store login credentials: " + e.getMessage());
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
