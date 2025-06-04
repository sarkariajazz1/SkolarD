
package skolard.presentation.profile;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import skolard.logic.profile.ProfileHandler;
import skolard.objects.Student;

/**
 * Specialized profile management view for students.
 * Allows students to view their basic profile information (name and email).
 */
public class StudentProfileView extends JFrame {

    private final ProfileHandler profileHandler;
    private final Student currentStudent;

    // UI Components
    private final JTextArea profileInfoArea = new JTextArea(12, 50);
    private final JButton backBtn = new JButton("Back");
    private final JLabel statusLabel = new JLabel("Profile loaded successfully", SwingConstants.CENTER);

    public StudentProfileView(ProfileHandler profileHandler, Student student) {
        super("SkolarD - My Profile (Student)");

        this.profileHandler = profileHandler;
        this.currentStudent = student;

        initializeUI();
        setupEventHandlers();
        loadProfileData();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("My Profile - " + currentStudent.getName(), SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Profile info panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(BorderFactory.createTitledBorder("Profile Information"));

        profileInfoArea.setEditable(false);
        profileInfoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        profileInfoArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        profileInfoArea.setBackground(getBackground());

        profilePanel.add(new JScrollPane(profileInfoArea), BorderLayout.CENTER);
        mainPanel.add(profilePanel, BorderLayout.CENTER);

        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        actionPanel.add(backBtn);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // Status label at bottom
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        backBtn.addActionListener(e -> dispose());
    }

    private void loadProfileData() {
        try {
            // Load student profile data once when window opens
            profileInfoArea.setText(profileHandler.viewFullProfile(currentStudent));
            statusLabel.setText("Profile loaded successfully");
        } catch (Exception ex) {
            showError("Error loading profile: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        statusLabel.setText("Error occurred");
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}