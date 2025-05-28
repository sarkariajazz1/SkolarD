package skolard.presentation;

import java.io.IOException;
import javax.swing.JOptionPane;

import skolard.logic.FAQHandler;


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
            boolean success = faqHandler.openFAQ();

            if (!success) {
                JOptionPane.showMessageDialog(null,
                    "FAQ file not found: " + faqHandler.getFAQPath(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                "Error opening FAQ: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
