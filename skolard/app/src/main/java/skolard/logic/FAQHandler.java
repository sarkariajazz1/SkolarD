package skolard.logic;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Handles the business logic for accessing the FAQ documentation.
 */
public class FAQHandler {
    private final String faqPath;


    public FAQHandler() {
        this("skolard/hugo-faq-site/public/index.html");
    }


    public FAQHandler(String faqPath) {
        this.faqPath = faqPath;
    }

    public boolean openFAQ() throws IOException {
        File htmlFile = new File(faqPath);
        
        // Check if Desktop is supported
        if (!Desktop.isDesktopSupported()) {
            return false;
        }
        
        Desktop desktop = Desktop.getDesktop();
        
        // Check if the file exists
        if (!htmlFile.exists()) {
            return false;
        }
        
        // Open the file in the default browser
        desktop.browse(htmlFile.toURI());
        return true;
    }
    

    public String getFAQPath() {
        return faqPath;
    }
}