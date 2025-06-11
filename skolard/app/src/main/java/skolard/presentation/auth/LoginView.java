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
import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.support.SupportHandler;
import skolard.objects.LoginCredentials;
import skolard.objects.Support;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;
import skolard.presentation.SkolardApp;
import skolard.presentation.dashboard.SupportDashboard;
import skolard.presentation.faq.FAQView;

public class LoginView extends JFrame {

    // Input fields for email and password
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    
    // Buttons for different login roles and navigation
    private final JButton loginStudentBtn = new JButton("Login as Student");
    private final JButton loginTutorBtn = new JButton("Login as Tutor");
    private final JButton loginSupportBtn = new JButton("Login as Support");
    private final JButton signUpBtn = new JButton("Go to Sign Up");
    private final JButton faqBtn = new JButton("FAQs");
    
    // Status label to show messages to the user
    private final JLabel statusLabel = new JLabel("Enter your credentials to login");

    // Handlers and parent application reference
    private final ProfileHandler profileHandler;
    private final SkolardApp parentApp;
    private final LoginHandler loginHandler;

    // Constructor initializes UI and event handlers
    public LoginView(ProfileHandler profileHandler, LoginHandler loginHandler, SkolardApp parentApp) {
        super("SkolarD - Login");  // Window title

        this.profileHandler = profileHandler;
        this.loginHandler = loginHandler;
        this.parentApp = parentApp;

        setLayout(new BorderLayout(10, 10)); // Main layout with spacing

        // Panel for input form using GridBagLayout for flexible layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components

        // Email label and field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Password label and field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER); // Add form panel to center

        // Panel for login buttons and other navigation buttons in a grid
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        buttonPanel.add(loginStudentBtn);
        buttonPanel.add(loginTutorBtn);
        buttonPanel.add(loginSupportBtn);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(faqBtn);
        add(buttonPanel, BorderLayout.SOUTH); // Add button panel to bottom

        // Center status label at the top
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.NORTH);

        // --- Event handler for Login as Student button ---
        loginStudentBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate input fields are not empty
            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password");
                return;
            }

            // Create credentials object for student role
            LoginCredentials creds = new LoginCredentials(email, password, "student");
            
            // Attempt login using LoginHandler
            if (loginHandler.login(creds)) {
                User student = profileHandler.getStudent(email);
                if (student != null) {
                    // Success message and popup
                    statusLabel.setText("Login successful! Welcome " + student.getName());
                    JOptionPane.showMessageDialog(this, "Login successful as student: " + student.getName(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Notify parent app of successful authentication
                    parentApp.onAuthenticationSuccess(student, false);
                    dispose(); // Close login window
                } else {
                    // Profile not found after successful login
                    showProfileNotFound("Student");
                }
            } else {
                // Login failed
                showLoginFailed("student");
            }
        });

        // --- Event handler for Login as Tutor button ---
        loginTutorBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate input
            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password");
                return;
            }

            // Credentials for tutor role
            LoginCredentials creds = new LoginCredentials(email, password, "tutor");

            if (loginHandler.login(creds)) {
                User tutor = profileHandler.getTutor(email);
                if (tutor != null) {
                    statusLabel.setText("Login successful! Welcome " + tutor.getName());
                    JOptionPane.showMessageDialog(this, "Login successful as tutor: " + tutor.getName(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    parentApp.onAuthenticationSuccess(tutor, false);
                    dispose();
                } else {
                    showProfileNotFound("Tutor");
                }
            } else {
                showLoginFailed("tutor");
            }
        });

        // --- Event handler for Login as Support button ---
        loginSupportBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate input
            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password");
                return;
            }

            // Credentials for support role
            LoginCredentials creds = new LoginCredentials(email, password, "support");
            if (loginHandler.login(creds)) {
                // Create a Support user object
                Support supportUser = new Support("Support", email);
                // Initialize handlers needed for support dashboard
                SupportHandler supportHandler = new SupportHandler(PersistenceRegistry.getSupportPersistence());
                MessageHandler messageHandler = new MessageHandler(PersistenceRegistry.getMessagePersistence());

                statusLabel.setText("Login successful! Welcome " + supportUser.getName());
                JOptionPane.showMessageDialog(this, "Login successful as support: " + supportUser.getName(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Open the Support Dashboard UI
                new SupportDashboard(supportUser, supportHandler, messageHandler, false);
                dispose(); // Close login window
            } else {
                showLoginFailed("support");
            }
        });

        // --- Event handler to open Sign Up view ---
        signUpBtn.addActionListener(e -> {
            new SignUpView(profileHandler, loginHandler, parentApp);
            dispose(); // Close login window
        });

        // --- Event handler to open FAQ view ---
        faqBtn.addActionListener(e -> new FAQView(new FAQHandler(PersistenceRegistry.getFAQPersistence())));

        // Set default close behavior, pack components, center on parent, show window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parentApp);
        setVisible(true);
    }

    /**
     * Shows an error message and updates status label when login fails.
     * @param role The role (student, tutor, support) for which login failed.
     */
    private void showLoginFailed(String role) {
        statusLabel.setText("Invalid " + role + " credentials");
        JOptionPane.showMessageDialog(this,
                "Invalid email or password. Please check your credentials and try again.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a warning message when login is successful but user profile was not found.
     * @param role The role (Student or Tutor) for which the profile was not found.
     */
    private void showProfileNotFound(String role) {
        statusLabel.setText("Error: " + role + " profile not found");
        JOptionPane.showMessageDialog(this,
                "Login successful but " + role.toLowerCase() + " profile not found. Please contact support.",
                "Profile Error", JOptionPane.WARNING_MESSAGE);
    }
}
