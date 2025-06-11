package skolard.presentation.booking;

import skolard.logic.rating.RatingHandler;
import skolard.logic.session.SessionHandler;
import skolard.logic.booking.BookingHandler;
import skolard.logic.payment.PaymentHandler;
import skolard.objects.Session;
import skolard.objects.Student;
import skolard.presentation.payment.PaymentView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingView extends JFrame {
    // Text field for course name input.
    private final JTextField courseField = new JTextField(15);
    // Text field for preferred session start time.
    private final JTextField startTimeField = new JTextField(16);
    // Text field for preferred session end time.
    private final JTextField endTimeField = new JTextField(16);
    // Dropdown for filtering/sorting session results.
    private final JComboBox<String> filterDropdown = new JComboBox<>(new String[]{
            "", "Sort by Time", "Sort by Tutor Course Grade", "Sort by Overall Tutor Rating"
    });

    // Button to initiate session search.
    private final JButton searchBtn = new JButton("Find Sessions");
    // Button to book a selected session.
    private final JButton bookButton = new JButton("Book");
    // Button to view detailed information about a selected session.
    private final JButton infoButton = new JButton("View Info");
    // Button to navigate back or close the view.
    private final JButton backButton = new JButton("Back");

    // Label to display status messages to the user.
    private final JLabel statusLabel = new JLabel(" ");
    // Label for the preferred time range input section.
    private final JLabel timeRangeLabel = new JLabel("Preferred time range:");
    // Panel containing the time input fields.
    private final JPanel timePanel = new JPanel();

    // Table to display available sessions.
    private final JTable sessionTable;
    // Model for managing data displayed in the sessionTable.
    private final DefaultTableModel tableModel;

    // List to hold the currently displayed session results.
    private List<Session> currentResults;
    // Formatter for consistent date and time string representation.
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Controller handling the business logic for this view.
    private final BookingController controller;
    // Handler for processing payments.
    private final PaymentHandler paymentHandler;
    // Handler for managing session data and operations.
    private final SessionHandler sessionHandler;
    // Handler for managing tutor ratings.
    private final RatingHandler ratingHandler;
    // The currently logged-in student.
    private final Student student;

    /**
     * Constructs a new BookingView window for students to find and book sessions.
     * Initializes the UI components and sets up necessary handlers for business logic.
     *
     * @param bookingHandler The handler for booking-related operations.
     * @param sessionHandler The handler for session-related operations.
     * @param ratingHandler The handler for managing ratings.
     * @param paymentHandler The handler for processing payments.
     * @param student The current student user.
     * @throws NullPointerException if any of the handler or student parameters are null.
     */
    public BookingView(BookingHandler bookingHandler, SessionHandler sessionHandler,
                       RatingHandler ratingHandler, PaymentHandler paymentHandler, Student student) {

        super("SkolarD - Booking View");
        this.paymentHandler = paymentHandler;
        this.sessionHandler = sessionHandler;
        this.ratingHandler = ratingHandler;
        this.student = student;
        this.controller = new BookingController(this, bookingHandler, sessionHandler, ratingHandler, paymentHandler, student);

        // Configure JFrame properties.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Define column headers for the session table.
        String[] columnNames = {"Tutor", "Start Time", "End Time"};
        // Initialize the table model with column names and no data.
        tableModel = new DefaultTableModel(columnNames, 0);
        // Initialize the table with the created model.
        sessionTable = new JTable(tableModel);
        // Allow only one row to be selected at a time.
        sessionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Setup various UI panels.
        setupInputPanel();
        setupTablePanel();
        setupButtonPanel();
        setupListeners();

        // Adjust window size to fit components.
        pack();
        // Center the window on the screen.
        setLocationRelativeTo(null);
        // Make the window visible.
        setVisible(true);
    }

    /**
     * Sets up the input panel containing course search, time range filters, and the search button.
     * This panel is placed at the top (NORTH) of the JFrame's BorderLayout.
     *
     * @return void
     */
    private void setupInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(new JLabel("Course:"));
        topRow.add(courseField);
        topRow.add(searchBtn);

        JComboBox<String> filterDropdown = new JComboBox<>(new String[] {
                "", "Sort by Time", "Sort by Tutor Course Grade", "Sort by Overall Tutor Rating"
        });
        topRow.add(filterDropdown);

        inputPanel.add(topRow);

        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        JPanel timeFieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeFieldsRow.add(new JLabel("Start:"));
        timeFieldsRow.add(startTimeField);
        timeFieldsRow.add(new JLabel("End:"));
        timeFieldsRow.add(endTimeField);

        JLabel timeExample = new JLabel("Format Example: 2025-06-01 09:00");

        timePanel.add(timeRangeLabel);
        timePanel.add(timeFieldsRow);
        timePanel.add(timeExample);

        timePanel.setVisible(false);
        timeRangeLabel.setVisible(false);

        inputPanel.add(timePanel);
        add(inputPanel, BorderLayout.NORTH);
    }

    /**
     * Sets up the panel containing the JTable for displaying session results.
     * This panel is placed in the center (CENTER) of the JFrame's BorderLayout.
     *
     * @return void
     */
    private void setupTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(statusLabel);
        tablePanel.add(new JScrollPane(sessionTable));
        add(tablePanel, BorderLayout.CENTER);
    }

    /**
     * Sets up the button panel at the bottom (SOUTH) of the JFrame,
     * including 'Back', 'Book', and 'View Info' buttons, and
     * configures their action listeners.
     *
     * @return void
     */
    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bookButton.setEnabled(false);
        infoButton.setEnabled(false);

        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(infoButton);
        add(buttonPanel, BorderLayout.SOUTH);

        bookButton.addActionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < currentResults.size()) {
                Session session = currentResults.get(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Do you want to book this session with " + session.getTutor().getName() + "? Pre-payment is required.",
                        "Confirm Booking",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // Show payment window.
                    PaymentView paymentDialog = new PaymentView(SwingUtilities.getWindowAncestor(this), paymentHandler, student);
                    paymentDialog.setVisible(true);

                    if (paymentDialog.wasPaid()) {
                        // Book the session and create a rating request.
                        sessionHandler.bookASession(student, session.getSessionId());
                        ratingHandler.createRatingRequest(session, student);

                        // Update table and buttons after booking.
                        currentResults.remove(selectedRow);
                        tableModel.removeRow(selectedRow);
                        sessionTable.clearSelection();
                        bookButton.setEnabled(false);
                        infoButton.setEnabled(false);

                        JOptionPane.showMessageDialog(this,
                                "Session booked successfully! Payment will be finalized and rating survey will open after session ends at " + session.getEndDateTime(),
                                "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Payment was not completed. Session not booked.",
                                "Payment Cancelled", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        infoButton.addActionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < currentResults.size()) {
                Session session = currentResults.get(selectedRow);
                showSessionDetailsPopup(session);
            }
        });

        backButton.addActionListener(e -> dispose());
    }

    /**
     * Sets up event listeners for interactive UI components, such as the filter dropdown,
     * search button, and session table selection.
     *
     * @return void
     */
    private void setupListeners() {
        filterDropdown.addActionListener(e -> {
            // Toggle visibility of time input fields based on filter selection.
            boolean show = "Sort by Time".equals(filterDropdown.getSelectedItem());
            timePanel.setVisible(show);
            timeRangeLabel.setVisible(show);
        });

        searchBtn.addActionListener(e ->
                // Trigger the search in the controller.
                controller.onSearch(courseField.getText(), (String) filterDropdown.getSelectedItem(),
                        startTimeField.getText(), endTimeField.getText()));


        infoButton.addActionListener(e -> controller.onViewInfo(sessionTable.getSelectedRow()));

        sessionTable.getSelectionModel().addListSelectionListener(e -> {
            // Enable/disable buttons based on table row selection.
            boolean valid = !e.getValueIsAdjusting() && sessionTable.getSelectedRow() >= 0;
            bookButton.setEnabled(valid);
            infoButton.setEnabled(valid);
        });
    }

    /**
     * Displays a detailed popup message for a given session, showing tutor, time, course, and bio information.
     *
     * @param session The {@link Session} object whose details are to be displayed.
     * @return void
     */
    private void showSessionDetailsPopup(Session session) {
        // Format message for display.
        String message = String.format(
                "<html><b>Tutor:</b> %s<br><b>Email:</b> %s<br><b>Start:</b> %s<br><b>End:</b> %s<br><b>Course:</b> %s<br><b>Bio:</b> %s<br><b>Tutor course grade:</b> %s</html>",
                session.getTutor().getName(),
                session.getTutor().getEmail(),
                session.getStartDateTime().format(formatter),
                session.getEndDateTime().format(formatter),
                session.getCourseName(),
                session.getTutor().getBio(),
                session.getTutor().getGradeForCourse(session.getCourseName())
        );
        JOptionPane.showMessageDialog(this, message, "Session Details", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates the session table with a new list of sessions. Clears any existing rows
     * and populates the table with data from the provided list. It also resets button states
     * and updates the status label.
     *
     * @param sessions A {@link List} of {@link Session} objects to display in the table.
     * @return void
     */
    public void updateSessionTable(List<Session> sessions) {
        tableModel.setRowCount(0);
        currentResults = sessions;

        // Add each session to the table.
        for (Session session : sessions) {
            Object[] row = {
                    session.getTutor().getName(),
                    session.getStartDateTime().format(formatter),
                    session.getEndDateTime().format(formatter)
            };
            tableModel.addRow(row);
        }

        sessionTable.clearSelection();
        bookButton.setEnabled(false);
        infoButton.setEnabled(false);

        // Update status message.
        if (sessions.isEmpty()) {
            showStatus("No sessions found for the specified criteria.");
        } else {
            showStatus("Found " + sessions.size() + " available session(s).");
        }
    }

    /**
     * Removes a session row from the displayed table and its corresponding entry
     * from the internal {@code currentResults} list.
     *
     * @param rowIndex The index of the row to be removed from the table.
     * @return void
     */
    public void removeSessionFromTable(int rowIndex) {
        if (currentResults != null && rowIndex >= 0 && rowIndex < currentResults.size()) {
            // Remove from list and table model.
            currentResults.remove(rowIndex);
            tableModel.removeRow(rowIndex);
        }
    }

    /**
     * Retrieves the {@link Session} object corresponding to a given row index in the displayed table.
     *
     * @param rowIndex The index of the row in the table.
     * @return The {@link Session} object at the specified row index, or {@code null} if the index is out of bounds or results are not loaded.
     */
    public Session getSelectedSession(int rowIndex) {
        if (currentResults != null && rowIndex >= 0 && rowIndex < currentResults.size()) {
            return currentResults.get(rowIndex);
        }
        return null;
    }

    /**
     * Displays a status message in the dedicated status label on the UI.
     *
     * @param message The string message to be displayed.
     * @return void
     */
    public void showStatus(String message) {
        statusLabel.setText(message);
    }

    /**
     * Displays detailed information about a session in a message dialog.
     * This method is functionally very similar to {@link #showSessionDetailsPopup(Session)}.
     *
     * @param session The {@link Session} object whose details are to be displayed.
     * @return void
     */
    public void showSessionDetails(Session session) {
        // Format message for display.
        String msg = String.format(
                "<html><b>Tutor:</b> %s<br><b>Email:</b> %s<br><b>Start:</b> %s<br><b>End:</b> %s<br><b>Course:</b> %s<br><b>Bio:</b> %s<br><b>Grade:</b> %s</html>",
                session.getTutor().getName(),
                session.getTutor().getEmail(),
                session.getStartDateTime().format(formatter),
                session.getEndDateTime().format(formatter),
                session.getCourseName(),
                session.getTutor().getBio(),
                session.getTutor().getGradeForCourse(session.getCourseName())
        );
        JOptionPane.showMessageDialog(this, msg, "Session Details", JOptionPane.INFORMATION_MESSAGE);
    }
}