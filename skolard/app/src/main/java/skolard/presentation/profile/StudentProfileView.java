
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
    private final Student currentStudent;
    private final ProfileHandler profileHandler;
    private final BookingHandler bookingHandler;
    private final SessionHandler sessionHandler;
    private final MessageHandler messageHandler;
    private final FAQHandler faqHandler;
    private final RatingHandler ratingHandler;
    private final PaymentHandler paymentHandler;
    private final boolean isFirstLogin;

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
        this.paymentHandler = new PaymentHandler(PersistenceRegistry.getCardPersistence());
        this.isFirstLogin = isFirstLogin;

        initializeUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        // Welcome message at the top
        String welcomeText = isFirstLogin ?
                "Welcome " + currentStudent.getName() + "!" :
                "Welcome back " + currentStudent.getName() + "!";

        JLabel welcomeLabel = new JLabel(welcomeText, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 18f));
        add(welcomeLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JButton viewMyProfileBtn = new JButton("View My Profile");
        JButton findTutorsBtn = new JButton("Find Tutor Sessions");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");
        JButton rateBtn = new JButton("Rate Tutor/Session");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(viewMyProfileBtn);
        buttonPanel.add(findTutorsBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);
        buttonPanel.add(rateBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // Setup event listeners
        viewMyProfileBtn.addActionListener(e -> {
            showStudentProfileView();
        });

        findTutorsBtn.addActionListener(e -> {
            new BookingView(bookingHandler, sessionHandler, ratingHandler, paymentHandler, currentStudent);
            dispose();
        });

        sessionBtn.addActionListener(e -> {
            new SessionView(sessionHandler, currentStudent);
            dispose();
        });

        messageBtn.addActionListener(e -> {
            new MessageView(messageHandler, currentStudent);
            dispose();
        });

        supportBtn.addActionListener(e -> {
            new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentStudent);
            dispose();
        });

        faqBtn.addActionListener(e -> {
            new FAQView(faqHandler);
            dispose();
        });

        rateBtn.addActionListener(e -> {
            new RatingView(ratingHandler, currentStudent);
            dispose();
        });

        backBtn.addActionListener(e -> dispose());
    }

    private void showStudentProfileView() {
        JFrame profileFrame = new JFrame("My Profile - " + currentStudent.getName());
        profileFrame.setLayout(new BorderLayout(10, 10));

        // Display current profile
        JTextArea profileArea = new JTextArea(15, 40);
        profileArea.setEditable(false);
        profileArea.setText(profileHandler.viewFullProfile(currentStudent));

        JScrollPane scrollPane = new JScrollPane(profileArea);
        profileFrame.add(scrollPane, BorderLayout.CENTER);

        // Button panel - only Back button for students
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backBtn = new JButton("Back");

        backBtn.addActionListener(e -> profileFrame.dispose());

        buttonPanel.add(backBtn);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(500, 400);
        profileFrame.setLocationRelativeTo(this);
        profileFrame.setVisible(true);
    }
}