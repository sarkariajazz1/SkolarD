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

    public FAQView(FAQHandler faqHandler) {
        super("Frequently Asked Questions");
        this.faqHandler = faqHandler;

        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Frequently Asked Questions", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        add(createFAQScrollPane(), BorderLayout.CENTER);

        // Add back button panel at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JScrollPane createFAQScrollPane() {
        List<FAQ> faqs = faqHandler.getAllFAQs();

        JPanel faqPanel = new JPanel();
        faqPanel.setLayout(new BoxLayout(faqPanel, BoxLayout.Y_AXIS));
        faqPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (FAQ faq : faqs) {
            JLabel qLabel = new JLabel("<html><b>Q: " + faq.getQuestion() + "</b></html>");
            JLabel aLabel = new JLabel("<html><i>A: " + faq.getAnswer() + "</i></html>");
            qLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            aLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            faqPanel.add(qLabel);
            faqPanel.add(Box.createVerticalStrut(4));
            faqPanel.add(aLabel);
            faqPanel.add(Box.createVerticalStrut(12));
        }

        JScrollPane scrollPane = new JScrollPane(faqPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }
}