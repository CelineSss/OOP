package gui.panels.manager;



import javax.swing.*;
import java.awt.*;
import service.UserService;
import model.User;
import util.UIStyler;
import gui.ManagerDashboardPanel;

public class ManagerProfilePanel extends JPanel {
    private final UserService userService;
    private JTextField userIdField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField securityKeywordField;
    private boolean isEditing = false;
    private JButton editButton;
    private JButton saveButton;
    private JButton cancelButton;

    public ManagerProfilePanel() {
        userService = new UserService();
        initializeUI();
        loadManagerProfile();
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

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        UIStyler.stylePanel(titlePanel);
        
        // Center title
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UIStyler.stylePanel(centerPanel);
        JLabel titleLabel = new JLabel("Manager Profile");
        UIStyler.styleTitleLabel(titleLabel);
        centerPanel.add(titleLabel);

        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(centerPanel, BorderLayout.CENTER);
        topPanel.add(titlePanel);

        add(topPanel, BorderLayout.NORTH);

        // Profile Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UIStyler.stylePanel(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // User ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userIdLabel = new JLabel("User ID:");
        UIStyler.styleLabel(userIdLabel);
        formPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        userIdField = new JTextField(20);
        userIdField.setEditable(false);
        UIStyler.styleTextField(userIdField);
        formPanel.add(userIdField, gbc);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel fullNameLabel = new JLabel("Full Name:");
        UIStyler.styleLabel(fullNameLabel);
        formPanel.add(fullNameLabel, gbc);

        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        fullNameField.setEditable(false);
        UIStyler.styleTextField(fullNameField);
        formPanel.add(fullNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        UIStyler.styleLabel(emailLabel);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setEditable(false);
        UIStyler.styleTextField(emailField);
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        UIStyler.styleLabel(passwordLabel);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        passwordPanel.setBackground(null);
        
        passwordField = new JPasswordField(20);
        passwordField.setEditable(false);
        UIStyler.styleTextField(passwordField);
        
        JToggleButton showPasswordButton = new JToggleButton("👁");
        showPasswordButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        showPasswordButton.setToolTipText("Show/Hide Password");
        showPasswordButton.setPreferredSize(new Dimension(40, passwordField.getPreferredSize().height));
        showPasswordButton.setFocusPainted(false);
        showPasswordButton.setBackground(new Color(240, 240, 240));
        showPasswordButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        
        showPasswordButton.addActionListener(e -> {
            if (showPasswordButton.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
                showPasswordButton.setText("🔒");
                showPasswordButton.setToolTipText("Hide Password");
            } else {
                passwordField.setEchoChar('•'); // Hide password
                showPasswordButton.setText("👁");
                showPasswordButton.setToolTipText("Show Password");
            }
        });
        
        passwordPanel.add(passwordField);
        passwordPanel.add(Box.createHorizontalStrut(5)); // Add some spacing
        passwordPanel.add(showPasswordButton);
        
        formPanel.add(passwordPanel, gbc);

        // Security Keyword
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel securityKeywordLabel = new JLabel("Security Keyword:");
        UIStyler.styleLabel(securityKeywordLabel);
        formPanel.add(securityKeywordLabel, gbc);

        gbc.gridx = 1;
        securityKeywordField = new JTextField(20);
        securityKeywordField.setEditable(false);
        UIStyler.styleTextField(securityKeywordField);
        formPanel.add(securityKeywordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        UIStyler.stylePanel(buttonPanel);

        Dimension buttonSize = new Dimension(100, 35);
        editButton = new JButton("Edit");
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        UIStyler.styleButton(editButton, buttonSize);
        UIStyler.styleButton(saveButton, buttonSize);
        UIStyler.styleButton(cancelButton, buttonSize);

        saveButton.setVisible(false);
        cancelButton.setVisible(false);

        editButton.addActionListener(e -> enableEditing());
        saveButton.addActionListener(e -> saveChanges());
        cancelButton.addActionListener(e -> cancelEditing());

        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadManagerProfile() {
        User manager = userService.getUserById("AD123456");
        if (manager != null) {
            userIdField.setText(manager.getUserId());
            fullNameField.setText(manager.getFullName());
            emailField.setText(manager.getEmail());
            passwordField.setText(manager.getPassword());
            securityKeywordField.setText(manager.getSecurityKeyword());
        }
    }

    private void enableEditing() {
        isEditing = true;
        fullNameField.setEditable(true);
        emailField.setEditable(true);
        passwordField.setEditable(true);
        securityKeywordField.setEditable(true);
        editButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
    }

    private void disableEditing() {
        isEditing = false;
        fullNameField.setEditable(false);
        emailField.setEditable(false);
        passwordField.setEditable(false);
        securityKeywordField.setEditable(false);
        editButton.setVisible(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    private void saveChanges() {
        try {
            String userId = userIdField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String securityKeyword = securityKeywordField.getText();

            // Validate inputs
            if (fullName.trim().isEmpty() || email.trim().isEmpty() || 
                password.trim().isEmpty() || securityKeyword.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "All fields must be filled out",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!userService.isValidFullName(fullName)) {
                JOptionPane.showMessageDialog(this,
                    "Invalid full name format",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!userService.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this,
                    "Invalid email format",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!userService.isValidPassword(password)) {
                JOptionPane.showMessageDialog(this, """
                                                    Invalid password format. Password must:
                                                    - Be at least 8 characters long
                                                    - Contain at least one uppercase letter
                                                    - Contain at least 3 digits
                                                    - Not contain spaces""",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Updated user object
            User updatedManager = new User(userId, password, fullName, email, securityKeyword);
            userService.updateUser(updatedManager);

            JOptionPane.showMessageDialog(this,
                "Profile updated successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            disableEditing();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating profile: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelEditing() {
        loadManagerProfile(); // Reload original data
        disableEditing();
    }
} 