
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

    private final FAQHandler faqHandler;
    private JScrollPane scrollPane;
    private JPanel faqPanel;
    private JButton backButton;
    private JLabel header;

    public FAQView(FAQHandler faqHandler) {
        super("Frequently Asked Questions");
        this.faqHandler = faqHandler;

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        header = new JLabel("Frequently Asked Questions", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        add(createFAQScrollPane(), BorderLayout.CENTER);

        // Add back button panel at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupComponentNames();
        setVisible(true);
    }

    /**
     * Set component names for testing purposes.
     */
    private void setupComponentNames() {
        header.setName("headerLabel");
        backButton.setName("backButton");
        scrollPane.setName("faqScrollPane");
        faqPanel.setName("faqPanel");
    }

    private JScrollPane createFAQScrollPane() {
        List<FAQ> faqs = faqHandler.getAllFAQs();

        faqPanel = new JPanel();
        faqPanel.setLayout(new BoxLayout(faqPanel, BoxLayout.Y_AXIS));
        faqPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int index = 0;
        for (FAQ faq : faqs) {
            JLabel qLabel = new JLabel("<html><b>Q: " + faq.getQuestion() + "</b></html>");
            JLabel aLabel = new JLabel("<html><i>A: " + faq.getAnswer() + "</i></html>");
            qLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            aLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Set names for testing - use index since FAQ doesn't have ID
            qLabel.setName("question_" + index);
            aLabel.setName("answer_" + index);

            faqPanel.add(qLabel);
            faqPanel.add(Box.createVerticalStrut(4));
            faqPanel.add(aLabel);
            faqPanel.add(Box.createVerticalStrut(12));

            index++;
        }

        scrollPane = new JScrollPane(faqPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }
}