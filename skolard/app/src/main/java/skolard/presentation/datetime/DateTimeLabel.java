package skolard.presentation.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class DateTimeLabel extends JLabel implements Runnable {

    // Formatter for displaying the date and time in a specific format.
    private final SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d - hh:mm:ss a");

    /**
     * Constructs a new DateTimeLabel.
     * Initializes the font and starts a new thread to continuously update the time.
     */
    public DateTimeLabel() {
        // Set the font for the label.
        setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));
        // Create a new thread and start it.
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * The run method for the thread, which continuously updates the label's text
     * with the current date and time.
     *
     * @return void
     */
    @Override
    public void run() {
        // Loop indefinitely to continuously update the time.
        while (true) {
            // Set the label's text to the current formatted date and time.
            setText(format.format(new Date()));
            try {
                // Pause the thread for 1 second before the next update.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Break the loop if the thread is interrupted.
                break;
            }
        }
    }
}
