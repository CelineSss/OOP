/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.panels.staff;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import util.ImageLoader;
import util.FileHandler;
import java.io.*;

public class PaymentManagementPanel extends JPanel {
    private JTable jTable1;
    private JTextField Search;
    private JButton backButton;
    private JButton changeStatusButton;
    private JButton generateReceiptButton;
    private JLabel titleLabel;
    private JScrollPane tableScrollPane;
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

    public PaymentManagementPanel(String staffId) {
        this.staffId = staffId;
        fileHandler = new FileHandler();
        initComponents();
        setupSearchBar();
        loadTableData();
    }

    private void initComponents() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title
        titleLabel = new JLabel("Payment Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(BACKGROUND_COLOR);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(LABEL_COLOR);
        Search = createStyledTextField();
        searchPanel.add(searchLabel);
        searchPanel.add(Search);

        // Table
        initializeTable();
        tableScrollPane = new JScrollPane(jTable1);
        tableScrollPane.setPreferredSize(new Dimension(750, 350));
        tableScrollPane.setMaximumSize(new Dimension(900, 400));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        changeStatusButton = createStyledButton("Change Status", new Dimension(150, 35));
        generateReceiptButton = createStyledButton("Generate Receipt", new Dimension(150, 35));
        backButton = createStyledButton("Back", new Dimension(150, 35));

        changeStatusButton.addActionListener(evt -> ChangeStatusButtonActionPerformed(evt));
        generateReceiptButton.addActionListener(evt -> GenerateReceiptActionPerformed(evt));
        backButton.addActionListener(evt -> BackActionPerformed(evt));

        buttonPanel.add(changeStatusButton);
        buttonPanel.add(generateReceiptButton);
        buttonPanel.add(backButton);

        // Add all components
        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(15));
        add(searchPanel);
        add(Box.createVerticalStrut(10));
        add(tableScrollPane);
        add(Box.createVerticalStrut(15));
        add(buttonPanel);
        add(Box.createVerticalStrut(10));
    }

    private void initializeTable() {
        jTable1 = new JTable();
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Student ID", "Name", "Gender", "Phone Number", "Room Number", "Amount", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        // Style the table
        jTable1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jTable1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        jTable1.setRowHeight(25);
        jTable1.setShowGrid(true);
        jTable1.setGridColor(FIELD_BORDER);
        jTable1.setSelectionBackground(new Color(210, 225, 245));
        jTable1.getTableHeader().setReorderingAllowed(false);
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

    private void setupSearchBar() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        jTable1.setRowSorter(sorter);
        
        Search.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = Search.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
    }

    private void loadTableData() {
        loadTableDataFromFile();
    }

    private void loadTableDataFromFile() {
        String filePath = fileHandler.getBasePath() + "ResidentsList.txt";
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        
        File file = new File(filePath);
        if (!file.exists()) {
            // Add some default data if file doesn't exist
            Object[][] defaultData = {
                {"TP000001", "Lim Ah Huat", "Male", "60123456789", "A-05-02", "800", "Unpaid"},
                {"TP000008", "Taylor Swift", "Female", "60198765432", "B-08-10", "600", "Unpaid"},
                {"TP000888", "Zhao Li Ying", "Female", "60191016870", "B-10-16", "400", "Paid"},
                {"TP111111", "Yang Mi", "Female", "60145698723", "B-09-12", "600", "Unpaid"},
                {"TP123456", "Chris Hemsworth", "Male", "60112233445", "A-01-01", "800", "Paid"}
            };
            for (Object[] row : defaultData) {
                model.addRow(row);
            }
            saveTableDataToFile();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] rowData = line.split("\t");
                    model.addRow(rowData);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void saveTableDataToFile() {
        String filePath = fileHandler.getBasePath() + "ResidentsList.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < jTable1.getColumnCount(); j++) {
                    row.append(jTable1.getValueAt(i, j)).append("\t");
                }
                writer.write(row.toString().trim());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
        }
    }

    private void ChangeStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String currentStatus = (String) jTable1.getValueAt(selectedRow, 6);
            String[] options = {"Paid", "Unpaid"};
            String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Update Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                currentStatus
            );
            
            if (newStatus != null) {
                jTable1.setValueAt(newStatus, selectedRow, 6);
                JOptionPane.showMessageDialog(this, "Status updated successfully.");
                saveTableDataToFile();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row first.");
        }
    }

    private void GenerateReceiptActionPerformed(java.awt.event.ActionEvent evt) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).getContentPane().removeAll();
            ((JFrame) window).getContentPane().add(new ReceiptGenerationPanel(staffId));
            ((JFrame) window).getContentPane().revalidate();
            ((JFrame) window).getContentPane().repaint();
        }
    }

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).getContentPane().removeAll();
            ((JFrame) window).getContentPane().add(new StaffDashboardPanel(staffId));
            ((JFrame) window).getContentPane().revalidate();
            ((JFrame) window).getContentPane().repaint();
        }
    }
}
