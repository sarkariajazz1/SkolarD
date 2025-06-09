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

public class PaymentView extends JDialog {
    private boolean wasPaid = false;
    private boolean saveCardInfo = false;

    public PaymentView(Window parent, PaymentHandler paymentHandler, Student student) {
        super(parent, "Payment", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        // --- Left: Card Table ---
        String[] columnNames = {"Card Number", "Name", "Expiry"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable cardTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(cardTable);

        // Populate saved cards
        List<Card> savedCards = paymentHandler.retrieveRecordedCards(student);
        for (Card card : savedCards) {
            tableModel.addRow(new String[]{maskCardNumber(card.getCardNumber()), card.getName(), card.getExpiry()});
        }

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Saved Cards:"), BorderLayout.NORTH);
        leftPanel.add(tableScroll, BorderLayout.CENTER);

        // --- Right: Input Fields + Save Checkbox ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Enter Card Details"));

        JTextField numberField = new JTextField(16);
        JTextField nameField = new JTextField(16);
        JTextField expiryField = new JTextField(16);
        JTextField cvvField = new JTextField(4);
        JCheckBox saveCardCheckBox = new JCheckBox("Save this card");

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
        JButton payInputButton = new JButton("Pay With Input");

        paySelectedButton.setEnabled(false);

        cardTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            paySelectedButton.setEnabled(cardTable.getSelectedRow() != -1);
        });

        closeButton.addActionListener(e -> {
            wasPaid = false;
            dispose();
        });

        paySelectedButton.addActionListener(e -> {
            int row = cardTable.getSelectedRow();
            if (row >= 0 && row < savedCards.size()) {
                Card card = savedCards.get(row);
                // Use default dummy CVV or store in future
                boolean success = paymentHandler.payWithCard(card.getName(), card.getCardNumber(), card.getExpiry(), "123", false, student);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment successful using saved card.");
                    wasPaid = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Saved card is no longer valid.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        payInputButton.addActionListener(e -> {
            String number = numberField.getText().trim();
            String name = nameField.getText().trim();
            String expiry = expiryField.getText().trim();
            String cvv = cvvField.getText().trim();
            boolean save = saveCardCheckBox.isSelected();

            boolean success = paymentHandler.payWithCard(name, number, expiry, cvv, save, student);
            if (success) {
                JOptionPane.showMessageDialog(this, "Payment successful using entered card.");
                wasPaid = true;
                saveCardInfo = save;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card details. Please check again.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        buttonPanel.add(paySelectedButton);
        buttonPanel.add(payInputButton);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(800, 350);
        setLocationRelativeTo(parent);
    }

    public boolean wasPaid() {
        return wasPaid;
    }

    private String maskCardNumber(String number) {
        if (number.length() >= 4) {
            return "**** **** **** " + number.substring(number.length() - 4);
        }
        return number;
    }
}
