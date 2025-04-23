package gui.panels.staff;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Date;
import util.ImageLoader;
import util.FileHandler;

public class ReceiptGenerationPanel extends JPanel {
    private JButton backButton;
    private JTextField changeDueField;
    private JButton generateButton;
    private JComboBox<String> paymentMethodCombo;
    private JButton printButton;
    private JTextField receivedAmountField;
    private JButton resetButton;
    private JTextField roomNumberField;
    private JTextField studentIdField;
    private JTextField totalAmountField;
    private JTextArea receiptArea;
    private JLabel titleLabel;
    private FileHandler fileHandler;
    private String staffId;

    // Define consistent colors
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color TITLE_COLOR = new Color(60, 72, 107);
    private static final Color BUTTON_COLOR = new Color(100, 141, 174);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color LABEL_COLOR = new Color(90, 103, 140);
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color FIELD_BORDER = new Color(210, 215, 220);

    public ReceiptGenerationPanel(String staffId) {
        this.staffId = staffId;
        fileHandler = new FileHandler();
        initComponents();
    }

    private void initComponents() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title
        titleLabel = new JLabel("Generate Receipt");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Initialize components
        studentIdField = createStyledTextField();
        roomNumberField = createStyledTextField();
        paymentMethodCombo = createStyledComboBox();
        receivedAmountField = createStyledTextField();
        totalAmountField = createStyledTextField();
        changeDueField = createStyledTextField();
        changeDueField.setEditable(false);

        // Add form fields
        addFormField(formPanel, "Student ID:", studentIdField, gbc, 0);
        addFormField(formPanel, "Room Number:", roomNumberField, gbc, 1);
        addFormField(formPanel, "Payment Method:", paymentMethodCombo, gbc, 2);
        addFormField(formPanel, "Received Amount:", receivedAmountField, gbc, 3);
        addFormField(formPanel, "Total Amount:", totalAmountField, gbc, 4);
        addFormField(formPanel, "Change Due:", changeDueField, gbc, 5);

        // Receipt Area
        receiptArea = new JTextArea(12, 40);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        scrollPane.setMaximumSize(new Dimension(500, 300));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        generateButton = createStyledButton("Generate", new Dimension(120, 35));
        printButton = createStyledButton("Print", new Dimension(120, 35));
        resetButton = createStyledButton("Reset", new Dimension(120, 35));
        backButton = createStyledButton("Back", new Dimension(120, 35));

        generateButton.addActionListener(evt -> GenerateActionPerformed(evt));
        printButton.addActionListener(evt -> PrintActionPerformed(evt));
        resetButton.addActionListener(evt -> ResetActionPerformed(evt));
        backButton.addActionListener(evt -> BackActionPerformed(evt));

        buttonPanel.add(generateButton);
        buttonPanel.add(printButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        // Add all components
        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(formPanel);
        add(Box.createVerticalStrut(15));
        add(scrollPane);
        add(Box.createVerticalStrut(15));
        add(buttonPanel);
        add(Box.createVerticalStrut(10));
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setBackground(FIELD_BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> combo = new JComboBox<>(new String[] {
            "", "Cash", "Online Banking", "Credit Card", "Debit Card", "E-wallet"
        });
        combo.setBackground(FIELD_BACKGROUND);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return combo;
    }

    private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(LABEL_COLOR);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private JButton createStyledButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        
        // Style the button
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    private void GenerateActionPerformed(java.awt.event.ActionEvent evt) {
        if (!validateFields()) {
            return;
        }

        try {
            double received = Double.parseDouble(receivedAmountField.getText());
            double total = Double.parseDouble(totalAmountField.getText());
            double change = received - total;
            
            if (received < total) {
                JOptionPane.showMessageDialog(this, 
                    "Received amount must be greater than or equal to total amount!",
                    "Invalid Amount",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            changeDueField.setText(String.format("%.2f", change));
            generateReceiptText();
            saveReceiptToFiles();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for amounts!",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateFields() {
        if (studentIdField.getText().isEmpty() || 
            roomNumberField.getText().isEmpty() || 
            receivedAmountField.getText().isEmpty() || 
            totalAmountField.getText().isEmpty() ||
            paymentMethodCombo.getSelectedIndex() == 0) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields!",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void generateReceiptText() {
        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append("*******************************************\n");
        receiptBuilder.append("*                APU Hostel Receipt               *\n");
        receiptBuilder.append("*******************************************\n\n");
        
        Date obj = new Date();
        String date = obj.toString();
        
        receiptBuilder.append("Date: ").append(date).append("\n\n");
        receiptBuilder.append("Student ID: ").append(studentIdField.getText()).append("\n");
        receiptBuilder.append("Room Number: ").append(roomNumberField.getText()).append("\n");
        receiptBuilder.append("Payment Method: ").append(paymentMethodCombo.getSelectedItem()).append("\n\n");
        receiptBuilder.append("-------------------------------------------------\n");
        receiptBuilder.append(String.format("Received Amount: RM %.2f\n", Double.parseDouble(receivedAmountField.getText())));
        receiptBuilder.append(String.format("Total Amount: RM %.2f\n", Double.parseDouble(totalAmountField.getText())));
        receiptBuilder.append(String.format("Change Due: RM %.2f\n", Double.parseDouble(changeDueField.getText())));
        receiptBuilder.append("-------------------------------------------------\n\n");
        receiptBuilder.append("Thank you for your payment!\n");
        
        receiptArea.setText(receiptBuilder.toString());
    }

    private void saveReceiptToFiles() {
        String receiptContent = receiptArea.getText();
        String basePath = fileHandler.getBasePath();
        
        // Save to main receipts file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(basePath + "receipts.txt", true))) {
            writer.write(receiptContent);
            writer.write("\n=================================================\n\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving to main receipts file: " + e.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Save to student-specific receipt file
        String studentFileName = basePath + studentIdField.getText() + "_receipts.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentFileName, true))) {
            writer.write(receiptContent);
            writer.write("\n=================================================\n\n");
            JOptionPane.showMessageDialog(this, 
                "Receipt saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving to student receipt file: " + e.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PrintActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            receiptArea.print();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error printing: " + e.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ResetActionPerformed(java.awt.event.ActionEvent evt) {
        studentIdField.setText("");
        roomNumberField.setText("");
        paymentMethodCombo.setSelectedIndex(0);
        receivedAmountField.setText("");
        totalAmountField.setText("");
        changeDueField.setText("");
        receiptArea.setText("");
    }

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).getContentPane().removeAll();
            ((JFrame) window).getContentPane().add(new PaymentManagementPanel(staffId));
            ((JFrame) window).getContentPane().revalidate();
            ((JFrame) window).getContentPane().repaint();
        }
    }
}
