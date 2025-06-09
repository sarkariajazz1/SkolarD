
package skolard.presentation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

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
import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.support.SupportHandler;
import skolard.objects.Student;
import skolard.objects.Support;
import skolard.objects.Tutor;
import skolard.objects.User;
import skolard.persistence.PersistenceRegistry;
import skolard.presentation.auth.LoginView;
import skolard.presentation.auth.SignUpView;
import skolard.presentation.faq.FAQView;
import skolard.presentation.dashboard.StudentView;
import skolard.presentation.matching.MatchingView;
import skolard.presentation.session.SessionView;
import skolard.presentation.message.MessageView;
import skolard.presentation.support.SupportView;
import skolard.presentation.rating.RatingView;
import skolard.presentation.dashboard.TutorView;
import skolard.presentation.support.SupportView;

public class SkolardApp extends JFrame {

    private final ProfileHandler profileHandler;
    private final MatchingHandler matchingHandler;
    private final SessionHandler sessionHandler;
    private final MessageHandler messageHandler;
    private final FAQHandler faqHandler;
    private final LoginHandler loginHandler;
    private final RatingHandler ratingHandler;

    private User currentUser;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel dashboardPanel;
    private boolean isFirstLogin = false;

    // Window state management
    private final List<Window> openWindows = new ArrayList<>();

    public SkolardApp(ProfileHandler profileHandler,
                      MatchingHandler matchingHandler,
                      SessionHandler sessionHandler,
                      MessageHandler messageHandler,
                      FAQHandler faqHandler,
                      LoginHandler loginHandler,
                      RatingHandler ratingHandler
    ) {
        super("SkolarD - Welcome");

        this.profileHandler = profileHandler;
        this.matchingHandler = matchingHandler;
        this.sessionHandler = sessionHandler;
        this.messageHandler = messageHandler;
        this.faqHandler = faqHandler;
        this.loginHandler = loginHandler;
        this.ratingHandler = ratingHandler;

        initializeUI();
        showAuthenticationView();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // Add window listener to clean up open windows when main app closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeAllOpenWindows();
            }
        });
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

        loginBtn.addActionListener(e -> openWindow(new LoginView(profileHandler, loginHandler, this)));
        signUpBtn.addActionListener(e -> openWindow(new SignUpView(profileHandler, loginHandler, this)));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return authPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(10, 10));

        // Create main content panel with BorderLayout
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Left side - Button panel
        JPanel buttonPanel = createButtonPanelForUser();
        contentPanel.add(buttonPanel, BorderLayout.WEST);

        // Right side - Welcome message
        JPanel welcomePanel = createWelcomePanel();
        contentPanel.add(welcomePanel, BorderLayout.CENTER);

        dashboardPanel.add(contentPanel, BorderLayout.CENTER);

        // Bottom panel for logout
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton logoutBtn = new JButton("Logout");
        bottomPanel.add(logoutBtn);
        dashboardPanel.add(bottomPanel, BorderLayout.SOUTH);

        logoutBtn.addActionListener(e -> logout());

        return dashboardPanel;
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout(10, 10));

        if (currentUser != null) {
            String userType = currentUser instanceof Student ? "Student" :
                    currentUser instanceof Tutor ? "Tutor" : "User";

            String welcomeText = isFirstLogin ?
                    "Welcome " + currentUser.getName() + "!" :
                    "Welcome back " + currentUser.getName() + "!";

            JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h2>" + welcomeText + "</h2>" +
                    "<p>You are logged in as a " + userType + "</p>" +
                    "<p>Use the buttons on the left to access different features</p>" +
                    "</div></html>", SwingConstants.CENTER);

            welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        } else {
            JLabel defaultLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h2>SkolarD Dashboard</h2>" +
                    "<p>Please login to access features</p>" +
                    "</div></html>", SwingConstants.CENTER);
            welcomePanel.add(defaultLabel, BorderLayout.CENTER);
        }

        return welcomePanel;
    }

    private JPanel createButtonPanelForUser() {
        if (currentUser == null) {
            return createGenericButtonPanel();
        }

        if (currentUser instanceof Student) {
            return createStudentButtonPanel();
        } else if (currentUser instanceof Tutor) {
            return createTutorButtonPanel();
        } else {
            return createGenericButtonPanel();
        }
    }

    private JPanel createStudentButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 10, 10));

        JButton myDashboardBtn = new JButton("My Dashboard");
        JButton findTutorsBtn = new JButton("Find Tutor Sessions");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");
        JButton rateBtn = new JButton("Rate Tutor/Session");

        buttonPanel.add(myDashboardBtn);
        buttonPanel.add(findTutorsBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);
        buttonPanel.add(rateBtn);

        // Setup event listeners
        myDashboardBtn.addActionListener(e -> {
            openWindow(new StudentView(profileHandler, matchingHandler, messageHandler, (Student) currentUser));
        });

        findTutorsBtn.addActionListener(e -> {
            openWindow(new MatchingView(matchingHandler, sessionHandler, (Student) currentUser));
        });

        sessionBtn.addActionListener(e -> {
            openWindow(new SessionView(sessionHandler, currentUser));
        });

        messageBtn.addActionListener(e -> {
            openWindow(new MessageView(messageHandler, currentUser));
        });

        supportBtn.addActionListener(e -> {
            openWindow(new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentUser));
        });

        faqBtn.addActionListener(e -> {
            new FAQView(faqHandler);
        });

        rateBtn.addActionListener(e -> {
            openWindow(new RatingView(ratingHandler));
        });

        return buttonPanel;
    }

    private JPanel createTutorButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));

        JButton myStudentsBtn = new JButton("My Students");
        JButton manageProfileBtn = new JButton("Manage Profile");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(myStudentsBtn);
        buttonPanel.add(manageProfileBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);

        // Setup event listeners
        myStudentsBtn.addActionListener(e -> {
            openWindow(new TutorView(profileHandler, sessionHandler, messageHandler, (Tutor) currentUser));
        });

        manageProfileBtn.addActionListener(e -> {
            showManageProfileView();
        });

        sessionBtn.addActionListener(e -> {
            openWindow(new SessionView(sessionHandler, currentUser));
        });

        messageBtn.addActionListener(e -> {
            openWindow(new MessageView(messageHandler, currentUser));
        });

        supportBtn.addActionListener(e -> {
            openWindow(new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentUser));
        });

        faqBtn.addActionListener(e -> {
            new FAQView(faqHandler);
        });

        return buttonPanel;
    }

    private void showManageProfileView() {
        JFrame profileFrame = new JFrame("Manage Profile - " + currentUser.getName());
        profileFrame.setLayout(new BorderLayout(10, 10));

        // Simple profile management for now
        JLabel profileLabel = new JLabel("<html><div style='text-align: center;'>" +
                "<h3>Profile Management</h3>" +
                "<p>Name: " + currentUser.getName() + "</p>" +
                "<p>Email: " + currentUser.getEmail() + "</p>" +
                "</div></html>", SwingConstants.CENTER);

        profileFrame.add(profileLabel, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> profileFrame.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeBtn);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(300, 200);
        profileFrame.setLocationRelativeTo(this);
        profileFrame.setVisible(true);
    }

    private JPanel createGenericButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");
        JButton faqBtn = new JButton("FAQs");

        buttonPanel.add(loginBtn);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(faqBtn);

        loginBtn.addActionListener(e -> openWindow(new LoginView(profileHandler, loginHandler, this)));
        signUpBtn.addActionListener(e -> openWindow(new SignUpView(profileHandler, loginHandler, this)));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return buttonPanel;
    }

    private void showLoginPrompt() {
        JOptionPane.showMessageDialog(this, "Please login first to access this feature.",
                "Authentication Required", JOptionPane.WARNING_MESSAGE);
    }

    public void onAuthenticationSuccess(User user) {
        onAuthenticationSuccess(user, false);
    }

    public void onAuthenticationSuccess(User user, boolean isFirstLogin) {
        this.currentUser = user;
        this.isFirstLogin = isFirstLogin;

        if (user instanceof Support) {
            SupportHandler supportHandler = new SupportHandler(PersistenceRegistry.getSupportPersistence());
            openWindow(new SupportView(supportHandler, user));
            return;
        }

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
        this.isFirstLogin = false;

        // Close all open windows when user logs out
        closeAllOpenWindows();

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

    /**
     * Registers and opens a new window, adding it to the tracked windows list
     */
    private void openWindow(Window window) {
        openWindows.add(window);

        // Add listener to remove from list when window is closed
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                openWindows.remove(window);
            }
        });

        window.setVisible(true);
    }

    /**
     * Closes all open windows (except the main app window)
     */
    private void closeAllOpenWindows() {
        List<Window> windowsToClose = new ArrayList<>(openWindows);
        for (Window window : windowsToClose) {
            if (window != this && window.isDisplayable()) {
                window.dispose();
            }
        }
        openWindows.clear();
    }

    public User getCurrentUser() {
        return currentUser;
    }
}