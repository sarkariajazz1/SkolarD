
package skolard.presentation;

import javax.swing.*;
import java.awt.*;
import skolard.logic.ProfileHandler;
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
    private final JLabel statusLabel = new JLabel("Enter your credentials to login");
    
    private ProfileHandler handler;
    
    public LoginView(ProfileHandler profileHandler) {
        super("SkolarD - Login");
        
        this.handler = profileHandler;
        
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
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginStudentBtn);
        buttonPanel.add(loginTutorBtn);
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
                // In a real application, you would validate the password here
                JOptionPane.showMessageDialog(this, 
                    "Login successful as student: " + student.getName(),
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
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
                // In a real application, you would validate the password here
                JOptionPane.showMessageDialog(this, 
                    "Login successful as tutor: " + tutor.getName(),
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                statusLabel.setText("No tutor found with that email");
            }
        });
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}