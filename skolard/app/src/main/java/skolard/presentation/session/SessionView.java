package skolard.presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import skolard.logic.session.SessionHandler;
import skolard.objects.User;
import skolard.objects.Tutor;
import skolard.objects.Student;

/**
 * GUI window for session management in SkolarD.
 * Allows tutors to create sessions and students to book sessions.
 */
public class SessionView extends JFrame {
    private SessionHandler sessionHandler;
    private User currentUser;
    
    // UI Components for session creation (Tutor)
    private final JTextField courseNameField = new JTextField(20);
    private final JTextField startTimeField = new JTextField(20);
    private final JTextField endTimeField = new JTextField(20);
    private final JButton createSessionBtn = new JButton("Create Session");
    
    // UI Components for session booking (Student)
    private final JTextField sessionIdField = new JTextField(10);
    private final JButton bookSessionBtn = new JButton("Book Session");
    
    private final JLabel statusLabel = new JLabel("Session Management");
    private final JTextArea instructionsArea = new JTextArea(3, 40);
    
    public SessionView(SessionHandler sessionHandler, User currentUser) {
        super("SkolarD - Session Management");
        this.sessionHandler = sessionHandler;
        this.currentUser = currentUser;
        
        initializeUI();
        setupEventHandlers();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Status label at top
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 16f));
        add(statusLabel, BorderLayout.NORTH);
        
        // Instructions area
        instructionsArea.setEditable(false);
        instructionsArea.setBackground(getBackground());
        instructionsArea.setText("Date/Time format: yyyy-MM-dd HH:mm (e.g., 2024-12-25 14:30)\n" +
                                "Tutors: Create sessions by filling course name and times\n" +
                                "Students: Book sessions by entering session ID");
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.setLineWrap(true);
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Instructions
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(new JScrollPane(instructionsArea), gbc);
        gbc.gridwidth = 1;
        
        // Session creation section (for Tutors)
        if (currentUser instanceof Tutor) {
            gbc.gridx = 0; gbc.gridy = 1;
            mainPanel.add(new JLabel("Course Name:"), gbc);
            gbc.gridx = 1;
            mainPanel.add(courseNameField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            mainPanel.add(new JLabel("Start Time:"), gbc);
            gbc.gridx = 1;
            mainPanel.add(startTimeField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            mainPanel.add(new JLabel("End Time:"), gbc);
            gbc.gridx = 1;
            mainPanel.add(endTimeField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(createSessionBtn, gbc);
        }
        
        // Session booking section (for Students)
        if (currentUser instanceof Student) {
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            mainPanel.add(new JLabel("Session ID:"), gbc);
            gbc.gridx = 1;
            mainPanel.add(sessionIdField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            mainPanel.add(bookSessionBtn, gbc);
        }
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // Create session handler (for Tutors)
        createSessionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSession();
            }
        });
        
        // Book session handler (for Students)
        bookSessionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookSession();
            }
        });
    }
    
    private void createSession() {
        try {
            String courseName = courseNameField.getText().trim();
            String startTimeStr = startTimeField.getText().trim();
            String endTimeStr = endTimeField.getText().trim();
            
            if (courseName.isEmpty() || startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
            
            if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
                showError("End time must be after start time");
                return;
            }
            
            sessionHandler.createSession(currentUser, startTime, endTime, courseName);
            
            showSuccess("Session created successfully!");
            clearCreateSessionFields();
            
        } catch (DateTimeParseException e) {
            showError("Invalid date/time format. Use: yyyy-MM-dd HH:mm");
        } catch (IllegalArgumentException e) {
            showError("Error creating session: " + e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }
    
    private void bookSession() {
        try {
            String sessionIdStr = sessionIdField.getText().trim();
            
            if (sessionIdStr.isEmpty()) {
                showError("Please enter a session ID");
                return;
            }
            
            int sessionId = Integer.parseInt(sessionIdStr);
            sessionHandler.bookASession(currentUser, sessionId);
            
            showSuccess("Session booked successfully!");
            sessionIdField.setText("");
            
        } catch (NumberFormatException e) {
            showError("Session ID must be a valid number");
        } catch (IllegalArgumentException e) {
            showError("Error booking session: " + e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }
    
    private void clearCreateSessionFields() {
        courseNameField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}