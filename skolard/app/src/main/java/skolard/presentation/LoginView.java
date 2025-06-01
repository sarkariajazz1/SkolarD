package skolard.presentation;

import skolard.objects.LoginCredentials;

import javax.swing.*;
import java.awt.*;

/**
 * GUI window for logging into the SkolarD platform.
 */
public class LoginView extends JFrame {

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JComboBox<String> roleCombo = new JComboBox<>(new String[]{"student", "tutor"});

    private LoginCredentials credentials;

    public LoginView() {
        super("SkolarD Login");

        setLayout(new BorderLayout(10, 10));

        // Top panel: email and password
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Email:"));
        topPanel.add(emailField);
        topPanel.add(new JLabel("Password:"));
        topPanel.add(passwordField);
        add(topPanel, BorderLayout.NORTH);

        // Center panel: role selection
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(new JLabel("Role:"));
        centerPanel.add(roleCombo);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel: login button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        bottomPanel.add(loginButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = ((String) roleCombo.getSelectedItem()).toLowerCase();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter email and password.");
                return;
            }

            credentials = new LoginCredentials(email, password, role);
            dispose(); // Close the login window
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    public LoginCredentials getCredentials() {
        return credentials;
    }
}
