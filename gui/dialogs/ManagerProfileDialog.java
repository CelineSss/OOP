package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import service.UserService;
import model.User;
import util.UIStyler;

public class ManagerProfileDialog extends JDialog {
    private final UserService userService;
    private JTextField userIdField;
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private final String managerId;
    private JToggleButton togglePassword;

    public ManagerProfileDialog(Window owner, String managerId) {
        super(owner, "Manager Profile", ModalityType.APPLICATION_MODAL);
        this.userService = new UserService();
        this.managerId = managerId;
        initializeUI();
        loadManagerData();
    }

    private void initializeUI() {
        setSize(500, 400);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        UIStyler.stylePanel(mainPanel);

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        UIStyler.stylePanel(titlePanel);
        
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
        backButton.addActionListener(e -> dispose());
        
        // Add hover effect to back button
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.GRAY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.BLACK);
            }
        });

        // Title with left alignment
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 80, 0));
        UIStyler.stylePanel(centerPanel);
        JLabel titleLabel = new JLabel("Manager Profile");
        UIStyler.styleTitleLabel(titleLabel);
        centerPanel.add(titleLabel);

        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UIStyler.stylePanel(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Manager ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userIdLabel = new JLabel("Manager ID:");
        UIStyler.styleLabel(userIdLabel);
        formPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        userIdField = new JTextField(20);
        userIdField.setEditable(false);
        UIStyler.styleTextField(userIdField);
        formPanel.add(userIdField, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel fullNameLabel = new JLabel("Full Name:");
        UIStyler.styleLabel(fullNameLabel);
        formPanel.add(fullNameLabel, gbc);

        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        fullNameField.setEditable(false);
        UIStyler.styleTextField(fullNameField);
        formPanel.add(fullNameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        UIStyler.styleLabel(passwordLabel);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setEditable(false);
        UIStyler.styleTextField(passwordField);
        formPanel.add(passwordField, gbc);

        // Show/Hide Password Toggle
        gbc.gridx = 2;
        togglePassword = new JToggleButton("Show");
        UIStyler.styleToggleButton(togglePassword);
        togglePassword.addActionListener(e -> {
            if (togglePassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
                togglePassword.setText("Hide");
            } else {
                passwordField.setEchoChar('•');
                togglePassword.setText("Show");
            }
        });
        formPanel.add(togglePassword, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        UIStyler.stylePanel(buttonPanel);

        JButton modifyButton = new JButton("Modify Profile");
        Dimension buttonSize = new Dimension(140, 30);
        UIStyler.styleButton(modifyButton, buttonSize);
        modifyButton.addActionListener(e -> openModifyDialog());
        buttonPanel.add(modifyButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void openModifyDialog() {
        ModifyManagerProfileDialog dialog = new ModifyManagerProfileDialog(this, managerId, this);
        dialog.setVisible(true);
    }

    public void refreshContent() {
        loadManagerData();
    }

    private void loadManagerData() {
        try {
            User manager = userService.getUserById(managerId);
            if (manager != null) {
                userIdField.setText(manager.getUserId());
                fullNameField.setText(manager.getFullName());
                passwordField.setText(manager.getPassword());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading manager data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 