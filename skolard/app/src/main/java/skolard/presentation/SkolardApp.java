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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import skolard.logic.faq.FAQHandler;
import skolard.logic.auth.LoginHandler;
import skolard.logic.booking.BookingHandler;
import skolard.logic.message.MessageHandler;
import skolard.logic.payment.PaymentHandler;
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
import skolard.presentation.booking.BookingView;
import skolard.presentation.session.SessionView;
import skolard.presentation.message.MessageView;
import skolard.presentation.support.SupportView;
import skolard.presentation.rating.RatingView;
import skolard.presentation.dashboard.TutorView;

/**
 * Main application frame for SkolarD, handling user authentication and displaying the appropriate dashboard.
 * Manages the top-level UI navigation between authentication views and user-specific dashboards.
 */
public class SkolardApp extends JFrame {

    // Handlers for various business logic components.
    private final ProfileHandler profileHandler;
    private final BookingHandler bookingHandler;
    private final SessionHandler sessionHandler;
    private final MessageHandler messageHandler;
    private final FAQHandler faqHandler;
    private final LoginHandler loginHandler;
    private final RatingHandler ratingHandler;
    private final PaymentHandler paymentHandler;

    // Current logged-in user.
    private User currentUser;
    // Main panel using CardLayout for switching views (auth vs. dashboard).
    private JPanel mainPanel;
    // Layout manager for switching between different panels in mainPanel.
    private CardLayout cardLayout;
    // Panel that displays the user's dashboard.
    private JPanel dashboardPanel;
    // Flag to indicate if it's the user's first login.
    private boolean isFirstLogin = false;

    // List to keep track of all opened child windows for proper closing on app exit.
    private final List<Window> openWindows = new ArrayList<>();

    /**
     * Constructs the main SkolarD application frame.
     * Initializes all necessary handlers and sets up the primary UI structure.
     *
     * @param profileHandler The handler for user profiles.
     * @param bookingHandler The handler for session bookings.
     * @param sessionHandler The handler for tutoring sessions.
     * @param messageHandler The handler for user messages.
     * @param faqHandler The handler for frequently asked questions.
     * @param loginHandler The handler for user authentication.
     * @param ratingHandler The handler for tutor/session ratings.
     * @param paymentHandler The handler for payment processing.
     */
    public SkolardApp(ProfileHandler profileHandler,
                      BookingHandler bookingHandler,
                      SessionHandler sessionHandler,
                      MessageHandler messageHandler,
                      FAQHandler faqHandler,
                      LoginHandler loginHandler,
                      RatingHandler ratingHandler,
                      PaymentHandler paymentHandler
    ) {
        super("SkolarD - Welcome");

        // Assign injected handlers to instance variables.
        this.profileHandler = profileHandler;
        this.bookingHandler = bookingHandler;
        this.sessionHandler = sessionHandler;
        this.messageHandler = messageHandler;
        this.faqHandler = faqHandler;
        this.loginHandler = loginHandler;
        this.ratingHandler = ratingHandler;
        this.paymentHandler = paymentHandler;

        // Initialize UI components and display the authentication view.
        initializeUI();
        showAuthenticationView();

        // Set default JFrame properties.
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit application on closing this frame.
        setSize(800, 500); // Set initial window size.
        setLocationRelativeTo(null); // Center the window on the screen.
        setVisible(true); // Make the window visible.

        // Add a window listener to close all child windows when the main application frame closes.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeAllOpenWindows(); // Close all tracked child windows.
            }
        });
    }

    /**
     * Initializes the main UI structure using a CardLayout to switch between
     * the authentication panel and the user dashboard panel.
     *
     * @return void
     */
    private void initializeUI() {
        // Create a new CardLayout.
        cardLayout = new CardLayout();
        // Initialize the main panel with the CardLayout.
        mainPanel = new JPanel(cardLayout);

        // Create the authentication and dashboard panels.
        JPanel authPanel = createAuthenticationPanel();
        dashboardPanel = createDashboardPanel();

        // Add both panels to the main panel with distinct names for CardLayout switching.
        mainPanel.add(authPanel, "AUTH");
        mainPanel.add(dashboardPanel, "DASHBOARD");

        // Add the main panel to the JFrame.
        add(mainPanel);
    }

    /**
     * Creates and configures the panel for user authentication (Login and Sign Up).
     * Includes buttons to access login, sign-up, and FAQ views.
     *
     * @return A {@link JPanel} representing the authentication view.
     */
    private JPanel createAuthenticationPanel() {
        JPanel authPanel = new JPanel(new BorderLayout(10, 10));

        // Create and configure a welcome label.
        JLabel welcomeLabel = new JLabel("Welcome to SkolarD", SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16f));
        authPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Create a panel for authentication buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");
        JButton faqBtn = new JButton("FAQs");

        // Add buttons to the panel.
        buttonPanel.add(loginBtn);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(faqBtn);
        authPanel.add(buttonPanel, BorderLayout.CENTER);

        // Create and configure an instruction label.
        JLabel instructionLabel = new JLabel("Please login or sign up to access all features", SwingConstants.CENTER);
        authPanel.add(instructionLabel, BorderLayout.SOUTH);

        // Add action listeners to authentication buttons to open respective views.
        loginBtn.addActionListener(e -> openWindow(new LoginView(profileHandler, loginHandler, this)));
        signUpBtn.addActionListener(e -> openWindow(new SignUpView(profileHandler, loginHandler, this)));
        faqBtn.addActionListener(e -> new FAQView(faqHandler)); // FAQView is a JFrame, not tracked by openWindow

        return authPanel;
    }

    /**
     * Creates and configures the main dashboard panel, which displays user-specific
     * information and navigation options. The content of the dashboard dynamically
     * changes based on the `currentUser`'s role.
     *
     * @return A {@link JPanel} representing the user's dashboard.
     */
    private JPanel createDashboardPanel() {
        JPanel localDashboardPanel = new JPanel(new BorderLayout(10, 10));

        // Create a title for the dashboard, including user type and name if logged in.
        String titleText = "SkolarD Dashboard";
        if (currentUser != null) {
            String userType = currentUser instanceof Student ? "Student" :
                    currentUser instanceof Tutor ? "Tutor" : "User";
            titleText += " - " + userType + ": " + currentUser.getName();
        }

        // Create and configure the dashboard title label.
        JLabel dashboardLabel = new JLabel(titleText, SwingConstants.CENTER);
        dashboardLabel.setFont(dashboardLabel.getFont().deriveFont(Font.BOLD, 16f));
        localDashboardPanel.add(dashboardLabel, BorderLayout.NORTH);

        // Create a central content panel to hold specific UI elements.
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Create and add the button panel based on the current user's role.
        JPanel buttonPanel = createButtonPanelForUser();
        contentPanel.add(buttonPanel, BorderLayout.WEST);

        // Create and add the welcome message panel.
        JPanel welcomePanel = createWelcomePanel();
        contentPanel.add(welcomePanel, BorderLayout.CENTER);

        localDashboardPanel.add(contentPanel, BorderLayout.CENTER);

        // Create and add the logout button panel at the bottom.
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton logoutBtn = new JButton("Logout");
        bottomPanel.add(logoutBtn);
        localDashboardPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add action listener for the logout button.
        logoutBtn.addActionListener(e -> logout());

        return localDashboardPanel;
    }

    /**
     * Creates and configures a welcome panel displayed on the dashboard.
     * The message adapts based on whether a user is logged in and if it's their first login.
     *
     * @return A {@link JPanel} displaying a welcome message.
     */
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new BorderLayout(10, 10));

        if (currentUser != null) {
            // Determine user type for display.
            String userType = currentUser instanceof Student ? "Student" :
                    currentUser instanceof Tutor ? "Tutor" : "User";

            // Craft a personalized welcome message.
            String welcomeText = isFirstLogin ?
                    "Welcome " + currentUser.getName() + "!" :
                    "Welcome back " + currentUser.getName() + "!";

            // Create and configure the welcome label with HTML for formatting.
            JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h2>" + welcomeText + "</h2>" +
                    "<p>You are logged in as a " + userType + "</p>" +
                    "<p>Use the buttons on the left to access different features</p>" +
                    "</div></html>", SwingConstants.CENTER);

            welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        } else {
            // Display a default message if no user is logged in.
            JLabel defaultLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<h2>SkolarD Dashboard</h2>" +
                    "<p>Please login to access features</p>" +
                    "</div></html>", SwingConstants.CENTER);
            welcomePanel.add(defaultLabel, BorderLayout.CENTER);
        }

        return welcomePanel;
    }

    /**
     * Determines which specific button panel (student, tutor, or generic) to create
     * based on the type of the `currentUser`.
     *
     * @return A {@link JPanel} containing buttons relevant to the current user's role.
     */
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

    /**
     * Creates and configures the button panel specific to a Student user.
     * Includes buttons for profile, booking, session management, messaging, support, FAQ, and rating.
     *
     * @return A {@link JPanel} with student-specific functionality buttons.
     */
    private JPanel createStudentButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 10, 10)); // Grid layout for buttons.

        // Initialize student-specific buttons.
        JButton viewMyProfileBtn = new JButton("View My Profile");
        JButton findTutorsBtn = new JButton("Find Tutor Sessions");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");
        JButton rateBtn = new JButton("Rate Tutor/Session");

        // Add buttons to the panel.
        buttonPanel.add(viewMyProfileBtn);
        buttonPanel.add(findTutorsBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);
        buttonPanel.add(rateBtn);

        // Setup event listeners for student buttons.
        viewMyProfileBtn.addActionListener(e -> {
            showStudentProfileView(); // Display student's profile.
        });

        findTutorsBtn.addActionListener(e -> {
            // Open the BookingView.
            openWindow(new BookingView(bookingHandler, sessionHandler, ratingHandler, paymentHandler, (Student) currentUser));
        });

        sessionBtn.addActionListener(e -> {
            // Open the SessionView.
            openWindow(new SessionView(sessionHandler, currentUser));
        });

        messageBtn.addActionListener(e -> {
            // Open the MessageView.
            openWindow(new MessageView(messageHandler, currentUser));
        });

        supportBtn.addActionListener(e -> {
            // Open the SupportView.
            openWindow(new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentUser));
        });

        faqBtn.addActionListener(e -> {
            new FAQView(faqHandler); // Open FAQView.
        });

        rateBtn.addActionListener(e -> {
            // Open the RatingView.
            openWindow(new RatingView(ratingHandler, (Student) currentUser));
        });

        return buttonPanel;
    }

    /**
     * Creates and configures the button panel specific to a Tutor user.
     * Includes buttons for managing students, profile, sessions, messages, support, and FAQs.
     *
     * @return A {@link JPanel} with tutor-specific functionality buttons.
     */
    private JPanel createTutorButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Grid layout for buttons.

        // Initialize tutor-specific buttons.
        JButton myStudentsBtn = new JButton("My Students");
        JButton manageProfileBtn = new JButton("Manage Profile");
        JButton sessionBtn = new JButton("Session Management");
        JButton messageBtn = new JButton("Messages");
        JButton supportBtn = new JButton("Support");
        JButton faqBtn = new JButton("FAQs");

        // Add buttons to the panel.
        buttonPanel.add(myStudentsBtn);
        buttonPanel.add(manageProfileBtn);
        buttonPanel.add(sessionBtn);
        buttonPanel.add(messageBtn);
        buttonPanel.add(supportBtn);
        buttonPanel.add(faqBtn);

        // Setup event listeners for tutor buttons.
        myStudentsBtn.addActionListener(e -> {
            // Open the TutorView.
            openWindow(new TutorView(profileHandler, sessionHandler, messageHandler, (Tutor) currentUser));
        });

        manageProfileBtn.addActionListener(e -> {
            showManageProfileView(); // Display tutor's profile management.
        });

        sessionBtn.addActionListener(e -> {
            // Open the SessionView.
            openWindow(new SessionView(sessionHandler, currentUser));
        });

        messageBtn.addActionListener(e -> {
            // Open the MessageView.
            openWindow(new MessageView(messageHandler, currentUser));
        });

        supportBtn.addActionListener(e -> {
            // Open the SupportView.
            openWindow(new SupportView(new SupportHandler(PersistenceRegistry.getSupportPersistence()), currentUser));
        });

        faqBtn.addActionListener(e -> {
            new FAQView(faqHandler); // Open FAQView.
        });

        return buttonPanel;
    }

    /**
     * Creates and displays a separate JFrame to show the current student's full profile details.
     * This view is read-only for the student.
     *
     * @return void
     */
    private void showStudentProfileView() {
        JFrame profileFrame = new JFrame("My Profile - " + currentUser.getName());
        profileFrame.setLayout(new BorderLayout(10, 10));

        // Display current profile in a non-editable text area.
        JTextArea profileArea = new JTextArea(15, 40);
        profileArea.setEditable(false);
        profileArea.setText(profileHandler.viewFullProfile(currentUser));

        JScrollPane scrollPane = new JScrollPane(profileArea);
        profileFrame.add(scrollPane, BorderLayout.CENTER);

        // Button panel with only a "Back" button for students.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backBtn = new JButton("Back");

        backBtn.addActionListener(e -> profileFrame.dispose()); // Close profile frame on back.

        buttonPanel.add(backBtn);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties.
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(500, 400);
        profileFrame.setLocationRelativeTo(this); // Center relative to the main app frame.
        profileFrame.setVisible(true);
    }

    /**
     * Creates and displays a separate JFrame to allow the tutor to view and manage their profile details.
     * This includes an option to edit their bio.
     *
     * @return void
     */
    private void showManageProfileView() {
        JFrame profileFrame = new JFrame("Manage Profile - " + currentUser.getName());
        profileFrame.setLayout(new BorderLayout(10, 10));

        // Display current profile in a non-editable text area.
        JTextArea profileArea = new JTextArea(15, 40);
        profileArea.setEditable(false);
        profileArea.setText(profileHandler.viewFullProfile(currentUser));

        JScrollPane scrollPane = new JScrollPane(profileArea);
        profileFrame.add(scrollPane, BorderLayout.CENTER);

        // Button panel with "Edit Bio" and "Back" buttons.
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton updateBioBtn = new JButton("Edit Bio");
        JButton backBtn = new JButton("Back");

        updateBioBtn.addActionListener(e -> {
            // Ensure the current user is a Tutor before attempting to update bio.
            if (currentUser instanceof Tutor) {
                Tutor currentTutor = (Tutor) currentUser;
                String currentBio = currentTutor.getBio();
                // Prompt for new bio.
                String newBio = JOptionPane.showInputDialog(profileFrame,
                        "Enter your new bio:", currentBio);

                if (newBio != null && !newBio.trim().isEmpty()) {
                    // Update bio using profile handler and refresh display.
                    profileHandler.updateBio(currentTutor, newBio.trim());
                    profileArea.setText(profileHandler.viewFullProfile(currentTutor));
                    JOptionPane.showMessageDialog(profileFrame, "Bio updated successfully!");
                }
            }
        });

        backBtn.addActionListener(e -> profileFrame.dispose()); // Close profile frame on back.

        buttonPanel.add(updateBioBtn);
        buttonPanel.add(backBtn);
        profileFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties.
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setSize(500, 400);
        profileFrame.setLocationRelativeTo(this); // Center relative to the main app frame.
        profileFrame.setVisible(true);
    }

    /**
     * Creates and configures a generic button panel with options for Login, Sign Up, and FAQs.
     * This panel is displayed when no user is logged in or if the user type is not specifically handled.
     *
     * @return A {@link JPanel} with generic authentication/information buttons.
     */
    private JPanel createGenericButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // Initialize generic buttons.
        JButton loginBtn = new JButton("Login");
        JButton signUpBtn = new JButton("Sign Up");
        JButton faqBtn = new JButton("FAQs");

        // Add buttons to the panel.
        buttonPanel.add(loginBtn);
        buttonPanel.add(signUpBtn);
        buttonPanel.add(faqBtn);

        // Add action listeners to generic buttons.
        loginBtn.addActionListener(e -> openWindow(new LoginView(profileHandler, loginHandler, this)));
        signUpBtn.addActionListener(e -> openWindow(new SignUpView(profileHandler, loginHandler, this)));
        faqBtn.addActionListener(e -> new FAQView(faqHandler));

        return buttonPanel;
    }

    /**
     * Callback method invoked when a user successfully authenticates.
     * Sets the current user and switches the main panel to the dashboard view.
     * This version defaults `isFirstLogin` to `false`.
     *
     * @param user The {@link User} object representing the authenticated user.
     * @return void
     */
    public void onAuthenticationSuccess(User user) {
        onAuthenticationSuccess(user, false);
    }

    /**
     * Callback method invoked when a user successfully authenticates, including
     * a flag to indicate if it's their first login.
     * Sets the current user, updates the frame title, rebuilds the dashboard,
     * and switches the main panel to the dashboard view.
     *
     * @param user The {@link User} object representing the authenticated user.
     * @param isFirstLogin A boolean flag indicating if this is the user's first login.
     * @return void
     */
    public void onAuthenticationSuccess(User user, boolean isFirstLogin) {
        this.currentUser = user; // Set the current user.
        this.isFirstLogin = isFirstLogin; // Set the first login flag.

        // If the user is support staff, open their dedicated support view and return.
        if (user instanceof Support) {
            SupportHandler supportHandler = new SupportHandler(PersistenceRegistry.getSupportPersistence());
            openWindow(new SupportView(supportHandler, user));
            return;
        }

        // Update the main frame title with the user's name.
        setTitle("SkolarD - Dashboard (" + (user != null ? user.getName() : "Unknown") + ")");
        // Remove the old dashboard panel and create a new one to reflect user-specific options.
        mainPanel.remove(dashboardPanel);
        dashboardPanel = createDashboardPanel();
        mainPanel.add(dashboardPanel, "DASHBOARD");
        // Switch to the dashboard view.
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    /**
     * A simplified callback method for authentication success.
     * It sets the title and switches to the dashboard view without updating user details.
     * This method might be a remnant or used for generic success without specific user info.
     *
     * @return void
     */
    public void onAuthenticationSuccess() {
        setTitle("SkolarD - Dashboard");
        cardLayout.show(mainPanel, "DASHBOARD");
    }

    /**
     * Switches the main panel back to the authentication view (login/sign-up).
     * This effectively logs out the current user and clears any open child windows.
     *
     * @return void
     */
    public void showAuthenticationView() {
        setTitle("SkolarD - Welcome");
        this.currentUser = null; // Clear the current user.
        this.isFirstLogin = false; // Reset first login flag.

        // Close all open child windows when logging out.
        closeAllOpenWindows();

        // Recreate and add the dashboard panel to ensure it's fresh for the next login.
        mainPanel.remove(dashboardPanel);
        dashboardPanel = createDashboardPanel();
        mainPanel.add(dashboardPanel, "DASHBOARD");

        // Switch to the authentication view.
        cardLayout.show(mainPanel, "AUTH");
    }

    /**
     * Handles the logout process.
     * Prompts the user for confirmation and, if confirmed, switches to the authentication view.
     *
     * @return void
     */
    private void logout() {
        // Show a confirmation dialog for logging out.
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            showAuthenticationView(); // Switch to authentication view on confirmation.
        }
    }

    /**
     * Registers and opens a new {@link Window} (e.g., JFrame, JDialog), adding it to the
     * tracked windows list. This ensures that child windows can be properly closed
     * when the main application frame shuts down.
     *
     * @param window The {@link Window} instance to open and track.
     * @return void
     */
    private void openWindow(Window window) {
        openWindows.add(window); // Add the window to the tracking list.

        // Add a window listener to remove the window from the list when it is closed.
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                openWindows.remove(window);
            }
        });

        window.setVisible(true); // Make the window visible.
    }

    /**
     * Closes all open child windows that are currently being tracked by the application,
     * ensuring a clean exit when the main application frame closes.
     *
     * @return void
     */
    private void closeAllOpenWindows() {
        // Create a copy to avoid ConcurrentModificationException during iteration.
        List<Window> windowsToClose = new ArrayList<>(openWindows);
        for (Window window : windowsToClose) {
            // Dispose of the window if it's not the main application frame and is displayable.
            if (window != this && window.isDisplayable()) {
                window.dispose();
            }
        }
        openWindows.clear(); // Clear the list after closing all windows.
    }

    /**
     * Returns the currently logged-in {@link User} object.
     *
     * @return The {@link User} object that is currently logged in, or {@code null} if no user is authenticated.
     */
    public User getCurrentUser() {
        return currentUser;
    }
}