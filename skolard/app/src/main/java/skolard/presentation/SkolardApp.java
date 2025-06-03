
package skolard.presentation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import skolard.logic.auth.LoginHandler;
import skolard.logic.faq.FAQHandler;
import skolard.logic.matching.MatchingHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.session.SessionHandler;
import skolard.objects.Student;
import skolard.objects.Tutor;
import skolard.objects.User;

/**
 * The main application window of SkolarD that handles authentication and navigation.
 */
public class SkolardApp extends JFrame {

    final private ProfileHandler profileHandler;
    final private MatchingHandler matchingHandler;
    final private SessionHandler sessionHandler;
    //final private SupportHandler supportHandler;
    final private MessageHandler messageHandler;
    final private FAQHandler faqHandler;
    final private LoginHandler loginHandler;

    // Current logged-in user
    private User currentUser;

    // UI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel dashboardPanel; // Store reference to recreate it

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler,
                      SessionHandler sessionHandler,
                      MessageHandler messageHandler, FAQHandler faqHandler, LoginHandler loginHandler) {
        super("SkolarD - Welcome");

        this.profileHandler = profileHandler;
        this.matchingHandler = matchingHandler;
        this.sessionHandler = sessionHandler;
        //this.supportHandler = supportHandler;
        this.messageHandler = messageHandler;
        this.faqHandler = faqHandler;
        this.loginHandler = loginHandler;

        initializeUI();
        showAuthenticationView();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create authentication panel (login/signup options)
        JPanel authPanel = createAuthenticationPanel();

        // Create initial dashboard panel (will be updated based on user type)
        dashboardPanel = createDashboardPanel();

        mainPanel.add(authPanel, "AUTH");
        mainPanel.add(dashboardPanel, "DASHBOARD");

        add(mainPanel);
    }

    private JPanel createAuthenticationPanel() {
        JPanel authPanel = new JPanel(new BorderLayout(10, 10));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to SkolarD", SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16f));
        authPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Authentication buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(loginBtn);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(faqBtn);
        authPanel.add(buttonPanel, BorderLayout.CENTER);

        // Instructions
        JLabel instructionLabel = new JLabel("Please login or sign up to access all features", SwingConstants.CENTER);
        authPanel.add(instructionLabel, BorderLayout.SOUTH);

        // Event listeners
        loginBtn.addActionListener(e -> new LoginView(profileHandler, loginHandler, this));
        signUpBtn.addActionListener(e -> new SignUpView(profileHandler, loginHandler, this));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return authPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));

        // Dashboard title with user info
        String titleText = "SkolarD Dashboard";
        if (currentUser != null) {
            String userType = currentUser instanceof Student ? "Student" : "Tutor";
            titleText += " - " + userType + ": " + currentUser.getName();
        }
        JLabel dashboardLabel = new JLabel(titleText, SwingConstants.CENTER);
        dashboardLabel.setFont(dashboardLabel.getFont().deriveFont(Font.BOLD, 16f));
        dashboardPanel.add(dashboardLabel, BorderLayout.NORTH);

        // Create buttons based on user type
        JPanel buttonPanel = createButtonPanelForUser();
        dashboardPanel.add(buttonPanel, BorderLayout.CENTER);

        // Logout button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton logoutBtn = new JButton("Logout");
        bottomPanel.add(logoutBtn);
        dashboardPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Logout event listener
        logoutBtn.addActionListener(e -> logout());

        return dashboardPanel;
    }

    private JPanel createButtonPanelForUser() {
        if (currentUser == null) {
            // Generic panel for unauthenticated users
            JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            JButton profileBtn = new JButton("Profile Management");
            JButton sessionBtn = new JButton("Session Management");
            JButton messageBtn = new JButton("Messages");
            JButton faqBtn = new JButton("FAQs");

            buttonPanel.add(profileBtn);
            buttonPanel.add(sessionBtn);
            buttonPanel.add(messageBtn);
            buttonPanel.add(faqBtn);

            // Generic event listeners (with authentication checks)
            profileBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Please login first to access profile management",
                        "Authentication Required", JOptionPane.WARNING_MESSAGE);
            });
            sessionBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Please login first to access session management",
                        "Authentication Required", JOptionPane.WARNING_MESSAGE);
            });
            messageBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Please login first to access messages",
                        "Authentication Required", JOptionPane.WARNING_MESSAGE);
            });
            faqBtn.addActionListener(e -> new FAQView(faqHandler));

            return buttonPanel;
        }

        if (currentUser instanceof Student) {
            return createStudentButtonPanel();
        } else if (currentUser instanceof Tutor) {
            return createTutorButtonPanel();
        } else {
            // Fallback for unknown user types
            return createGenericButtonPanel();
        }
    }

    private JPanel createStudentButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JButton myDashboardBtn = new JButton("My Dashboard");
        JButton findTutorsBtn = new JButton("Find Tutors");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(myDashboardBtn);
        buttonPanel.add(findTutorsBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);

        // Student-specific event listeners
        myDashboardBtn.addActionListener(e -> new StudentView(profileHandler, matchingHandler, messageHandler, (Student) currentUser));
        findTutorsBtn.addActionListener(e -> new StudentView(profileHandler, matchingHandler, messageHandler, (Student) currentUser));
        sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
        messageBtn.addActionListener(e -> new MessageView(messageHandler));
        //supportBtn.addActionListener(e -> new SupportView(supportHandler));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return buttonPanel;
    }

    private JPanel createTutorButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JButton myDashboardBtn = new JButton("My Dashboard");
        JButton studentsBtn = new JButton("My Students");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(myDashboardBtn);
        buttonPanel.add(studentsBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);

        // Tutor-specific event listeners
        myDashboardBtn.addActionListener(e -> new TutorView(profileHandler, sessionHandler, messageHandler, (Tutor) currentUser));
        studentsBtn.addActionListener(e -> new TutorView(profileHandler, sessionHandler, messageHandler, (Tutor) currentUser));
        sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
        messageBtn.addActionListener(e -> new MessageView(messageHandler));
        //supportBtn.addActionListener(e -> new SupportView(supportHandler));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return buttonPanel;
    }

    private JPanel createGenericButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton profileBtn = new JButton("Profile Management");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(profileBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(faqBtn);

        // Generic event listeners
        profileBtn.addActionListener(e -> new ProfileView(profileHandler));
        sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
        messageBtn.addActionListener(e -> new MessageView(messageHandler));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return buttonPanel;
    }

    /**
     * Opens the appropriate dashboard based on the user type
     */
    private void openUserSpecificDashboard() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please login first to access your dashboard",
                    "Authentication Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentUser instanceof Student) {
            new StudentView(profileHandler, matchingHandler, messageHandler, (Student) currentUser);
        } else if (currentUser instanceof Tutor) {
            new TutorView(profileHandler, sessionHandler, messageHandler, (Tutor) currentUser);
        } else {
            // Fallback to the old ProfileView for any other user types
            new ProfileView(profileHandler);
        }
    }

    /**
     * Called when user successfully authenticates
     */
    public void onAuthenticationSuccess() {
        setTitle("SkolarD - Dashboard");
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    /**
     * Called when user successfully authenticates with user information
     */
    public void onAuthenticationSuccess(User user) {
        this.currentUser = user;
        setTitle("SkolarD - Dashboard (" + user.getName() + ")");

        // Recreate the dashboard panel with user-specific buttons
        mainPanel.remove(dashboardPanel);
        dashboardPanel = createDashboardPanel();
        mainPanel.add(dashboardPanel, "DASHBOARD");

        cardLayout.show(mainPanel, "DASHBOARD");
    }

    /**
     * Shows the authentication view (login/signup)
     */
    public void showAuthenticationView() {
        setTitle("SkolarD - Welcome");
        this.currentUser = null; // Clear current user on logout

        // Recreate the dashboard panel for unauthenticated state
        mainPanel.remove(dashboardPanel);
        dashboardPanel = createDashboardPanel();
        mainPanel.add(dashboardPanel, "DASHBOARD");

        cardLayout.show(mainPanel, "AUTH");
    }

    /**
     * Handles user logout
     */
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            showAuthenticationView();
        }
    }

    /**
     * Gets the currently logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
}