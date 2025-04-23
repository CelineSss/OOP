package gui.panels.resident;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import util.ImageLoader;

public class ResidentViewReceiptPanel extends JPanel {
    private JTextField studentIdField;
    private JTextArea receiptArea;
    private JButton viewButton;
    private JButton backButton;
    private JLabel titleLabel;
    private String filePath;
    private String residentId;

    // Define consistent colors
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color TITLE_COLOR = new Color(60, 72, 107);
    private static final Color BUTTON_COLOR = new Color(100, 141, 174);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color LABEL_COLOR = new Color(90, 103, 140);
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color FIELD_BORDER = new Color(210, 215, 220);

    public ResidentViewReceiptPanel(String residentId) {
        this.residentId = residentId;
        initComponents();
    }

    private void initComponents() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title
        titleLabel = new JLabel("Payment Records");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        studentIdField = createStyledTextField();
        viewButton = createStyledButton("View Receipt", new Dimension(120, 35));
        
        searchPanel.add(studentIdField);
        searchPanel.add(viewButton);

        // Receipt Area
        receiptArea = new JTextArea(15, 40);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        scrollPane.setMaximumSize(new Dimension(500, 400));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        backButton = createStyledButton("Back", new Dimension(120, 35));

        viewButton.addActionListener(evt -> viewReceiptActionPerformed(evt));
        backButton.addActionListener(evt -> backButtonActionPerformed(evt));

        buttonPanel.add(backButton);

        // Add all components
        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(searchPanel);
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

    private void viewReceiptActionPerformed(java.awt.event.ActionEvent evt) {
        String studentIdReceipt = studentIdField.getText().trim();
        
        if(studentIdReceipt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID cannot be empty.");
            return;
        }
        
        filePath = studentIdReceipt + "_receipts.txt";
        File file = new File(filePath);
        
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Error: File does not exist at: " + file.getAbsolutePath());
            receiptArea.setText("");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder receiptContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                receiptContent.append(line).append("\n");
            }
            receiptArea.setText(receiptContent.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Receipt not found for Student ID: " + studentIdReceipt + "\nError:" + e.getMessage());
            receiptArea.setText("");
        }
    }

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).getContentPane().removeAll();
            ((JFrame) window).getContentPane().add(new ResidentDashboardPanel(residentId));
            ((JFrame) window).getContentPane().revalidate();
            ((JFrame) window).getContentPane().repaint();
        }
    }
}