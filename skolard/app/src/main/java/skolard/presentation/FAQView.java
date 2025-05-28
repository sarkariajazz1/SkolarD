package skolard.presentation;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * A class that opens the FAQ website in the default browser.
 */
public class FAQView {

    /**
     * Opens the FAQ website in the default browser.
     */
    public FAQView() {
        try {
            // Get the path to the index.html file
            File htmlFile = new File("skolard/hugo-faq-site/public/index.html");

            // Check if Desktop is supported
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();

                // Check if the file exists
                if (htmlFile.exists()) {
                    // Open the file in the default browser
                    desktop.browse(htmlFile.toURI());
                } else {
                    JOptionPane.showMessageDialog(null,
                        "FAQ file not found: " + htmlFile.getAbsolutePath(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                    "Desktop is not supported on this platform.",
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