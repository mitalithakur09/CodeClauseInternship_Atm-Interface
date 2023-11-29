import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMInterface extends JFrame {
    private JTextField cardNumberField, pinField;
    private JTextArea transactionArea;
    private JButton balanceButton, withdrawButton, depositButton, exitButton;

    private DatabaseManager databaseManager;

    public ATMInterface() {
        // GUI components
        cardNumberField = new JTextField(20);
        pinField = new JTextField(4);
        transactionArea = new JTextArea(10, 30);
        balanceButton = new JButton("Balance Inquiry");
        withdrawButton = new JButton("Cash Withdrawal");
        depositButton = new JButton("Deposit");
        exitButton = new JButton("Exit");

        //layout
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Card Number:"));
        inputPanel.add(cardNumberField);
        inputPanel.add(new JLabel("PIN:"));
        inputPanel.add(pinField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));
        add(inputPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(transactionArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(balanceButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        //database connection
        databaseManager = new DatabaseManager();

        //adding action listeners
        balanceButton.addActionListener(e -> displayBalance());

        withdrawButton.addActionListener(e -> withdrawCash());

        depositButton.addActionListener(e -> depositCash());

        exitButton.addActionListener(e -> exit());

        //setting frame properties
        setTitle("ATM Interface");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void displayBalance() {
        //fetching and displaying balance from the database
        String cardNumber = cardNumberField.getText();
        String pin = pinField.getText();

        double balance = databaseManager.getBalance(cardNumber, pin);

        if (balance != -1) {
            transactionArea.append("Balance: $" + balance + "\n");
        } else {
            transactionArea.append("Invalid card number or PIN.\n");
        }
    }

    private void withdrawCash() {
        //cash withdrawal
        String cardNumber = cardNumberField.getText();
        String pin = pinField.getText();

        double currentBalance = databaseManager.getBalance(cardNumber, pin);

        if (currentBalance != -1) {
            String withdrawalInput = JOptionPane.showInputDialog("Enter withdrawal amount:");

            if (withdrawalInput != null && !withdrawalInput.isEmpty()) {
                double withdrawalAmount = Double.parseDouble(withdrawalInput);

                if (currentBalance >= withdrawalAmount) {
                    databaseManager.updateBalance(cardNumber, pin, currentBalance - withdrawalAmount);
                    transactionArea.append("Withdrawal successful. Remaining balance: $" + (currentBalance - withdrawalAmount) + "\n");
                } else {
                    transactionArea.append("Insufficient funds.\n");
                }
            } else {
                transactionArea.append("Invalid withdrawal amount.\n");
            }
        } else {
            transactionArea.append("Invalid card number or PIN.\n");
        }
    }

    private void depositCash() {
        //cash deposit
        String cardNumber = cardNumberField.getText();
        String pin = pinField.getText();

        double currentBalance = databaseManager.getBalance(cardNumber, pin);

        if (currentBalance != -1) {
            String depositInput = JOptionPane.showInputDialog("Enter deposit amount:");

            if (depositInput != null && !depositInput.isEmpty()) {
                double depositAmount = Double.parseDouble(depositInput);
                databaseManager.updateBalance(cardNumber, pin, currentBalance + depositAmount);
                transactionArea.append("Deposit successful. New balance: $" + (currentBalance + depositAmount) + "\n");
            } else {
                transactionArea.append("Invalid deposit amount.\n");
            }
        } else {
            transactionArea.append("Invalid card number or PIN.\n");
        }
    }

    private void exit() {
        databaseManager.closeConnection();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMInterface::new);
    }
}
