package gui;

import main.HostelManagementSystem;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import service.UserService;
import util.ImageLoader;
import util.UIStyler;

public class LoginPanel extends JPanel {
    private final HostelManagementSystem mainWindow;
    private final UserService userService;
    private JTextField userIdField;
    private JPasswordField passwordField;
    
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color TITLE_COLOR = new Color(60, 72, 107);
    private static final Color BUTTON_COLOR = new Color(100, 141, 174);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color FIELD_BACKGROUND = new Color(255, 255, 255);
    private static final Color LABEL_COLOR = new Color(90, 103, 140);
    private static final Color LINK_COLOR = new Color(100, 141, 174);
    private static final Color TOGGLE_BUTTON_COLOR = new Color(240, 242, 245);
    private static final Color TOGGLE_BORDER_COLOR = new Color(210, 215, 220);

    public LoginPanel(HostelManagementSystem mainWindow) {
        this.mainWindow = mainWindow;
        this.userService = new UserService();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setBackground(BACKGROUND_COLOR);

        // Create top panel for logout button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topPanel.setBackground(BACKGROUND_COLOR);
        
        // Create logout button with image
        ImageIcon logoutIcon = ImageLoader.loadImage("/images/logout.png", 24, 24);
        JButton logoutButton = new JButton(logoutIcon);
        logoutButton.setToolTipText("Exit Application");
        logoutButton.setBackground(BACKGROUND_COLOR);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setMargin(new Insets(5, 5, 5, 5));
        
        // Add confirmation dialog before exiting
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        topPanel.add(logoutButton);

        ImageIcon logoIcon = ImageLoader.loadImage("/images/logo.png", 150, 150);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("APU Hostel Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(topPanel);  // Add the top panel first
        add(Box.createVerticalStrut(10));
        add(logoLabel);
        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(20));
        add(createLoginPanel());
        add(Box.createVerticalStrut(20));
        add(createButtonPanel());
        add(Box.createVerticalGlue());
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        addLoginField(loginPanel, gbc, "User ID:", userIdField = new JTextField(15), 0);
        addLoginField(loginPanel, gbc, "Password:", passwordField = new JPasswordField(15), 1);
        addTogglePasswordButton(loginPanel, gbc);
        addClearButton(loginPanel, gbc);
        addForgotPasswordLink(loginPanel, gbc);
        return loginPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        topButtonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton loginButton = createStyledButton("Login", new Dimension(100, 30));
        JButton registerButton = createStyledButton("Register", new Dimension(100, 30));
        
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> mainWindow.showRegistrationPanel());
        
        topButtonPanel.add(loginButton);
        topButtonPanel.add(registerButton);

        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomButtonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton checkStatusButton = createStyledButton("Check Status", new Dimension(200, 30));
        checkStatusButton.addActionListener(e -> checkRegistrationStatus());
        bottomButtonPanel.add(checkStatusButton);

        buttonPanel.add(topButtonPanel);
        buttonPanel.add(bottomButtonPanel);

        return buttonPanel;
    }

    private void handleLogin() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (userService.login(userId, password)) {
                clearFields();
                if (userId.startsWith("AD")) {
                    mainWindow.showManagerDashboard(userId);
                } else if (userId.startsWith("ST")) {
                    mainWindow.showStaffDashboard(userId);
                } else if (userId.startsWith("TP")) {
                    mainWindow.showResidentDashboard(userId);
                }
            }
        } catch (SecurityException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Login Error",
                JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error during login: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addLoginField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        JLabel label = new JLabel(labelText, SwingConstants.RIGHT);
        label.setForeground(LABEL_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(label, gbc);

        gbc.gridx = 1;
        styleTextField(field);
        panel.add(field, gbc);
    }

    private void addTogglePasswordButton(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JToggleButton togglePassword = new JToggleButton("👁");
        togglePassword.setFont(new Font("Dialog", Font.PLAIN, 12));
        togglePassword.setToolTipText("Show/Hide Password");
        togglePassword.setPreferredSize(new Dimension(40, passwordField.getPreferredSize().height));
        togglePassword.setFocusPainted(false);
        togglePassword.setBackground(TOGGLE_BUTTON_COLOR);
        togglePassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TOGGLE_BORDER_COLOR),
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
        panel.add(togglePassword, gbc);
    }

    private void addClearButton(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        JToggleButton clearButton = new JToggleButton("Clear");
        styleToggleButton(clearButton);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton, gbc);
    }

    private void addForgotPasswordLink(JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel forgotPasswordLink = new JLabel("Forgot Password?");
        forgotPasswordLink.setForeground(LINK_COLOR);
        forgotPasswordLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleForgotPassword();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                forgotPasswordLink.setForeground(LINK_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                forgotPasswordLink.setForeground(LINK_COLOR);
            }
        });
        panel.add(forgotPasswordLink, gbc);
    }

    private void styleTextField(JTextField textField) {
        textField.setBackground(FIELD_BACKGROUND);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(TOGGLE_BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setSelectionColor(new Color(210, 225, 245));
        textField.setCaretColor(LABEL_COLOR);
    }

    private void styleToggleButton(JToggleButton button) {
        button.setMinimumSize(new Dimension(60, 25));
        button.setMaximumSize(new Dimension(60, 25));
        button.setPreferredSize(new Dimension(60, 25));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setFocusPainted(false);
        button.setBackground(TOGGLE_BUTTON_COLOR);
        button.setBorder(new LineBorder(TOGGLE_BORDER_COLOR));
        button.setForeground(LABEL_COLOR);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(TOGGLE_BUTTON_COLOR.darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(TOGGLE_BUTTON_COLOR);
                }
            }
        });
    }

    private JButton createStyledButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private void clearFields() {
        userIdField.setText("");
        passwordField.setText("");
        userIdField.requestFocus();
    }

    private void handleForgotPassword() {
        JDialog resetDialog = new JDialog(mainWindow, "Reset Password", true);
        resetDialog.setSize(400, 300);
        resetDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // User ID
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 10, 2, 10); 
        panel.add(new JLabel("User ID:"), gbc);
        JTextField userIdField = new JTextField(15);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 5, 10); 
        panel.add(userIdField, gbc);
        
        // Security Keyword
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 10, 2, 10); 
        panel.add(new JLabel("Security Keyword:"), gbc);
        JTextField keywordField = new JTextField(15);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 5, 10);
        panel.add(keywordField, gbc);
        
        // New Password
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 10, 2, 10); 
        panel.add(new JLabel("New Password:"), gbc);
        JPasswordField newPasswordField = new JPasswordField(15);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 5, 10);
        panel.add(newPasswordField, gbc);

        // Show/Hide Password Button
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 10, 10, 10); 
        JToggleButton togglePassword = new JToggleButton("👁");
        togglePassword.setFont(new Font("Dialog", Font.PLAIN, 12));
        togglePassword.setToolTipText("Show/Hide Password");
        togglePassword.setPreferredSize(new Dimension(40, newPasswordField.getPreferredSize().height));
        togglePassword.setFocusPainted(false);
        togglePassword.setBackground(TOGGLE_BUTTON_COLOR);
        togglePassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TOGGLE_BORDER_COLOR),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        togglePassword.addActionListener(e -> {
            if (togglePassword.isSelected()) {
                newPasswordField.setEchoChar((char) 0);
                togglePassword.setText("🔒");
                togglePassword.setToolTipText("Hide Password");
            } else {
                newPasswordField.setEchoChar('•');
                togglePassword.setText("👁");
                togglePassword.setToolTipText("Show Password");
            }
        });
        panel.add(togglePassword, gbc);
        
        // Reset Button
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 10, 10, 10); 
        JButton resetButton = createStyledButton("Reset Password", new Dimension(150, 30));
        resetButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String keyword = keywordField.getText();
            String newPassword = new String(newPasswordField.getPassword());
            
            try {
                if (userService.resetPassword(userId, keyword, newPassword)) {
                    JOptionPane.showMessageDialog(resetDialog,
                        "Password successfully reset!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    resetDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(resetDialog,
                        "Invalid User ID or Security Keyword",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(resetDialog,
                    "Error resetting password.\n" +
                    "Password format must contain:\n" +
                    "- Minimum 8 characters\n" +
                    "- At least 1 uppercase letter\n" +
                    "- At least 3 digits\n" +
                    "- No spaces allowed",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(resetButton, gbc);
        
        resetDialog.add(panel);
        resetDialog.setVisible(true);
    }

    private void checkRegistrationStatus() {
        String userId = JOptionPane.showInputDialog(this,
            "Enter your User ID:",
            "Check Registration Status",
            JOptionPane.QUESTION_MESSAGE);
                
        if (userId != null && !userId.trim().isEmpty()) {
            try {
                String status = userService.checkDetailedRegistrationStatus(userId);
                switch (status) {
                    case "PENDING" -> JOptionPane.showMessageDialog(this,
                        "Status: PENDING\nYour registration is still being processed.",
                        "Registration Status",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                    case "REJECTED" -> JOptionPane.showMessageDialog(this,
                        "Status: REJECTED\nYour registration has been rejected.",
                        "Registration Status",
                        JOptionPane.WARNING_MESSAGE);
                        
                    case "APPROVED" -> JOptionPane.showMessageDialog(this,
                        "Status: APPROVED\nYour registration has been approved.",
                        "Registration Status",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                    case "NOT_FOUND" -> JOptionPane.showMessageDialog(this,
                        "No registration found for this User ID.",
                        "Registration Status",
                        JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error checking registration status: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 