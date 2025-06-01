package skolard.logic;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles the business logic for accessing the FAQ documentation.
 * Ensures that the FAQ HTML properly references the CSS stylesheet for styling.
 */
public class FAQHandler {
    private final String faqPath;
    private final String cssPath;

    public FAQHandler() {
        this("skolard/hugo-faq-site/public/index.html",
                "skolard/hugo-faq-site/public/assets/css/stylesheet.36819bea596090d8b48cf10d9831382996197aa7e4fc86f792f7c08c9ca4d23b.css");
    }

    public FAQHandler(String faqPath) {
        this(faqPath, "skolard/hugo-faq-site/public/assets/css/stylesheet.36819bea596090d8b48cf10d9831382996197aa7e4fc86f792f7c08c9ca4d23b.css");
    }

    public FAQHandler(String faqPath, String cssPath) {
        this.faqPath = faqPath;
        this.cssPath = cssPath;
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

        // Ensure the HTML file properly references the CSS stylesheet
        ensureCSSIntegration(htmlFile);

        // Open the file in the default browser
        desktop.browse(htmlFile.toURI());
        return true;
    }

    /**
     * Ensures that the HTML file properly references the CSS stylesheet
     */
    private void ensureCSSIntegration(File htmlFile) throws IOException {
        Path htmlPath = htmlFile.toPath();
        String content = Files.readString(htmlPath);

        // Check if CSS is already linked
        String cssFileName = Paths.get(cssPath).getFileName().toString();
        String cssLinkTag = "<link rel=\"stylesheet\" href=\"assets/css/" + cssFileName + "\">";

        if (!content.contains(cssLinkTag) && !content.contains("stylesheet")) {
            // Find the </head> tag and insert CSS link before it
            if (content.contains("</head>")) {
                content = content.replace("</head>",
                        "    " + cssLinkTag + "\n</head>");

                // Write the updated content back to the file
                Files.writeString(htmlPath, content);
            }
        }
    }

    public String getFAQPath() {
        return faqPath;
    }

    public String getCSSPath() {
        return cssPath;
    }

    /**
     * Checks if the CSS file exists
     */
    public boolean isCSSAvailable() {
        return new File(cssPath).exists();
    }
}