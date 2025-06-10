package skolard.presentation.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.presentation.datetime.DateTimeLabel;

public class StudentView extends JFrame {

    private final ProfileHandler profileHandler;
    private final MessageHandler messageHandler;
    private final Student currentStudent;

    // UI Components
    private final JTextField searchField = new JTextField(20);
    private final JTextArea displayArea = new JTextArea(15, 50);
    private final JButton searchTutorsBtn = new JButton("Search Tutors");
    private final JButton messageTutorBtn = new JButton("Message Tutor");
    private final JButton viewMyProfileBtn = new JButton("View My Profile");

    private Tutor selectedTutor;

    public StudentView(ProfileHandler profileHandler,
                       MessageHandler messageHandler,
                       Student student) {
        super("SkolarD - Student Dashboard");

        this.profileHandler = profileHandler;
        this.messageHandler = messageHandler;
        this.currentStudent = student;

        initializeUI();
        setupEventListeners();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Welcome panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel welcomeLabel = new JLabel("Welcome, " + currentStudent.getName() + "!");
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(java.awt.Font.BOLD, 16f));
        DateTimeLabel clockLabel = new DateTimeLabel();
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(clockLabel);
        add(welcomePanel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search by course or tutor name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchTutorsBtn);

        // Display area
        displayArea.setEditable(false);
        displayArea.setText("Enter a course name or tutor name to search for available tutors.");

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonPanel.add(messageTutorBtn);
        buttonPanel.add(viewMyProfileBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initially disable tutor-specific buttons
        messageTutorBtn.setEnabled(false);
    }

    private void setupEventListeners() {
        //searchTutorsBtn.addActionListener(e -> searchTutors());
        messageTutorBtn.addActionListener(e -> messageTutor());
        viewMyProfileBtn.addActionListener(e -> viewMyProfile());
    }


    private void messageTutor() {
        if (messageHandler == null) {
            JOptionPane.showMessageDialog(this, "Message handler is not initialized.");
            return;
        }
        if (selectedTutor == null) {
            JOptionPane.showMessageDialog(this, "Please select a tutor first.");
            return;
        }

        String message = JOptionPane.showInputDialog(this,
            "Enter your message to " + selectedTutor.getName() + ":");

        if (message != null && !message.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Message sent to " + selectedTutor.getName() + "!");
            displayArea.append("\n\nMessage sent to " + selectedTutor.getName());
        }
    }

    private void viewMyProfile() {
        displayArea.setText("My Profile:\n\n");
        displayArea.append(profileHandler.viewFullProfile(currentStudent));
        selectedTutor = null;
        messageTutorBtn.setEnabled(false);
    }
}
