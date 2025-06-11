package skolard.presentation.faq;

import skolard.logic.faq.FAQHandler;
import skolard.objects.FAQ;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * In-app FAQ view panel.
 * Displays a scrollable list of questions and answers.
 */
public class FAQView extends JFrame {

    // Handler for retrieving FAQ data.
    private final FAQHandler faqHandler;
    // Scroll pane to make the FAQ content scrollable.
    private JScrollPane scrollPane;
    // Panel to hold individual FAQ question and answer pairs.
    private JPanel faqPanel;
    // Button to navigate back or close the FAQ view.
    private JButton backButton;
    // Label for the header/title of the FAQ view.
    private JLabel header;

    /**
     * Constructs a new FAQView window.
     * Initializes the UI components and populates them with FAQ data.
     *
     * @param faqHandler The handler responsible for providing FAQ data.
     */
    public FAQView(FAQHandler faqHandler) {
        super("Frequently Asked Questions");
        this.faqHandler = faqHandler;

        // Set the initial size of the frame.
        setSize(500, 400);
        // Center the frame on the screen.
        setLocationRelativeTo(null);
        // Set the default close operation for the frame.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Set the layout manager for the frame.
        setLayout(new BorderLayout());

        // Initialize and configure the header label.
        header = new JLabel("Frequently Asked Questions", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        // Create and add the scrollable FAQ panel to the center.
        add(createFAQScrollPane(), BorderLayout.CENTER);

        // Create a panel for the back button and add it to the bottom.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back");
        // Add an action listener to the back button to close the frame.
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set component names, useful for UI testing.
        setupComponentNames();
        // Make the frame visible.
        setVisible(true);
    }

    /**
     * Sets component names for testing purposes.
     * These names can be used by automated UI testing frameworks to identify components.
     *
     * @return void
     */
    private void setupComponentNames() {
        header.setName("headerLabel");
        backButton.setName("backButton");
        scrollPane.setName("faqScrollPane");
        faqPanel.setName("faqPanel");
    }

    /**
     * Creates and populates a JScrollPane with the list of FAQs.
     * Each FAQ question and answer pair is displayed as a JLabel.
     *
     * @return A {@link JScrollPane} containing the FAQ content.
     */
    private JScrollPane createFAQScrollPane() {
        // Retrieve all FAQs from the handler.
        List<FAQ> faqs = faqHandler.getAllFAQs();

        // Initialize the panel that will hold the FAQs.
        faqPanel = new JPanel();
        // Set the layout for the FAQ panel to be vertical.
        faqPanel.setLayout(new BoxLayout(faqPanel, BoxLayout.Y_AXIS));
        // Add empty border for padding.
        faqPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize an index for naming FAQ components (for testing).
        int index = 0;
        // Iterate through each FAQ and create labels for question and answer.
        for (FAQ faq : faqs) {
            // Create labels for the question and answer, using HTML for formatting.
            JLabel qLabel = new JLabel("<html><b>Q: " + faq.getQuestion() + "</b></html>");
            JLabel aLabel = new JLabel("<html><i>A: " + faq.getAnswer() + "</i></html>");
            // Align labels to the left.
            qLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            aLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Set names for testing, using the index as part of the name.
            qLabel.setName("question_" + index);
            aLabel.setName("answer_" + index);

            // Add the question, a vertical strut for spacing, the answer, and another strut to the panel.
            faqPanel.add(qLabel);
            faqPanel.add(Box.createVerticalStrut(4));
            faqPanel.add(aLabel);
            faqPanel.add(Box.createVerticalStrut(12));

            // Increment the index for the next FAQ.
            index++;
        }

        // Create a scroll pane and add the FAQ panel to it.
        scrollPane = new JScrollPane(faqPanel);
        // Ensure the vertical scrollbar is always visible.
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }
}