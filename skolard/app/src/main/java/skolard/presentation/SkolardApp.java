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

import skolard.logic.FAQHandler;
import skolard.logic.LoginHandler;
import skolard.logic.MatchingHandler;
import skolard.logic.ProfileHandler;
import skolard.logic.MessageHandler;
import skolard.logic.SupportHandler;
import skolard.logic.SessionHandler;
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

        // Create main dashboard panel (available after authentication)
        JPanel dashboardPanel = createDashboardPanel();

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
        signUpBtn.addActionListener(e -> new SignUpView(profileHandler, this));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return authPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));

        // Dashboard title
        JLabel dashboardLabel = new JLabel("SkolarD Dashboard", SwingConstants.CENTER);
        dashboardLabel.setFont(dashboardLabel.getFont().deriveFont(Font.BOLD, 16f));
        dashboardPanel.add(dashboardLabel, BorderLayout.NORTH);

        // Main feature buttons - Updated to 3x2 grid for more features
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JButton profileBtn = new JButton("View Profiles");
        JButton matchBtn = new JButton("Find Tutors");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");

        // Add buttons to panel
        buttonPanel.add(profileBtn);
        buttonPanel.add(matchBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);
        dashboardPanel.add(buttonPanel, BorderLayout.CENTER);

        // Logout button at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton logoutBtn = new JButton("Logout");
        bottomPanel.add(logoutBtn);
        dashboardPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Event listeners
        profileBtn.addActionListener(e -> new ProfileView(profileHandler));
        matchBtn.addActionListener(e -> new MatchingView(matchingHandler));
        sessionBtn.addActionListener(e -> {
            if (currentUser != null) {
                new SessionView(sessionHandler, currentUser);
            } else {
                JOptionPane.showMessageDialog(this, "Please login first to access session management",
                        "Authentication Required", JOptionPane.WARNING_MESSAGE);
            }
        });
        messageBtn.addActionListener(e -> new MessageView(messageHandler));
        //supportBtn.addActionListener(e -> new SupportView(supportHandler));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));
        logoutBtn.addActionListener(e -> logout());

        return dashboardPanel;
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
        onAuthenticationSuccess();
    }

    /**
     * Shows the authentication view (login/signup)
     */
    public void showAuthenticationView() {
        setTitle("SkolarD - Welcome");
        this.currentUser = null; // Clear current user on logout
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