package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import service.UserService;
import model.User;
import util.UIStyler;

public class ModifyManagerProfileDialog extends JDialog {
    private final UserService userService;
    private JTextField userIdField;
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private final String managerId;
    private final ManagerProfileDialog parentDialog;

    public ModifyManagerProfileDialog(Window owner, String managerId, ManagerProfileDialog parentDialog) {
        super(owner, "Modify Manager Profile", ModalityType.APPLICATION_MODAL);
        this.userService = new UserService();
        this.managerId = managerId;
        this.parentDialog = parentDialog;
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
        JPanel titlePanel = new JPanel();
        UIStyler.stylePanel(titlePanel);
        JLabel titleLabel = new JLabel("Modify Manager Profile");
        UIStyler.styleTitleLabel(titleLabel);
        titlePanel.add(titleLabel);
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
        UIStyler.styleTextField(passwordField);
        formPanel.add(passwordField, gbc);

        // Show/Hide Password Toggle
        gbc.gridx = 2;
        JToggleButton togglePassword = new JToggleButton("Show");
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

        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");
        Dimension buttonSize = new Dimension(140, 30);

        UIStyler.styleButton(saveButton, buttonSize);
        UIStyler.styleButton(cancelButton, buttonSize);

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
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

    private void handleSave() {
        String fullName = fullNameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (!userService.isValidFullName(fullName)) {
                JOptionPane.showMessageDialog(this,
                    "Invalid full name format. Please use only letters and spaces.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!userService.isValidPassword(password)) {
                JOptionPane.showMessageDialog(this, """
                                                    - Minimum 8 characters
                                                    Invalid password format. Password must contain:
                                                    - At least 1 uppercase letter
                                                    - At least 3 digits
                                                    - No spaces allowed""",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            User manager = userService.getUserById(managerId);
            manager.setFullName(fullName);
            manager.setPassword(password);
            userService.updateUser(manager);

            JOptionPane.showMessageDialog(this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
                
            // Refresh the parent dialog's display
            parentDialog.refreshContent();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error saving changes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 