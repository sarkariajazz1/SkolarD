package skolard.presentation.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

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
import skolard.objects.Session;
import skolard.objects.Student; // at the top
import skolard.objects.Tutor;
import skolard.presentation.datetime.DateTimeLabel;



/**
 * GUI window for student-specific functionality in SkolarD.
 * Allows students to search for tutors, book sessions, and send messages.
 */
public class StudentView extends JFrame {
    
    private final ProfileHandler profileHandler;
    private final MatchingHandler matchingHandler;
    private final MessageHandler messageHandler;
    private final Student currentStudent;
    private final RatingHandler ratingHandler; // add this as a field
    private final SessionHandler sessionHandler; // Assuming you have a SessionHandler class
    
    // UI Components
    private final JTextField searchField = new JTextField(20);
    private final JTextArea displayArea = new JTextArea(15, 50);
    private final JButton searchTutorsBtn = new JButton("Search Tutors");
    private final JButton bookTutorBtn = new JButton("Book Tutor");
    private final JButton messageTutorBtn = new JButton("Message Tutor");
    private final JButton viewMyProfileBtn = new JButton("View My Profile");
    
    private Tutor selectedTutor; // Currently selected tutor
    
public StudentView(ProfileHandler profileHandler,
                   MatchingHandler matchingHandler,
                   MessageHandler messageHandler,
                   SessionHandler sessionHandler,
                   RatingHandler ratingHandler,
                   Student student)
 {
        super("SkolarD - Student Dashboard");
        
        this.profileHandler = profileHandler;
        this.matchingHandler = matchingHandler;
        this.messageHandler = messageHandler;
        this.currentStudent = student;
        this.ratingHandler = ratingHandler; // initialize the rating handler
        this.sessionHandler = sessionHandler; // initialize the session handler

        
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
        DateTimeLabel clockLabel = new DateTimeLabel(); // shows date + time
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
        
        // Center panel combining search and display
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        // Action buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        buttonPanel.add(bookTutorBtn);
        buttonPanel.add(messageTutorBtn);
        buttonPanel.add(viewMyProfileBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initially disable action buttons until a tutor is selected
        bookTutorBtn.setEnabled(false);
        messageTutorBtn.setEnabled(false);
    }
    
    private void setupEventListeners() {
        searchTutorsBtn.addActionListener(e -> searchTutors());
        bookTutorBtn.addActionListener(e -> bookTutor());
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
        
        // First try to find tutors by course using MatchingHandler
        try {
            var sessions = matchingHandler.getAvailableSessions(searchQuery);
            if (!sessions.isEmpty()) {
                displayArea.append("Available tutoring sessions for '" + searchQuery + "':\n");
                for (var session : sessions) {
                    displayArea.append("- " + session.toString() + "\n");
                }
                displayArea.append("\nTo book a session or message a tutor, search for the specific tutor by name.\n\n");
            }
        } catch (Exception ex) {
            // Continue to search by tutor name
        }
        
        // Search for specific tutor by email/name
        Tutor tutor = profileHandler.getTutor(searchQuery);
        if (tutor != null) {
            selectedTutor = tutor;
            displayArea.append("Tutor found:\n");
            displayArea.append(profileHandler.viewFullProfile(tutor));
            displayArea.append("\n\nYou can now book this tutor or send a message.");
            
            // Enable action buttons
            bookTutorBtn.setEnabled(true);
            messageTutorBtn.setEnabled(true);
        } else {
            if (displayArea.getText().equals("Searching for tutors...\n\n")) {
                displayArea.setText("No tutors found for '" + searchQuery + "'.\n");
                displayArea.append("Try searching by course name or tutor email.");
            }
            selectedTutor = null;
            bookTutorBtn.setEnabled(false);
            messageTutorBtn.setEnabled(false);
        }
    }
    
    private void bookTutor() {
        if (selectedTutor == null) {
            JOptionPane.showMessageDialog(this, "Please select a tutor first.");
            return;
        }

        String course = JOptionPane.showInputDialog(this, 
            "Enter the course you want tutoring for:");

        if (course != null && !course.trim().isEmpty()) {
            try {
                // Find first unbooked session by this tutor for the course
                List <Session> sessions = matchingHandler.getAvailableSessions(course.trim());
                Session session = sessions.stream()
                    .filter(s -> s.getTutor().equals(selectedTutor) && !s.isBooked())
                    .findFirst()
                    .orElse(null);

                if (session != null) {
                    int sessionId = session.getSessionId();

                    // Call the proper booking method from SessionHandler
                    sessionHandler.bookASession(currentStudent, sessionId);

                    // Add to rating queue
                    ratingHandler.createRatingRequest((Session)session, currentStudent);

                    JOptionPane.showMessageDialog(this,
                        "Session booked successfully! You can rate it after it ends.");
                    displayArea.append("\n\nBooked session ID " + sessionId +
                        " for " + course.trim() + " with " + selectedTutor.getName());
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No available sessions for this tutor and course.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error booking session: " + ex.getMessage());
            }
        }
    }


    
    private void messageTutor() {
        if (selectedTutor == null) {
            JOptionPane.showMessageDialog(this, "Please select a tutor first.");
            return;
        }
        
        // Open a message dialog or redirect to messaging view
        String message = JOptionPane.showInputDialog(this, 
            "Enter your message to " + selectedTutor.getName() + ":");
        
        if (message != null && !message.trim().isEmpty()) {
            // Here you would typically send the message using MessageHandler
            JOptionPane.showMessageDialog(this, 
                "Message sent to " + selectedTutor.getName() + "!");
            
            displayArea.append("\n\nMessage sent to " + selectedTutor.getName());
        }
    }
    
    private void viewMyProfile() {
        displayArea.setText("My Profile:\n\n");
        displayArea.append(profileHandler.viewFullProfile(currentStudent));
        
        // Disable tutor-specific buttons when viewing own profile
        selectedTutor = null;
        bookTutorBtn.setEnabled(false);
        messageTutorBtn.setEnabled(false);
    }
}