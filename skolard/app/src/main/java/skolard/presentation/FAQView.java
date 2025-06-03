
package skolard.presentation;

import java.io.IOException;

import javax.swing.JOptionPane;

import skolard.logic.faq.FAQHandler;

/**
 * View class for displaying the FAQ documentation.
 * Handles opening the FAQ in the default browser with proper styling.
 */
public class FAQView {
    private final FAQHandler faqHandler;

    public FAQView() {
        this(new FAQHandler());
    }

    public FAQView(FAQHandler faqHandler) {
        this.faqHandler = faqHandler;
        openFAQ();
    }

    private void openFAQ() {
        try {
            // Check if CSS is available for better styling
            if (!faqHandler.isCSSAvailable()) {
                // Show warning but continue
                JOptionPane.showMessageDialog(null,
                        "Note: CSS stylesheet not found. FAQ will display with basic styling.\n" +
                                "Expected location: " + faqHandler.getCSSPath(),
                        "Styling Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

            boolean success = faqHandler.openFAQ();

            if (!success) {
                JOptionPane.showMessageDialog(null,
                        "FAQ file not found: " + faqHandler.getFAQPath() + "\n\n" +
                                "Please ensure the FAQ documentation has been generated.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error opening FAQ: " + e.getMessage() + "\n\n" +
                            "This might be due to:\n" +
                            "• File access permissions\n" +
                            "• Missing browser configuration\n" +
                            "• Corrupted FAQ files",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}