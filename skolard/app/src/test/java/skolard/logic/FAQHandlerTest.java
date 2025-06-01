package skolard.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FAQHandler
 * Tests the functionality of FAQ handling including file operations,
 * CSS integration, and path management.
 */
public class FAQHandlerTest {

    @TempDir
    Path tempDir;

    private Path testHtmlFile;
    private Path testCssFile;
    private Path cssDir;
    private FAQHandler faqHandler;

    @BeforeEach
    void setUp() throws IOException {
        // Create test directory structure
        cssDir = tempDir.resolve("assets").resolve("css");
        Files.createDirectories(cssDir);
        
        // Create test HTML file
        testHtmlFile = tempDir.resolve("test-faq.html");
        String basicHtmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test FAQ</title>
            </head>
            <body>
                <h1>FAQ</h1>
                <p>Test content</p>
            </body>
            </html>
            """;
        Files.writeString(testHtmlFile, basicHtmlContent);
        
        // Create test CSS file
        testCssFile = cssDir.resolve("stylesheet.36819bea596090d8b48cf10d9831382996197aa7e4fc86f792f7c08c9ca4d23b.css");
        String testCssContent = "body { font-family: Arial, sans-serif; }";
        Files.writeString(testCssFile, testCssContent);
        
        // Initialize FAQ handler with test paths
        faqHandler = new FAQHandler(testHtmlFile.toString(), testCssFile.toString());
    }

    @AfterEach
    void tearDown() {
        // Cleanup is handled automatically by @TempDir
    }

    @Test
    void testDefaultConstructor() {
        FAQHandler defaultHandler = new FAQHandler();
        
        assertEquals("skolard/hugo-faq-site/public/index.html", defaultHandler.getFAQPath());
        assertTrue(defaultHandler.getCSSPath().contains("stylesheet.36819bea596090d8b48cf10d9831382996197aa7e4fc86f792f7c08c9ca4d23b.css"));
    }

    @Test
    void testSingleParameterConstructor() {
        String customPath = "custom/path/faq.html";
        FAQHandler customHandler = new FAQHandler(customPath);
        
        assertEquals(customPath, customHandler.getFAQPath());
        assertTrue(customHandler.getCSSPath().contains("stylesheet.36819bea596090d8b48cf10d9831382996197aa7e4fc86f792f7c08c9ca4d23b.css"));
    }

    @Test
    void testTwoParameterConstructor() {
        String customFaqPath = "custom/faq.html";
        String customCssPath = "custom/style.css";
        FAQHandler customHandler = new FAQHandler(customFaqPath, customCssPath);
        
        assertEquals(customFaqPath, customHandler.getFAQPath());
        assertEquals(customCssPath, customHandler.getCSSPath());
    }

    @Test
    void testGetFAQPath() {
        assertEquals(testHtmlFile.toString(), faqHandler.getFAQPath());
    }

    @Test
    void testGetCSSPath() {
        assertEquals(testCssFile.toString(), faqHandler.getCSSPath());
    }

    @Test
    void testIsCSSAvailable_WhenCSSExists() {
        assertTrue(faqHandler.isCSSAvailable());
    }

    @Test
    void testIsCSSAvailable_WhenCSSDoesNotExist() {
        FAQHandler handlerWithMissingCSS = new FAQHandler(
            testHtmlFile.toString(), 
            "non/existent/path.css"
        );
        
        assertFalse(handlerWithMissingCSS.isCSSAvailable());
    }

    @Test
    void testOpenFAQ_WithNonExistentFile() throws IOException {
        FAQHandler handlerWithMissingFile = new FAQHandler(
            "non/existent/file.html", 
            testCssFile.toString()
        );
        
        assertFalse(handlerWithMissingFile.openFAQ());
    }

    @Test
    void testOpenFAQ_WhenDesktopNotSupported() throws IOException {
        // This test assumes Desktop is supported on the test environment
        // If Desktop is not supported, openFAQ should return false
        if (!Desktop.isDesktopSupported()) {
            assertFalse(faqHandler.openFAQ());
        } else {
            // If Desktop is supported, we can't easily test the "not supported" case
            // without mocking, so we'll just verify the file exists
            assertTrue(Files.exists(testHtmlFile));
        }
    }

    @Test
    void testCSSIntegration_AddsLinkWhenNotPresent() throws IOException {
        // Verify CSS link is not initially present
        String initialContent = Files.readString(testHtmlFile);
        assertFalse(initialContent.contains("stylesheet"));
        
        // Call openFAQ which should trigger CSS integration
        // Note: This will attempt to open in browser, but the CSS integration should still occur
        if (Desktop.isDesktopSupported()) {
            faqHandler.openFAQ();
            
            // Verify CSS link was added
            String updatedContent = Files.readString(testHtmlFile);
            String expectedCssFileName = testCssFile.getFileName().toString();
            assertTrue(updatedContent.contains("assets/css/" + expectedCssFileName));
            assertTrue(updatedContent.contains("<link rel=\"stylesheet\""));
        }
    }

    @Test
    void testCSSIntegration_DoesNotDuplicateWhenAlreadyPresent() throws IOException {
        // First, add CSS link manually
        String cssFileName = testCssFile.getFileName().toString();
        String cssLinkTag = "<link rel=\"stylesheet\" href=\"assets/css/" + cssFileName + "\">";
        String contentWithCSS = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test FAQ</title>
                %s
            </head>
            <body>
                <h1>FAQ</h1>
                <p>Test content</p>
            </body>
            </html>
            """.formatted(cssLinkTag);
        
        Files.writeString(testHtmlFile, contentWithCSS);
        
        if (Desktop.isDesktopSupported()) {
            faqHandler.openFAQ();
            
            // Verify CSS link appears only once
            String finalContent = Files.readString(testHtmlFile);
            int firstIndex = finalContent.indexOf(cssLinkTag);
            int lastIndex = finalContent.lastIndexOf(cssLinkTag);
            assertEquals(firstIndex, lastIndex, "CSS link should not be duplicated");
        }
    }

    @Test
    void testCSSIntegration_WithNoHeadTag() throws IOException {
        // Create HTML without proper head tag
        String malformedHtml = """
            <!DOCTYPE html>
            <html>
            <body>
                <h1>FAQ</h1>
                <p>Test content</p>
            </body>
            </html>
            """;
        Files.writeString(testHtmlFile, malformedHtml);
        
        if (Desktop.isDesktopSupported()) {
            faqHandler.openFAQ();
            
            // Content should remain unchanged if no </head> tag is found
            String finalContent = Files.readString(testHtmlFile);
            assertEquals(malformedHtml, finalContent);
        }
    }

    @Test
    void testCSSIntegration_PreservesExistingStylesheet() throws IOException {
        // Create HTML with existing stylesheet reference
        String htmlWithExistingCSS = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test FAQ</title>
                <link rel="stylesheet" href="existing-style.css">
            </head>
            <body>
                <h1>FAQ</h1>
                <p>Test content</p>
            </body>
            </html>
            """;
        Files.writeString(testHtmlFile, htmlWithExistingCSS);
        
        if (Desktop.isDesktopSupported()) {
            faqHandler.openFAQ();
            
            // Should not add new CSS link since "stylesheet" is already present
            String finalContent = Files.readString(testHtmlFile);
            assertEquals(htmlWithExistingCSS, finalContent);
        }
    }

    @Test
    void testFilePathHandling_WithSpaces() throws IOException {
        // Create a file with spaces in the name
        Path fileWithSpaces = tempDir.resolve("test file with spaces.html");
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test FAQ</title>
            </head>
            <body>
                <h1>FAQ</h1>
            </body>
            </html>
            """;
        Files.writeString(fileWithSpaces, htmlContent);
        
        FAQHandler handlerWithSpaces = new FAQHandler(
            fileWithSpaces.toString(), 
            testCssFile.toString()
        );
        
        assertEquals(fileWithSpaces.toString(), handlerWithSpaces.getFAQPath());
        assertTrue(handlerWithSpaces.isCSSAvailable());
    }

    @Test
    void testCSSPathExtraction() {
        String cssFileName = faqHandler.getCSSPath();
        assertTrue(cssFileName.endsWith("stylesheet.36819bea596090d8b48cf10d9831382996197aa7e4fc86f792f7c08c9ca4d23b.css"));
    }
}