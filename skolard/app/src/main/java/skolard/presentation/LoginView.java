package skolard.presentation;

import javax.swing.*;
import java.awt.*;
import skolard.logic.ProfileHandler;
import skolard.logic.FAQHandler;
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

    public LoginView(ProfileHandler profileHandler, SkolardApp parentApp) {
        super("SkolarD - Login");

        this.handler = profileHandler;
        this.parentApp = parentApp;

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

            User student = handler.getStudent(email);
            if (student != null) {
                statusLabel.setText("Login successful! Welcome " + student.getName());
                JOptionPane.showMessageDialog(this,
                        "Login successful as student: " + student.getName(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Notify parent app of successful authentication
                parentApp.onAuthenticationSuccess();
                dispose(); // Close login window
            } else {
                statusLabel.setText("No student found with that email");
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

            User tutor = handler.getTutor(email);
            if (tutor != null) {
                statusLabel.setText("Login successful! Welcome " + tutor.getName());
                JOptionPane.showMessageDialog(this,
                        "Login successful as tutor: " + tutor.getName(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Notify parent app of successful authentication
                parentApp.onAuthenticationSuccess();
                dispose(); // Close login window
            } else {
                statusLabel.setText("No tutor found with that email");
            }
        });

        // Switch to sign up
        signUpBtn.addActionListener(e -> {
            new SignUpView(handler, parentApp);
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