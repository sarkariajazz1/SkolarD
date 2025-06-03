package skolard.presentation.auth;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.*;

import skolard.presentation.SkolardApp;
import skolard.presentation.faq.FAQView;
import skolard.presentation.support.SupportView;
import skolard.logic.auth.LoginHandler;
import skolard.logic.faq.FAQHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.support.SupportHandler;
import skolard.objects.LoginCredentials;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;

public class LoginView extends JFrame {

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginStudentBtn = new JButton("Login as Student");
    private final JButton loginTutorBtn = new JButton("Login as Tutor");
    private final JButton loginSupportBtn = new JButton("Login as Support"); // NEW
    private final JButton signUpBtn = new JButton("Go to Sign Up");
    private final JButton faqBtn = new JButton("FAQs");
    private final JLabel statusLabel = new JLabel("Enter your credentials to login");

    private final ProfileHandler profileHandler;
    private final SkolardApp parentApp;
    private final LoginHandler loginHandler;

    public LoginView(ProfileHandler profileHandler, LoginHandler loginHandler, SkolardApp parentApp) {
        super("SkolarD - Login");

        this.profileHandler = profileHandler;
        this.loginHandler = loginHandler;
        this.parentApp = parentApp;

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 5, 5)); // updated to fit all 5 buttons
        buttonPanel.add(loginStudentBtn);
        buttonPanel.add(loginTutorBtn);
        buttonPanel.add(loginSupportBtn); // NEW
        buttonPanel.add(signUpBtn);
        buttonPanel.add(faqBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // Login as Student
        loginStudentBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password");
                return;
            }

            LoginCredentials creds = new LoginCredentials(email, password, "student");
            if (loginHandler.login(creds)) {
                User student = profileHandler.getStudent(email);
                if (student != null) {
                    statusLabel.setText("Login successful! Welcome " + student.getName());
                    JOptionPane.showMessageDialog(this, "Login successful as student: " + student.getName(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    parentApp.onAuthenticationSuccess(student);
                    dispose();
                } else {
                    showProfileNotFound("Student");
                }
            } else {
                showLoginFailed("student");
            }
        });

        // Login as Tutor
        loginTutorBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password");
                return;
            }

            LoginCredentials creds = new LoginCredentials(email, password, "tutor");
            if (loginHandler.login(creds)) {
                User tutor = profileHandler.getTutor(email);
                if (tutor != null) {
                    statusLabel.setText("Login successful! Welcome " + tutor.getName());
                    JOptionPane.showMessageDialog(this, "Login successful as tutor: " + tutor.getName(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    parentApp.onAuthenticationSuccess(tutor);
                    dispose();
                } else {
                    showProfileNotFound("Tutor");
                }
            } else {
                showLoginFailed("tutor");
            }
        });

        // Login as Support
        loginSupportBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password");
                return;
            }

            LoginCredentials creds = new LoginCredentials(email, password, "support");
            if (loginHandler.login(creds)) {
                SupportHandler supportHandler = new SupportHandler(PersistenceRegistry.getSupportPersistence());
                new SupportView(supportHandler);
                dispose();
            } else {
                showLoginFailed("support");
            }
        });

        signUpBtn.addActionListener(e -> {
            new SignUpView(profileHandler, loginHandler, parentApp);
            dispose();
        });

        faqBtn.addActionListener(e -> new FAQView(new FAQHandler()));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parentApp);
        setVisible(true);
    }

    private void showLoginFailed(String role) {
        statusLabel.setText("Invalid " + role + " credentials");
        JOptionPane.showMessageDialog(this,
                "Invalid email or password. Please check your credentials and try again.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void showProfileNotFound(String role) {
        statusLabel.setText("Error: " + role + " profile not found");
        JOptionPane.showMessageDialog(this,
                "Login successful but " + role.toLowerCase() + " profile not found. Please contact support.",
                "Profile Error", JOptionPane.WARNING_MESSAGE);
    }
}
