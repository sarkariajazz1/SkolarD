package skolard.presentation.payment;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.JCheckBox;
import javax.swing.event.ListSelectionEvent;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import java.util.List;

import skolard.objects.Card;
import skolard.objects.Student;
import skolard.logic.payment.PaymentHandler;

/**
 * A dialog window for handling payment transactions.
 * It allows users to pay using either a saved card or by entering new card details.
 */
public class PaymentView extends JDialog {
    // Flag to indicate if the payment was successful.
    private boolean wasPaid = false;

    /**
     * Constructs a new PaymentView dialog.
     *
     * @param parent The parent {@link Window} of this dialog.
     * @param paymentHandler The {@link PaymentHandler} instance to process payment logic.
     * @param student The {@link Student} object for whom the payment is being made.
     */
    public PaymentView(Window parent, PaymentHandler paymentHandler, Student student) {
        // Call the super constructor to create a modal dialog with a title.
        super(parent, "Payment", ModalityType.APPLICATION_MODAL);
        // Set the layout for the dialog.
        setLayout(new BorderLayout());

        // --- Left Panel: Saved Card Table ---
        // Define column names for the card table.
        String[] columnNames = {"Card Number", "Name", "Expiry"};
        // Create a default table model.
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        // Create the JTable using the table model.
        JTable cardTable = new JTable(tableModel);
        // Add the table to a scroll pane to enable scrolling if many cards are present.
        JScrollPane tableScroll = new JScrollPane(cardTable);

        // Populate the table with saved cards for the current student.
        List<Card> savedCards = paymentHandler.retrieveRecordedCards(student);
        for (Card card : savedCards) {
            // Add a row to the table, masking the card number for security.
            tableModel.addRow(new String[]{maskCardNumber(card.getCardNumber()), card.getName(), card.getExpiry()});
        }

        // Create and configure the left panel to hold the saved cards table.
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Saved Cards:"), BorderLayout.NORTH);
        leftPanel.add(tableScroll, BorderLayout.CENTER);

        // --- Right Panel: New Card Input Fields + Save Checkbox ---
        JPanel rightPanel = new JPanel();
        // Set layout to BoxLayout for vertical arrangement.
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        // Add a titled border for visual grouping.
        rightPanel.setBorder(BorderFactory.createTitledBorder("Enter Card Details"));

        // Initialize text fields for card details.
        JTextField numberField = new JTextField(16);
        JTextField nameField = new JTextField(16);
        JTextField expiryField = new JTextField(16);
        JTextField cvvField = new JTextField(4);
        // Checkbox to allow saving the entered card.
        JCheckBox saveCardCheckBox = new JCheckBox("Save this card");

        // Add labels and text fields to the right panel.
        rightPanel.add(new JLabel("Card Number:"));
        rightPanel.add(numberField);
        rightPanel.add(new JLabel("Cardholder Name:"));
        rightPanel.add(nameField);
        rightPanel.add(new JLabel("Expiry Date (MM/YY):"));
        rightPanel.add(expiryField);
        rightPanel.add(new JLabel("CVV:"));
        rightPanel.add(cvvField);
        rightPanel.add(saveCardCheckBox);

        // --- Buttons ---
        JButton closeButton = new JButton("Close");
        JButton paySelectedButton = new JButton("Pay Selected Card");
        JButton payInputButton = new JButton("Pay With Card");

        // Initially disable the "Pay Selected Card" button as no card is selected.
        paySelectedButton.setEnabled(false);

        // Add a listener to the card table selection model.
        cardTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            // Enable the "Pay Selected Card" button only if a row is selected.
            paySelectedButton.setEnabled(cardTable.getSelectedRow() != -1);
        });

        // Add action listener for the "Close" button.
        closeButton.addActionListener(e -> {
            wasPaid = false; // Set payment status to false.
            dispose(); // Close the dialog.
        });

        // Add action listener for the "Pay Selected Card" button.
        paySelectedButton.addActionListener(e -> {
            int row = cardTable.getSelectedRow();
            if (row >= 0 && row < savedCards.size()) {
                Card card = savedCards.get(row);
                // Attempt payment using the selected saved card.
                // Note: CVV is hardcoded as "123" assuming it's not stored or validated in this dummy example.
                boolean success = paymentHandler.payWithCard(card.getName(), card.getCardNumber(), card.getExpiry(), "123", false, student);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment successful using saved card.");
                    wasPaid = true; // Set payment status to true.
                    dispose(); // Close the dialog.
                } else {
                    JOptionPane.showMessageDialog(this, "Saved card is no longer valid.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener for the "Pay With Card" button (using entered details).
        payInputButton.addActionListener(e -> {
            String number = numberField.getText().trim();
            String name = nameField.getText().trim();
            String expiry = expiryField.getText().trim();
            String cvv = cvvField.getText().trim();
            boolean save = saveCardCheckBox.isSelected(); // Check if the user wants to save the card.

            // Attempt payment using the entered card details.
            boolean success = paymentHandler.payWithCard(name, number, expiry, cvv, save, student);
            if (success) {
                JOptionPane.showMessageDialog(this, "Payment successful using entered card.");
                wasPaid = true; // Set payment status to true.
                dispose(); // Close the dialog.
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card details. Please check again.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Create a panel for the action buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        buttonPanel.add(paySelectedButton);
        buttonPanel.add(payInputButton);

        // Add the main panels to the dialog.
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set the preferred size of the dialog.
        setSize(800, 350);
        // Center the dialog relative to its parent window.
        setLocationRelativeTo(parent);
    }

    /**
     * Returns whether the payment operation was successful.
     * This method can be called by the parent window after the dialog is closed
     * to check the outcome of the payment attempt.
     *
     * @return {@code true} if the payment was successful, {@code false} otherwise.
     */
    public boolean wasPaid() {
        return wasPaid;
    }

    /**
     * Masks the credit card number, showing only the last four digits.
     *
     * @param number The full credit card number string.
     * @return A masked string of the credit card number (e.g., "**** **** **** 1234").
     */
    private String maskCardNumber(String number) {
        if (number != null && number.length() >= 4) {
            return "**** **** **** " + number.substring(number.length() - 4);
        }
        return number; // Return as is if less than 4 digits or null
    }
}