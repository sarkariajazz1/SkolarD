
package skolard.presentation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import skolard.logic.faq.FAQHandler;
import skolard.logic.auth.LoginHandler;
import skolard.logic.matching.MatchingHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.profile.ProfileHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.support.SupportHandler;
import skolard.objects.Student;
import skolard.objects.Support;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;
import skolard.presentation.auth.LoginView;
import skolard.presentation.auth.SignUpView;
import skolard.presentation.dashboard.StudentView;
import skolard.presentation.dashboard.TutorView;
import skolard.presentation.faq.FAQView;
import skolard.presentation.message.MessageView;
import skolard.presentation.profile.ProfileView;
import skolard.presentation.profile.StudentProfileView;
import skolard.presentation.profile.TutorProfileView;
import skolard.presentation.session.SessionView;
import skolard.presentation.support.SupportView;

public class SkolardApp extends JFrame {

    private final ProfileHandler profileHandler;
    private final MatchingHandler matchingHandler;
    private final SessionHandler sessionHandler;
    private final MessageHandler messageHandler;
    private final FAQHandler faqHandler;
    private final LoginHandler loginHandler;

    private User currentUser;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel dashboardPanel;
    private JPanel dashboardContentPanel; // Main content area for dashboard

    public SkolardApp(ProfileHandler profileHandler, MatchingHandler matchingHandler,
                      SessionHandler sessionHandler, MessageHandler messageHandler,
                      FAQHandler faqHandler, LoginHandler loginHandler) {
        super("SkolarD - Welcome");

        this.profileHandler = profileHandler;
        this.matchingHandler = matchingHandler;
        this.sessionHandler = sessionHandler;
        this.messageHandler = messageHandler;
        this.faqHandler = faqHandler;
        this.loginHandler = loginHandler;

        initializeUI();
        showAuthenticationView();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700); // Increased size for dashboard layout
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel authPanel = createAuthenticationPanel();
        dashboardPanel = createDashboardPanel();

        mainPanel.add(authPanel, "AUTH");
        mainPanel.add(dashboardPanel, "DASHBOARD");

        add(mainPanel);
    }

    private JPanel createAuthenticationPanel() {
        JPanel authPanel = new JPanel(new BorderLayout(10, 10));

        JLabel welcomeLabel = new JLabel("Welcome to SkolarD", SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16f));
        authPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(loginBtn);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(faqBtn);
        authPanel.add(buttonPanel, BorderLayout.CENTER);

        JLabel instructionLabel = new JLabel("Please login or sign up to access all features", SwingConstants.CENTER);
        authPanel.add(instructionLabel, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> new LoginView(profileHandler, loginHandler, this));
        signUpBtn.addActionListener(e -> new SignUpView(profileHandler, loginHandler, this));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return authPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title at top
        String titleText = "SkolarD Dashboard";
        if (currentUser != null) {
            String userType = currentUser instanceof Student ? "Student" :
                    currentUser instanceof Tutor ? "Tutor" : "User";
            titleText += " - " + userType + ": " + currentUser.getName();
        }

        JLabel titleLabel = new JLabel(titleText, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Left sidebar with navigation buttons
        JPanel sidebarPanel = createSidebarPanel();
        panel.add(sidebarPanel, BorderLayout.WEST);

        // Main content area (dashboard content)
        dashboardContentPanel = createDashboardContentPanel();
        panel.add(dashboardContentPanel, BorderLayout.CENTER);

        // Bottom panel with logout button (centered)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(logoutBtn.getFont().deriveFont(Font.BOLD, 14f));
        bottomPanel.add(logoutBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        logoutBtn.addActionListener(e -> logout());

        return panel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setBorder(BorderFactory.createTitledBorder("Navigation"));
        sidebar.setPreferredSize(new java.awt.Dimension(200, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        if (currentUser == null) {
            // Guest user buttons
            gbc.gridy = 0;
            JButton profileBtn = new JButton("Profile Management");
            profileBtn.addActionListener(e -> showLoginPrompt());
            sidebar.add(profileBtn, gbc);

            gbc.gridy = 1;
            JButton sessionBtn = new JButton("Session Management");
            sessionBtn.addActionListener(e -> showLoginPrompt());
            sidebar.add(sessionBtn, gbc);

            gbc.gridy = 2;
            JButton messageBtn = new JButton("Messages");
            messageBtn.addActionListener(e -> showLoginPrompt());
            sidebar.add(messageBtn, gbc);

            gbc.gridy = 3;
            JButton faqBtn = new JButton("FAQs");
            faqBtn.addActionListener(e -> new FAQView(faqHandler));
            sidebar.add(faqBtn, gbc);

        } else if (currentUser instanceof Student) {
            // Student navigation buttons
            gbc.gridy = 0;
            JButton manageProfileBtn = new JButton("Manage Profile");
            manageProfileBtn.addActionListener(e -> new StudentProfileView(profileHandler, (Student) currentUser));
            sidebar.add(manageProfileBtn, gbc);

            gbc.gridy = 1;
            JButton findTutorsBtn = new JButton("Find Tutors");
            findTutorsBtn.addActionListener(e -> loadFindTutorsView());
            sidebar.add(findTutorsBtn, gbc);

            gbc.gridy = 2;
            JButton sessionBtn = new JButton("Session Management");
            sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
            sidebar.add(sessionBtn, gbc);

            gbc.gridy = 3;
            JButton messageBtn = new JButton("Messages");
            messageBtn.addActionListener(e -> new MessageView(messageHandler,currentUser));
            sidebar.add(messageBtn, gbc);

            gbc.gridy = 4;
            JButton supportBtn = new JButton("Support");
            supportBtn.addActionListener(e -> new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentUser));
            sidebar.add(supportBtn, gbc);

            gbc.gridy = 5;
            JButton faqBtn = new JButton("FAQs");
            faqBtn.addActionListener(e -> new FAQView(faqHandler));
            sidebar.add(faqBtn, gbc);

        } else if (currentUser instanceof Tutor) {
            // Tutor navigation buttons
            gbc.gridy = 0;
            JButton manageProfileBtn = new JButton("Manage Profile");
            manageProfileBtn.addActionListener(e -> new TutorProfileView(profileHandler, (Tutor) currentUser));
            sidebar.add(manageProfileBtn, gbc);

            gbc.gridy = 1;
            JButton studentsBtn = new JButton("My Students");
            studentsBtn.addActionListener(e -> loadMyStudentsView());
            sidebar.add(studentsBtn, gbc);

            gbc.gridy = 2;
            JButton sessionBtn = new JButton("Session Management");
            sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
            sidebar.add(sessionBtn, gbc);

            gbc.gridy = 3;
            JButton messageBtn = new JButton("Messages");
            messageBtn.addActionListener(e -> new MessageView(messageHandler,currentUser));
            sidebar.add(messageBtn, gbc);

            gbc.gridy = 4;
            JButton supportBtn = new JButton("Support");
            supportBtn.addActionListener(e -> new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentUser));
            sidebar.add(supportBtn, gbc);

            gbc.gridy = 5;
            JButton faqBtn = new JButton("FAQs");
            faqBtn.addActionListener(e -> new FAQView(faqHandler));
            sidebar.add(faqBtn, gbc);

        } else {
            // Generic user buttons
            gbc.gridy = 0;
            JButton profileBtn = new JButton("Profile Management");
            profileBtn.addActionListener(e -> new ProfileView(profileHandler));
            sidebar.add(profileBtn, gbc);

            gbc.gridy = 1;
            JButton sessionBtn = new JButton("Session Management");
            sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
            sidebar.add(sessionBtn, gbc);

            gbc.gridy = 2;
            JButton messageBtn = new JButton("Messages");
            messageBtn.addActionListener(e -> new MessageView(messageHandler,currentUser));
            sidebar.add(messageBtn, gbc);

            gbc.gridy = 3;
            JButton faqBtn = new JButton("FAQs");
            faqBtn.addActionListener(e -> new FAQView(faqHandler));
            sidebar.add(faqBtn, gbc);
        }

        return sidebar;
    }

    private JPanel createDashboardContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder("Dashboard"));

        if (currentUser == null) {
            // Guest dashboard content
            JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h2>Welcome to SkolarD!</h2>" +
                    "<p>Please login to access your personalized dashboard.</p>" +
                    "<p>Use the navigation panel on the left to explore available features.</p>" +
                    "</div></html>", SwingConstants.CENTER);
            contentPanel.add(welcomeLabel, BorderLayout.CENTER);

        } else if (currentUser instanceof Student) {
            // Student dashboard content - embed StudentView content
            loadStudentDashboard(contentPanel);

        } else if (currentUser instanceof Tutor) {
            // Tutor dashboard content - embed TutorView content
            loadTutorDashboard(contentPanel);

        } else {
            // Generic dashboard content
            JLabel dashboardLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h2>Welcome, " + currentUser.getName() + "!</h2>" +
                    "<p>Use the navigation panel on the left to access system features.</p>" +
                    "</div></html>", SwingConstants.CENTER);
            contentPanel.add(dashboardLabel, BorderLayout.CENTER);
        }

        return contentPanel;
    }

    private void loadStudentDashboard(JPanel contentPanel) {
        // Create a simple student dashboard view
        JLabel dashboardLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>Student Dashboard</h2>" +
                "<p>Welcome, " + currentUser.getName() + "!</p>" +
                "<br>" +
                "<p><b>Quick Actions:</b></p>" +
                "<p>• Use 'Find Tutors' to search for tutors in your subjects</p>" +
                "<p>• Check 'Session Management' for your upcoming sessions</p>" +
                "<p>• Visit 'Messages' to communicate with your tutors</p>" +
                "<p>• Update your 'Manage Profile' anytime</p>" +
                "</div></html>", SwingConstants.CENTER);
        contentPanel.add(dashboardLabel, BorderLayout.CENTER);
    }

    private void loadTutorDashboard(JPanel contentPanel) {
        // Create a simple tutor dashboard view
        JLabel dashboardLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h2>Tutor Dashboard</h2>" +
                "<p>Welcome, " + currentUser.getName() + "!</p>" +
                "<br>" +
                "<p><b>Quick Actions:</b></p>" +
                "<p>• Check 'My Students' to see your current students</p>" +
                "<p>• Manage your 'Session Management' and availability</p>" +
                "<p>• Use 'Messages' to communicate with students</p>" +
                "<p>• Keep your 'Manage Profile' up to date with courses and bio</p>" +
                "</div></html>", SwingConstants.CENTER);
        contentPanel.add(dashboardLabel, BorderLayout.CENTER);
    }

    private void loadFindTutorsView() {
        // Load the find tutors functionality (StudentView)
        new StudentView(profileHandler, matchingHandler, messageHandler, (Student) currentUser);
    }

    private void loadMyStudentsView() {
        // Load the my students functionality (TutorView)
        new TutorView(profileHandler, sessionHandler, messageHandler, (Tutor) currentUser);
    }

    private void showLoginPrompt() {
        JOptionPane.showMessageDialog(this, "Please login first to access this feature.",
                "Authentication Required", JOptionPane.WARNING_MESSAGE);
    }

    public void onAuthenticationSuccess(User user) {
        this.currentUser = user;

        // Handle Support users separately (they get their own view)
        if (user instanceof Support) {
            SupportHandler supportHandler = new SupportHandler(PersistenceRegistry.getSupportPersistence());
            new SupportView(supportHandler, user);
            return;
        }

        // Update the dashboard with user-specific content
        setTitle("SkolarD - Dashboard (" + user.getName() + ")");
        mainPanel.remove(dashboardPanel);
        dashboardPanel = createDashboardPanel();
        mainPanel.add(dashboardPanel, "DASHBOARD");
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    public void onAuthenticationSuccess() {
        setTitle("SkolarD - Dashboard");
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    public void showAuthenticationView() {
        setTitle("SkolarD - Welcome");
        this.currentUser = null;

        mainPanel.remove(dashboardPanel);
        dashboardPanel = createDashboardPanel();
        mainPanel.add(dashboardPanel, "DASHBOARD");

        cardLayout.show(mainPanel, "AUTH");
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            showAuthenticationView();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}