/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui.panels.manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import service.RateService;
import util.FileHandler;
import gui.dialogs.RateDialog;
import util.UIStyler;
import gui.ManagerDashboardPanel;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;

public class RateManagementPanel extends JPanel {
    private final RateService rateService;
    private final FileHandler fileHandler;
    private JTable ratesTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> rateTypeCombo;
    private TableRowSorter<DefaultTableModel> sorter;
    private final Dimension buttonSize = new Dimension(120, 35);

    public RateManagementPanel() {
        rateService = new RateService();
        fileHandler = new FileHandler();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        UIStyler.stylePanel(this);

        // Title Panel with Back Button
        JPanel topPanel = new JPanel(new BorderLayout());
        UIStyler.stylePanel(topPanel);

        // Back button
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Dialog", Font.BOLD, 24));
        backButton.setToolTipText("Back to Dashboard");
        backButton.setBackground(null);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(50, 25));
        backButton.setForeground(Color.BLACK);
        backButton.setMargin(new Insets(2, 2, 2, 2));
        backButton.addActionListener(e -> {
            Container parent = SwingUtilities.getAncestorOfClass(ManagerDashboardPanel.class, this);
            if (parent instanceof ManagerDashboardPanel) {
                ((ManagerDashboardPanel) parent).showMainPanel();
            }
        });
        
        // Add hover effect to back button
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.BLACK);
            }
        });

        JPanel titlePanel = new JPanel(new BorderLayout());
        UIStyler.stylePanel(titlePanel);
        
        // Center title
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 240, 0));
        UIStyler.stylePanel(centerPanel);
        JLabel titleLabel = new JLabel("Rate Management");
        UIStyler.styleTitleLabel(titleLabel);
        centerPanel.add(titleLabel);

        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.add(titlePanel);

        add(topPanel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        UIStyler.stylePanel(filterPanel);
        JLabel filterLabel = new JLabel("Filter:");
        UIStyler.styleLabel(filterLabel);
        rateTypeCombo = new JComboBox<>(new String[]{
            "All Rates", 
            "Rental Rates", 
            "Utility Rates", 
            "Penalty Rates"
        });
        UIStyler.styleComboBox(rateTypeCombo);
        filterPanel.add(filterLabel);
        filterPanel.add(rateTypeCombo);
        topPanel.add(filterPanel, BorderLayout.EAST);

        // Table
        String[] columns = {"Rate ID", "Type", "Description", "Amount (RM)", "Last Updated"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ratesTable = new JTable(tableModel);
        ratesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIStyler.styleTable(ratesTable);
        
        // Add sorter for filtering
        sorter = new TableRowSorter<>(tableModel);
        ratesTable.setRowSorter(sorter);
        
        // Add these lines to align the columns
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        // Apply left alignment to all columns
        for (int i = 0; i < ratesTable.getColumnCount(); i++) {
            ratesTable.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(ratesTable);
        scrollPane.getViewport().setBackground(UIStyler.BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        UIStyler.stylePanel(buttonPanel);
        
        // Left panel for Back button
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        UIStyler.stylePanel(leftButtonPanel);

        // Center panel for main buttons
        JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        UIStyler.stylePanel(centerButtonPanel);

        JButton addButton = new JButton("Add Rate");
        JButton editButton = new JButton("Edit Rate");
        JButton deleteButton = new JButton("Delete Rate");

        UIStyler.styleButton(addButton, buttonSize);
        UIStyler.styleButton(editButton, buttonSize);
        UIStyler.styleButton(deleteButton, buttonSize);

        addButton.addActionListener(e -> showAddRateDialog());
        editButton.addActionListener(e -> showEditRateDialog());
        deleteButton.addActionListener(e -> handleDeleteRate());

        centerButtonPanel.add(addButton);
        centerButtonPanel.add(editButton);
        centerButtonPanel.add(deleteButton);

        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
        buttonPanel.add(centerButtonPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add filter listener
        rateTypeCombo.addActionListener(e -> loadRates());

        // Initial load
        loadRates();
    }

    private void loadRates() {
        tableModel.setRowCount(0);
        String selectedType = (String) rateTypeCombo.getSelectedItem();

        if ("Rental Rates".equals(selectedType) || "All Rates".equals(selectedType)) {
            loadRatesFromFile("rent_rate.txt", "Rental");
        }
        if ("Utility Rates".equals(selectedType) || "All Rates".equals(selectedType)) {
            loadRatesFromFile("utility_rate.txt", "Utility");
        }
        if ("Penalty Rates".equals(selectedType) || "All Rates".equals(selectedType)) {
            loadRatesFromFile("penalty_rates.txt", "Penalty");
        }
    }

    private void loadRatesFromFile(String fileName, String rateType) {
        List<String> rates = fileHandler.readFile(fileName);
        for (String rate : rates) {
            String[] parts = rate.split(",");
            if (parts.length >= 5) {
                tableModel.addRow(new Object[]{
                    parts[0],    // Rate ID
                    parts[1],    // Type
                    parts[2],    // Description
                    parts[3],    // Amount (remove RM prefix if needed)
                    parts[4]     // Last Updated
                });
            }
        }
    }

    private void showAddRateDialog() {
        RateDialog dialog = new RateDialog(SwingUtilities.getWindowAncestor(this), "Add New Rate");
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            loadRates();
        }
    }

    private void showEditRateDialog() {
        int selectedRow = ratesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a rate to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rateId = (String) tableModel.getValueAt(selectedRow, 0);
        String rateType = (String) tableModel.getValueAt(selectedRow, 1);
        RateDialog dialog = new RateDialog(
            SwingUtilities.getWindowAncestor(this), 
            "Edit Rate",
            rateId,
            rateType
        );
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            loadRates();
        }
    }

    private void handleDeleteRate() {
        int selectedRow = ratesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a rate to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rateId = (String) tableModel.getValueAt(selectedRow, 0);
        String rateType = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this rate?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String fileName = getFileNameForRateType(rateType);
                List<String> rates = fileHandler.readFile(fileName);
                rates.removeIf(rate -> rate.startsWith(rateId + ","));
                fileHandler.writeFile(fileName, rates);
                loadRates();
                JOptionPane.showMessageDialog(this,
                    "Rate deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting rate: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFileNameForRateType(String rateType) {
        return switch (rateType) {
            case "Rental" -> "rent_rate.txt";
            case "Utility" -> "utility_rate.txt";
            case "Penalty" -> "penalty_rates.txt";
            default -> throw new IllegalArgumentException("Invalid rate type: " + rateType);
        };
    }
}


