package skolard.presentation.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class DateTimeLabel extends JLabel implements Runnable {

    private final SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d yyyy - hh:mm:ss a");

    public DateTimeLabel() {
        setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            setText(format.format(new Date()));
            try {
                Thread.sleep(1000); // Refresh every second
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
