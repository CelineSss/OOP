package gui;

import main.HostelManagementSystem;
import javax.swing.*;
import java.awt.*;
import service.UserService;
import service.MaxEmailAttemptsExecption;
import service.InvalidEmailExecption;
import util.UIStyler;

public class RegistrationPanel extends JPanel {
    private final HostelManagementSystem mainWindow;
    private final UserService userService;
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField securityKeywordField;

    public RegistrationPanel(HostelManagementSystem mainWindow) {
        this.mainWindow = mainWindow;
        this.userService = new UserService();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        UIStyler.stylePanel(this);

        JLabel titleLabel = new JLabel("User Registration");
        UIStyler.styleTitleLabel(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        UIStyler.stylePanel(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel fullNameLabel = new JLabel("Full Name:");
        UIStyler.styleLabel(fullNameLabel);
        formPanel.add(fullNameLabel, gbc);
        
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        UIStyler.styleTextField(fullNameField);
        formPanel.add(fullNameField, gbc);

        // User ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel userIdLabel = new JLabel("User ID:");
        UIStyler.styleLabel(userIdLabel);
        formPanel.add(userIdLabel, gbc);
        
        gbc.gridx = 1;
        userIdField = new JTextField(20);
        UIStyler.styleTextField(userIdField);
        formPanel.add(userIdField, gbc);

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

        // Show/Hide Password button
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JToggleButton togglePassword = new JToggleButton("👁");
        togglePassword.setFont(new Font("Dialog", Font.PLAIN, 12));
        togglePassword.setToolTipText("Show/Hide Password");
        togglePassword.setPreferredSize(new Dimension(40, passwordField.getPreferredSize().height));
        togglePassword.setFocusPainted(false);
        togglePassword.setBackground(new Color(240, 240, 240));
        togglePassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        togglePassword.addActionListener(e -> {
            if (togglePassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
                togglePassword.setText("🔒");
                togglePassword.setToolTipText("Hide Password");
            } else {
                passwordField.setEchoChar('•');
                togglePassword.setText("👁");
                togglePassword.setToolTipText("Show Password");
            }
        });
        formPanel.add(togglePassword, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel emailLabel = new JLabel("Email:");
        UIStyler.styleLabel(emailLabel);
        formPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        UIStyler.styleTextField(emailField);
        formPanel.add(emailField, gbc);

        // Security Keyword
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel securityLabel = new JLabel("Security Keyword:");
        UIStyler.styleLabel(securityLabel);
        formPanel.add(securityLabel, gbc);
        
        gbc.gridx = 1;
        securityKeywordField = new JTextField(20);
        UIStyler.styleTextField(securityKeywordField);
        formPanel.add(securityKeywordField, gbc);
        
        // Security Keyword Info
        gbc.gridx = 1;
        gbc.gridy = 6;
        JLabel keywordInfo = new JLabel("For password recovery");
        keywordInfo.setFont(UIStyler.SMALL_FONT);
        keywordInfo.setForeground(UIStyler.LABEL_COLOR.brighter());
        formPanel.add(keywordInfo, gbc);

        // Information Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR),
            "Registration Information"
        ));
        UIStyler.stylePanel(infoPanel);
        
        JTextArea infoText = new JTextArea(
            "User ID Format:\n" +
            "- TPxxxxxx for Residents\n" +
            "- STxxxxxx for Staff\n" +
            "NOTE: Manager are not allowed to register a new account.\n\n" +
            "Password Requirements:\n" +
            "- Minimum 8 characters\n" +
            "- At least 1 uppercase letter\n" +
            "- At least 3 digits\n" +
            "- No spaces allowed\n\n" +
            "Security Keyword:\n" +
            "- Minimum 6 characters\n" +
            "- Can include letters, numbers, and special characters\n" +
            "- Used for password recovery"
        );
        UIStyler.styleTextArea(infoText);
        infoText.setEditable(false);
        infoText.setBackground(null);
        
        // Create a scroll pane for the info text
        JScrollPane infoScrollPane = new JScrollPane(infoText);
        infoScrollPane.setPreferredSize(new Dimension(400, 150));  // Set fixed size
        infoScrollPane.setBorder(null);  // Remove scroll pane border
        infoScrollPane.setBackground(null);
        infoScrollPane.getViewport().setBackground(null);
        
        infoPanel.add(infoScrollPane);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        UIStyler.stylePanel(buttonPanel);
        
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");
        
        Dimension buttonSize = new Dimension(100, 30);
        UIStyler.styleButton(registerButton, buttonSize);
        UIStyler.styleButton(cancelButton, buttonSize);

        registerButton.addActionListener(e -> handleRegistration());
        cancelButton.addActionListener(e -> mainWindow.showLoginPanel());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        // Add all components to main panel with adjusted spacing
        add(titleLabel);
        add(Box.createVerticalStrut(10));  // Reduced spacing
        add(formPanel);
        add(Box.createVerticalStrut(10));  // Reduced spacing
        add(infoPanel);
        add(Box.createVerticalStrut(10));  // Reduced spacing
        add(buttonPanel);
    }

    private void handleRegistration() {
        String fullName = fullNameField.getText();
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String securityKeyword = securityKeywordField.getText();

        if (!validateInputs(fullName, userId, password, email, securityKeyword)) {
            return;
        }

        try {
            userService.registerUser(userId, password, fullName, email, securityKeyword);
            clearFormFields();
            JOptionPane.showMessageDialog(this,
                "Registration submitted for approval.",
                "Registration Success",
                JOptionPane.INFORMATION_MESSAGE);
            mainWindow.showLoginPanel();
        } catch (MaxEmailAttemptsExecption | InvalidEmailExecption e) {
            JOptionPane.showMessageDialog(this,
                "Registration failed: " + e.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFormFields() {
        fullNameField.setText("");
        userIdField.setText("");
        passwordField.setText("");
        emailField.setText("");
        securityKeywordField.setText("");
    }

    private boolean validateInputs(String fullName, String userId, String password, 
                                 String email, String securityKeyword) {
        if (!userService.isValidFullName(fullName)) {
            JOptionPane.showMessageDialog(this,
                "Invalid full name. Please use only letters and spaces.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!userService.isValidUserId(userId)) {
            JOptionPane.showMessageDialog(this,
                "Invalid User ID format. Please check the Registration Information below.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!userService.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this,
                "Invalid password format. Check the requirements below.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!userService.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                "Invalid email format.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!userService.isValidSecurityKeyword(securityKeyword)) {
            JOptionPane.showMessageDialog(this,
                "Security keyword must be at least 6 characters long.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}