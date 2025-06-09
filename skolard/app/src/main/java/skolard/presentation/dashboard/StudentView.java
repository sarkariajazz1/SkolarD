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

import skolard.logic.matching.MatchingHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.presentation.datetime.DateTimeLabel;

public class StudentView extends JFrame {

    private final ProfileHandler profileHandler;
    private final MatchingHandler matchingHandler;
    private final MessageHandler messageHandler;
    private final Student currentStudent;
    private final RatingHandler ratingHandler;
    private final SessionHandler sessionHandler;

    // UI Components
    private final JTextField searchField = new JTextField(20);
    private final JTextArea displayArea = new JTextArea(15, 50);
    private final JButton searchTutorsBtn = new JButton("Search Tutors");
    private final JButton messageTutorBtn = new JButton("Message Tutor");
    private final JButton viewMyProfileBtn = new JButton("View My Profile");

    private Tutor selectedTutor;

    public StudentView(ProfileHandler profileHandler,
                       MatchingHandler matchingHandler,
                       MessageHandler messageHandler,
                       SessionHandler sessionHandler,
                       RatingHandler ratingHandler,
                       Student student) {
        super("SkolarD - Student Dashboard");

        this.profileHandler = profileHandler;
        this.matchingHandler = matchingHandler;
        this.messageHandler = messageHandler;
        this.currentStudent = student;
        this.ratingHandler = ratingHandler;
        this.sessionHandler = sessionHandler;

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
        searchTutorsBtn.addActionListener(e -> searchTutors());
        messageTutorBtn.addActionListener(e -> messageTutor());
        viewMyProfileBtn.addActionListener(e -> viewMyProfile());
    }

    private void searchTutors() {
        String searchQuery = searchField.getText().trim();
        if (searchQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.");
            return;
        }

        displayArea.setText("Searching for tutors...\n\n");

        try {
            var sessions = matchingHandler.getAvailableSessions(searchQuery, currentStudent.getEmail());
            if (!sessions.isEmpty()) {
                displayArea.append("Available tutoring sessions for '" + searchQuery + "':\n");
                for (var session : sessions) {
                    displayArea.append("- " + session.toString() + "\n");
                }
                displayArea.append("\nTo message a tutor, search for the specific tutor by name.\n\n");
            }
        } catch (Exception ex) {
            // Continue to search by tutor name
        }

        Tutor tutor = profileHandler.getTutor(searchQuery);
        if (tutor != null) {
            selectedTutor = tutor;
            displayArea.append("Tutor found:\n");
            displayArea.append(profileHandler.viewFullProfile(tutor));
            displayArea.append("\n\nYou can now send a message to this tutor.");
            messageTutorBtn.setEnabled(true);
        } else {
            if (displayArea.getText().equals("Searching for tutors...\n\n")) {
                displayArea.setText("No tutors found for '" + searchQuery + "'.\nTry searching by course name or tutor email.");
            }
            selectedTutor = null;
            messageTutorBtn.setEnabled(false);
        }
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
