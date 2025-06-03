package skolard.presentation;

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
import skolard.objects.User;

/**
 * GUI window for user login in SkolarD.
 * Allows users to authenticate as either a student or tutor.
 */
public class LoginView extends JFrame {

    // UI Components
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginStudentBtn = new JButton("Login as Student");
    private final JButton loginTutorBtn = new JButton("Login as Tutor");
    private final JButton signUpBtn = new JButton("Go to Sign Up");
    private final JButton faqBtn = new JButton("FAQs");
    private final JLabel statusLabel = new JLabel("Enter your credentials to login");

    private ProfileHandler handler;
    private SkolardApp parentApp;
    private LoginHandler loginHandler;

    public LoginView(ProfileHandler profileHandler, LoginHandler loginHandler, SkolardApp parentApp) {
        super("SkolarD - Login");

        this.handler = profileHandler;
        this.parentApp = parentApp;
        this.loginHandler = loginHandler;

        setLayout(new BorderLayout(10, 10));

        // Center panel for login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Email field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.add(loginStudentBtn);
        buttonPanel.add(loginTutorBtn);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(faqBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Status label at top
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // Login as student
        loginStudentBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password");
                return;
            }

            LoginCredentials creds = new LoginCredentials(email, password, "student");

            if (loginHandler.login(creds)) {
                User student = handler.getStudent(email);
                if (student != null) {
                    statusLabel.setText("Login successful! Welcome " + student.getName());
                    JOptionPane.showMessageDialog(this,
                            "Login successful as student: " + student.getName(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Pass the user object to authentication success
                    parentApp.onAuthenticationSuccess(student);
                    dispose();
                } else {
                    statusLabel.setText("Error: Student profile not found");
                    JOptionPane.showMessageDialog(this,
                            "Login successful but student profile not found. Please contact support.",
                            "Profile Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                statusLabel.setText("Invalid student credentials");
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password. Please check your credentials and try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Login as tutor
        loginTutorBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password");
                return;
            }

            LoginCredentials creds = new LoginCredentials(email, password, "tutor");

            if (loginHandler.login(creds)) {
                User tutor = handler.getTutor(email);
                if (tutor != null) {
                    statusLabel.setText("Login successful! Welcome " + tutor.getName());
                    JOptionPane.showMessageDialog(this,
                            "Login successful as tutor: " + tutor.getName(),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Pass the user object to authentication success
                    parentApp.onAuthenticationSuccess(tutor);
                    dispose();
                } else {
                    statusLabel.setText("Error: Tutor profile not found");
                    JOptionPane.showMessageDialog(this,
                            "Login successful but tutor profile not found. Please contact support.",
                            "Profile Error",
                            JOptionPane.WARNING_MESSAGE);
                }
            } else {
                statusLabel.setText("Invalid tutor credentials");
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password. Please check your credentials and try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Switch to sign up
        signUpBtn.addActionListener(e -> {
            // Pass the LoginHandler to SignUpView
            new SignUpView(handler, loginHandler, parentApp);
            dispose(); // Close login window
        });

        // Open FAQ
        faqBtn.addActionListener(e -> new FAQView(new FAQHandler()));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parentApp);
        setVisible(true);
    }
}