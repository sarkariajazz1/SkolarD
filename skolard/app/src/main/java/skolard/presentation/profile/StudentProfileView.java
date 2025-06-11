package skolard.presentation.profile;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import skolard.logic.faq.FAQHandler;
import skolard.logic.booking.BookingHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.payment.PaymentHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.support.SupportHandler;
import skolard.objects.Student;
import skolard.persistence.PersistenceRegistry;
import skolard.presentation.faq.FAQView;
import skolard.presentation.booking.BookingView;
import skolard.presentation.message.MessageView;
import skolard.presentation.rating.RatingView;
import skolard.presentation.session.SessionView;
import skolard.presentation.support.SupportView;

/**
 * GUI window for student profile dashboard in SkolarD.
 * Displays personalized welcome message and student-specific functionality.
 */
public class StudentProfileView extends JFrame {
    // The Student object representing the currently logged-in student.
    private final Student currentStudent;
    // Handler for student profile-related business logic.
    private final ProfileHandler profileHandler;
    // Handler for booking-related business logic.
    private final BookingHandler bookingHandler;
    // Handler for session-related business logic.
    private final SessionHandler sessionHandler;
    // Handler for message-related business logic.
    private final MessageHandler messageHandler;
    // Handler for FAQ-related business logic.
    private final FAQHandler faqHandler;
    // Handler for rating-related business logic.
    private final RatingHandler ratingHandler;
    // Handler for payment-related business logic.
    private final PaymentHandler paymentHandler;
    // A boolean indicating if this is the student's first login.
    private final boolean isFirstLogin;

    /**
     * Constructs a new StudentProfileView window.
     *
     * @param student The {@link Student} object for the logged-in student.
     * @param profileHandler The {@link ProfileHandler} instance for managing student profiles.
     * @param bookingHandler The {@link BookingHandler} instance for managing bookings.
     * @param sessionHandler The {@link SessionHandler} instance for managing sessions.
     * @param messageHandler The {@link MessageHandler} instance for managing messages.
     * @param faqHandler The {@link FAQHandler} instance for managing FAQs.
     * @param ratingHandler The {@link RatingHandler} instance for managing ratings.
     * @param isFirstLogin A boolean flag indicating if this is the user's first login.
     */
    public StudentProfileView(Student student, ProfileHandler profileHandler,
                              BookingHandler bookingHandler, SessionHandler sessionHandler,
                              MessageHandler messageHandler, FAQHandler faqHandler,
                              RatingHandler ratingHandler, boolean isFirstLogin) {
        super("SkolarD - Student Dashboard");
        this.currentStudent = student;
        this.profileHandler = profileHandler;
        this.bookingHandler = bookingHandler;
        this.sessionHandler = sessionHandler;
        this.messageHandler = messageHandler;
        this.faqHandler = faqHandler;
        this.ratingHandler = ratingHandler;
        // Initialize PaymentHandler using PersistenceRegistry for CardPersistence.
        this.paymentHandler = new PaymentHandler(PersistenceRegistry.getCardPersistence());
        this.isFirstLogin = isFirstLogin;

        // Initialize the graphical user interface components.
        initializeUI();
        // Set the default close operation for the frame.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the size of the window.
        setSize(600, 400);
        // Center the window on the screen.
        setLocationRelativeTo(null);
        // Make the window visible to the user.
        setVisible(true);
    }

    /**
     * Initializes and arranges all UI components within the dashboard frame.
     * This includes the welcome message and functional buttons specific to a student.
     *
     * @return void
     */
    private void initializeUI() {
        // Set the layout manager for the main frame.
        setLayout(new BorderLayout(10, 10));

        // Determine the welcome message based on whether it's the first login.
        String welcomeText = isFirstLogin ?
                "Welcome " + currentStudent.getName() + "!" :
                "Welcome back " + currentStudent.getName() + "!";

        // Create and configure the welcome label.
        JLabel welcomeLabel = new JLabel(welcomeText, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f));
        add(welcomeLabel, BorderLayout.NORTH);

        // Create a panel for the action buttons with a grid layout.
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // Initialize buttons for various student functionalities.
        JButton viewMyProfileBtn = new JButton("View My Profile");
        JButton findTutorsBtn = new JButton("Find Tutor Sessions");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");
        JButton rateBtn = new JButton("Rate Tutor/Session");
        JButton backBtn = new JButton("Back");

        // Add buttons to the button panel.
        buttonPanel.add(viewMyProfileBtn);
        buttonPanel.add(findTutorsBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);
        buttonPanel.add(rateBtn);
        buttonPanel.add(backBtn);

        // Add the button panel to the center of the frame.
        add(buttonPanel, BorderLayout.CENTER);

        // Setup event listeners for each button.
        viewMyProfileBtn.addActionListener(e -> {
            showStudentProfileView(); // Display the student's profile in a new window.
        });

        findTutorsBtn.addActionListener(e -> {
            // Open the BookingView window and dispose the current dashboard.
            new BookingView(bookingHandler, sessionHandler, ratingHandler, paymentHandler, currentStudent);
            dispose();
        });

        sessionBtn.addActionListener(e -> {
            // Open the SessionView window and dispose the current dashboard.
            new SessionView(sessionHandler, currentStudent);
            dispose();
        });

        messageBtn.addActionListener(e -> {
            // Open the MessageView window and dispose the current dashboard.
            new MessageView(messageHandler, currentStudent);
            dispose();
        });

        supportBtn.addActionListener(e -> {
            // Open the SupportView window (requires a new SupportHandler instance) and dispose the current dashboard.
            new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentStudent);
            dispose();
        });

        faqBtn.addActionListener(e -> {
            // Open the FAQView window and dispose the current dashboard.
            new FAQView(faqHandler);
            dispose();
        });

        rateBtn.addActionListener(e -> {
            // Open the RatingView window and dispose the current dashboard.
            new RatingView(ratingHandler, currentStudent);
            dispose();
        });

        backBtn.addActionListener(e -> dispose()); // Close the current dashboard window.
    }

    /**
     * Creates and displays a separate JFrame to show the current student's full profile details.
     * This view is read-only for the student.
     *
     * @return void
     */
    private void showStudentProfileView() {
        // Create a new JFrame for the profile display.
        JFrame profileFrame = new JFrame("My Profile - " + currentStudent.getName());
        profileFrame.setLayout(new BorderLayout(10, 10));

        // Create a non-editable JTextArea to display the profile information.
        JTextArea profileArea = new JTextArea(15, 40);
        profileArea.setEditable(false);
        // Get the full profile string from the profile handler and set it to the text area.
        profileArea.setText(profileHandler.viewFullProfile(currentStudent));

        // Add a scroll pane around the text area for content that might exceed visible bounds.
        JScrollPane scrollPane = new JScrollPane(profileArea);
        profileFrame.add(scrollPane, BorderLayout.CENTER);

        // Create a button panel for the 'Back' button.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backBtn = new JButton("Back");

        // Add action listener to the 'Back' button to close the profile frame.
        backBtn.addActionListener(e -> profileFrame.dispose());

        buttonPanel.add(backBtn);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Set default close operation, size, and position for the profile frame.
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(500, 400);
        profileFrame.setLocationRelativeTo(this); // Center relative to the dashboard.
        profileFrame.setVisible(true);
    }
}