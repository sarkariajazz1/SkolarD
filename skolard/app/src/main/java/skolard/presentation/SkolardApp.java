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
import skolard.presentation.dashboard.StudentView;
import skolard.presentation.dashboard.TutorView;
import skolard.presentation.faq.FAQView;
import skolard.presentation.matching.MatchingView;
import skolard.presentation.message.MessageView;
import skolard.presentation.profile.ProfileView;
import skolard.presentation.rating.RatingView;
import skolard.presentation.session.SessionView;
import skolard.presentation.support.SupportView;

public class SkolardApp extends JFrame {

    private final ProfileHandler profileHandler;
    private final MatchingHandler matchingHandler;
    private final SessionHandler sessionHandler;
    private final MessageHandler messageHandler;
    private final FAQHandler faqHandler;
    private final LoginHandler loginHandler;
    private final RatingHandler ratingHandler; // Uncomment if RatingHandler is implemented

    private User currentUser;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel dashboardPanel;

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
        setSize(600, 300);
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
        JPanel localDashboardPanel = new JPanel(new BorderLayout(10, 10));

        String titleText = "SkolarD Dashboard";
        if (currentUser != null) {
            String userType = currentUser instanceof Student ? "Student" :
                              currentUser instanceof Tutor ? "Tutor" : "User";
            titleText += " - " + userType + ": " + currentUser.getName();
        }

        JLabel dashboardLabel = new JLabel(titleText, SwingConstants.CENTER);
        dashboardLabel.setFont(dashboardLabel.getFont().deriveFont(Font.BOLD, 16f));
        localDashboardPanel.add(dashboardLabel, BorderLayout.NORTH);

        JPanel buttonPanel = createButtonPanelForUser();
        localDashboardPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton logoutBtn = new JButton("Logout");
        bottomPanel.add(logoutBtn);
        localDashboardPanel.add(bottomPanel, BorderLayout.SOUTH);

        logoutBtn.addActionListener(e -> logout());

        return localDashboardPanel;
    }

    private JPanel createButtonPanelForUser() {
        if (currentUser == null) {
            JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            JButton profileBtn = new JButton("Profile Management");
            JButton sessionBtn = new JButton("Session Management");
            JButton messageBtn = new JButton("Messages");
            JButton faqBtn = new JButton("FAQs");

            buttonPanel.add(profileBtn);
            buttonPanel.add(sessionBtn);
            buttonPanel.add(messageBtn);
            buttonPanel.add(faqBtn);

            profileBtn.addActionListener(e -> showLoginPrompt());
            sessionBtn.addActionListener(e -> showLoginPrompt());
            messageBtn.addActionListener(e -> showLoginPrompt());
            faqBtn.addActionListener(e -> new FAQView(faqHandler));

            return buttonPanel;
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
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));

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

        myDashboardBtn.addActionListener(e -> new StudentView(profileHandler, matchingHandler, messageHandler, sessionHandler, ratingHandler, (Student) currentUser));
        findTutorsBtn.addActionListener(e -> new MatchingView(matchingHandler, sessionHandler, (Student) currentUser));
        sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
        messageBtn.addActionListener(e -> new MessageView(messageHandler,currentUser));
        supportBtn.addActionListener(e -> new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentUser));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));
        rateBtn.addActionListener(e-> new RatingView(ratingHandler));

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

        myDashboardBtn.addActionListener(e -> new TutorView(profileHandler, sessionHandler, messageHandler, (Tutor) currentUser));
        studentsBtn.addActionListener(e -> new TutorView(profileHandler, sessionHandler, messageHandler, (Tutor) currentUser));
        sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
        messageBtn.addActionListener(e -> new MessageView(messageHandler,currentUser));
        supportBtn.addActionListener(e -> new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentUser));
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

        profileBtn.addActionListener(e -> new ProfileView(profileHandler));
        sessionBtn.addActionListener(e -> new SessionView(sessionHandler, currentUser));
        messageBtn.addActionListener(e -> new MessageView(messageHandler,currentUser));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return buttonPanel;
    }

    private void showLoginPrompt() {
        JOptionPane.showMessageDialog(this, "Please login first to access this feature.",
                "Authentication Required", JOptionPane.WARNING_MESSAGE);
    }

    public void onAuthenticationSuccess(User user) {
        this.currentUser = user;

        if (user instanceof Support) {
            SupportHandler supportHandler = new SupportHandler(PersistenceRegistry.getSupportPersistence());
            new SupportView(supportHandler, user);
            return;
        }

        setTitle("SkolarD - Dashboard (" + (user != null ? user.getName() : "Unknown") + ")");
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
