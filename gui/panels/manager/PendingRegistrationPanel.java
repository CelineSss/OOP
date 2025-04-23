package gui.panels.manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import service.UserService;
import util.FileHandler;
import util.UIStyler;
import gui.ManagerDashboardPanel;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PendingRegistrationPanel extends JPanel {
    private final UserService userService;
    private final FileHandler fileHandler;
    private JTable registrationsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterCombo;
    private TableRowSorter<DefaultTableModel> sorter;

    public PendingRegistrationPanel() {
        userService = new UserService();
        fileHandler = new FileHandler();
        initializeUI();
        loadPendingRegistrations();
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
        JPanel centerPanel = new JPanel();
        UIStyler.stylePanel(centerPanel);
        JLabel titleLabel = new JLabel("Pending Registrations");
        UIStyler.styleTitleLabel(titleLabel);
        centerPanel.add(titleLabel);

        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(centerPanel, BorderLayout.CENTER);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        UIStyler.stylePanel(filterPanel);
        JLabel filterLabel = new JLabel("Filter by:");
        UIStyler.styleLabel(filterLabel);
        filterCombo = new JComboBox<>(new String[]{"All Users", "Staff", "Resident"});
        UIStyler.styleComboBox(filterCombo);
        filterPanel.add(filterLabel);
        filterPanel.add(filterCombo);

        titlePanel.add(filterPanel, BorderLayout.EAST);
        topPanel.add(titlePanel);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"User ID", "Full Name", "Email", "User Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        registrationsTable = new JTable(tableModel);
        registrationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIStyler.styleTable(registrationsTable);
        
        // Add sorter for filtering
        sorter = new TableRowSorter<>(tableModel);
        registrationsTable.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(registrationsTable);
        scrollPane.getViewport().setBackground(UIStyler.BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        UIStyler.stylePanel(buttonPanel);
        
        // Center panel for buttons
        JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        UIStyler.stylePanel(centerButtonPanel);
        
        Dimension buttonSize = new Dimension(120, 35);
        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");

        UIStyler.styleButton(approveButton, buttonSize);
        UIStyler.styleButton(rejectButton, buttonSize);

        approveButton.addActionListener(e -> handleApproval());
        rejectButton.addActionListener(e -> handleRejection());

        centerButtonPanel.add(approveButton);
        centerButtonPanel.add(rejectButton);

        buttonPanel.add(centerButtonPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        //Filter listener
        filterCombo.addActionListener(e -> applyFilter());
    }

    private void applyFilter() {
        String selectedFilter = (String) filterCombo.getSelectedItem();
        if (selectedFilter == null) return;

        if (selectedFilter.equals("All Users")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(selectedFilter, 3)); // Column 3 is User Type
        }
    }

    private void loadPendingRegistrations() {
        tableModel.setRowCount(0); // Clear existing data
        List<String> pendingRegistrations = fileHandler.readFile("pending_registration.txt");

        for (String registration : pendingRegistrations) {
            String[] parts = registration.split(",");
            if (parts.length >= 4) {
                String userType = parts[0].startsWith("TP") ? "Resident" : "Staff";
                tableModel.addRow(new Object[]{
                    parts[0], // User ID
                    parts[2], // Full Name
                    parts[3], // Email
                    userType  // User Type
                });
            }
        }
        applyFilter(); // Reapply filter after loading data
    }

    private void handleApproval() {
        int selectedRow = registrationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a registration to approve",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to approve this registration?",
            "Confirm Approval",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userService.approvePendingRegistration(userId);
                loadPendingRegistrations();
                
                JOptionPane.showMessageDialog(this,
                    "Registration approved successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error approving registration: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRejection() {
        int selectedRow = registrationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a registration to reject",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to reject this registration?",
            "Confirm Rejection",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userService.rejectPendingRegistration(userId);
                loadPendingRegistrations();
                JOptionPane.showMessageDialog(this,
                    "Registration rejected successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error rejecting registration: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}