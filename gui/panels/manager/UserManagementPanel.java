package gui.panels.manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import service.UserService;
import util.FileHandler;
import gui.dialogs.UserDialog;
import util.UIStyler;
import gui.ManagerDashboardPanel;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;

public class UserManagementPanel extends JPanel {
    private final UserService userService;
    private final FileHandler fileHandler;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> userTypeCombo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JButton backButton;

    public UserManagementPanel() {
        userService = new UserService();
        fileHandler = new FileHandler();
        initializeUI();
        loadUsers();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        UIStyler.stylePanel(this);

        // Title Panel with Back Button
        JPanel topPanel = new JPanel(new BorderLayout());
        UIStyler.stylePanel(topPanel);

        // Back button
        backButton = new JButton("←");
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
        
        // Left panel combining back button and search
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        UIStyler.stylePanel(leftPanel);
        
        // Add back button
        leftPanel.add(backButton);
        
        // Add search components
        JLabel searchLabel = new JLabel("Search:");
        UIStyler.styleLabel(searchLabel);
        JTextField searchField = new JTextField(15);
        UIStyler.styleTextField(searchField);
        searchField.addActionListener(e -> applyFilter(searchField.getText()));
        
        leftPanel.add(searchLabel);
        leftPanel.add(searchField);
        
        // Center title
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        UIStyler.stylePanel(centerPanel);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, -50, 0, 0));
        JLabel titleLabel = new JLabel("User Management");
        UIStyler.styleTitleLabel(titleLabel);
        centerPanel.add(titleLabel);

        // Right panel for filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        UIStyler.stylePanel(filterPanel);
        
        // Add user type filter
        JLabel filterLabel = new JLabel("Filter by:");
        UIStyler.styleLabel(filterLabel);
        userTypeCombo = new JComboBox<>(new String[]{"All Users", "Staff", "Residents"});
        UIStyler.styleComboBox(userTypeCombo);
        
        filterPanel.add(filterLabel);
        filterPanel.add(userTypeCombo);

        titlePanel.add(leftPanel, BorderLayout.WEST);
        titlePanel.add(centerPanel, BorderLayout.CENTER);
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
        usersTable = new JTable(tableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIStyler.styleTable(usersTable);
        
        // Add sorter for filtering
        sorter = new TableRowSorter<>(tableModel);
        usersTable.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.getViewport().setBackground(UIStyler.BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        UIStyler.stylePanel(buttonPanel);
        
        // Left panel for Back button
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        UIStyler.stylePanel(leftButtonPanel);
        
        // Center panel for other buttons
        JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        UIStyler.stylePanel(centerButtonPanel);
        
        Dimension buttonSize = new Dimension(100, 30);
        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");

        UIStyler.styleButton(addButton, buttonSize);
        UIStyler.styleButton(editButton, buttonSize);
        UIStyler.styleButton(deleteButton, buttonSize);

        addButton.addActionListener(e -> showAddUserDialog());
        editButton.addActionListener(e -> showEditUserDialog());
        deleteButton.addActionListener(e -> handleDeleteUser());

        centerButtonPanel.add(addButton);
        centerButtonPanel.add(editButton);
        centerButtonPanel.add(deleteButton);

        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
        buttonPanel.add(centerButtonPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add filter listeners
        userTypeCombo.addActionListener(e -> {
            loadUsers();
            applyFilter(searchField.getText());
        });
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
            private void search() {
                applyFilter(searchField.getText());
            }
        });
    }

    public void loadUsers() {
        tableModel.setRowCount(0);
        String selectedType = (String) userTypeCombo.getSelectedItem();
        
        if ("Staff".equals(selectedType) || "All Users".equals(selectedType)) {
            loadUsersFromFile("staff_acc.txt", "Staff");
        }
        if ("Residents".equals(selectedType) || "All Users".equals(selectedType)) {
            loadUsersFromFile("resident_acc.txt", "Resident");
        }
    }

    private void loadUsersFromFile(String fileName, String userType) {
        List<String> users = fileHandler.readFile(fileName);
        System.out.println("Loading users from " + fileName + ": " + users.size() + " users found");
        for (String user : users) {
            System.out.println("Processing user record: " + user);
            String[] parts = user.trim().split(",");
            System.out.println("Split into " + parts.length + " parts");
            if (parts.length >= 4) {
                System.out.println("Adding user to table: ID=" + parts[0] + ", Name=" + parts[2] + ", Email=" + parts[3]);
                tableModel.addRow(new Object[]{
                    parts[0].trim(), // User ID
                    parts[2].trim(), // Full Name
                    parts[3].trim(), // Email
                    userType
                });
            } else {
                System.out.println("Skipping invalid user record (not enough parts)");
            }
        }
    }

    private void showAddUserDialog() {
        UserDialog dialog = new UserDialog(SwingUtilities.getWindowAncestor(this), "Add New User");
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            loadUsers(); // Refresh the table
        }
    }

    private void showEditUserDialog() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert the view index to model index since the table might be sorted/filtered
        int modelRow = usersTable.convertRowIndexToModel(selectedRow);
        String userId = (String) tableModel.getValueAt(modelRow, 0);
        UserDialog dialog = new UserDialog(SwingUtilities.getWindowAncestor(this), "Edit User", userId);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            loadUsers(); // Refresh the table
        }
    }

    private void handleDeleteUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert the view index to model index since the table might be sorted/filtered
        int modelRow = usersTable.convertRowIndexToModel(selectedRow);
        String userId = (String) tableModel.getValueAt(modelRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this user?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Check all possible files for the user
                String[] filesToCheck = {"staff_acc.txt", "resident_acc.txt"};
                boolean removed = false;
                
                for (String fileName : filesToCheck) {
                    List<String> users = fileHandler.readFile(fileName);
                    List<String> updatedUsers = new ArrayList<>();
                    boolean foundInThisFile = false;
                    
                    for (String user : users) {
                        if (user.trim().startsWith(userId + ",")) {
                            foundInThisFile = true;
                            removed = true;
                            System.out.println("Found and removing user from " + fileName + ": " + user);
                        } else {
                            updatedUsers.add(user);
                        }
                    }
                    
                    if (foundInThisFile) {
                        fileHandler.writeFile(fileName, updatedUsers);
                        System.out.println("Updated " + fileName + " after removal");
                    }
                }
                
                if (!removed) {
                    System.out.println("Warning: User not found in any file for deletion");
                    JOptionPane.showMessageDialog(this,
                        "User not found in the system",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Refresh the table
                tableModel.setRowCount(0);
                loadUsers();
                
                JOptionPane.showMessageDialog(this,
                    "User deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                System.out.println("Error during deletion: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Error deleting user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void applyFilter(String searchText) {
        RowFilter<DefaultTableModel, Object> userTypeFilter = null;
        RowFilter<DefaultTableModel, Object> searchFilter = null;
        
        // Create user type filter
        String selectedType = (String) userTypeCombo.getSelectedItem();
        if (selectedType != null && !selectedType.equals("All Users")) {
            userTypeFilter = RowFilter.regexFilter(selectedType.substring(0, selectedType.length() - 1), 3);
        }
        
        // Create search filter
        if (searchText != null && !searchText.isEmpty()) {
            searchFilter = RowFilter.regexFilter("(?i)" + searchText); // Case-insensitive search
        }
        
        // Combine filters if both are present
        if (userTypeFilter != null && searchFilter != null) {
            List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
            filters.add(userTypeFilter);
            filters.add(searchFilter);
            sorter.setRowFilter(RowFilter.andFilter(filters));
        } else if (userTypeFilter != null) {
            sorter.setRowFilter(userTypeFilter);
        } else if (searchFilter != null) {
            sorter.setRowFilter(searchFilter);
        } else {
            sorter.setRowFilter(null);
        }
    }
}