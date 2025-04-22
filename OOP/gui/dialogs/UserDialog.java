package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import service.UserService;
import util.FileHandler;
import util.UIStyler;
import model.User;
import model.Staff;
import model.Resident;

public class UserDialog extends JDialog {
    private boolean confirmed = false;
    private final UserService userService;
    private final FileHandler fileHandler;
    private JTextField userIdField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;

    public UserDialog(Window owner, String title) {
        this(owner, title, null);
    }

    public UserDialog(Window owner, String title, String userId) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        userService = new UserService();
        fileHandler = new FileHandler();
        initializeUI(userId);
        loadUserData(userId);
    }

    private void initializeUI(String userId) {
        setLayout(new BorderLayout(10, 10));
        setSize(450, 350);
        setLocationRelativeTo(getOwner());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        UIStyler.stylePanel(mainPanel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UIStyler.stylePanel(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // User Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel typeLabel = new JLabel("User Type:");
        UIStyler.styleLabel(typeLabel);
        formPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(new String[]{"Staff", "Resident"});
        UIStyler.styleComboBox(userTypeCombo);
        userTypeCombo.setEnabled(userId == null);
        formPanel.add(userTypeCombo, gbc);

        // User ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel userIdLabel = new JLabel("User ID:");
        UIStyler.styleLabel(userIdLabel);
        formPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        userIdField = new JTextField(20);
        userIdField.setPreferredSize(new Dimension(300, 32));
        UIStyler.styleTextField(userIdField);
        userIdField.setEnabled(userId == null);
        formPanel.add(userIdField, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel nameLabel = new JLabel("Full Name:");
        UIStyler.styleLabel(nameLabel);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        fullNameField.setPreferredSize(new Dimension(450, 32));
        UIStyler.styleTextField(fullNameField);
        formPanel.add(fullNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel emailLabel = new JLabel("Email:");
        UIStyler.styleLabel(emailLabel);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(450, 32));
        UIStyler.styleTextField(emailField);
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel passwordLabel = new JLabel("Password:");
        UIStyler.styleLabel(passwordLabel);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(450, 32));
        UIStyler.styleTextField(passwordField);
        formPanel.add(passwordField, gbc);

        // Add Show/Hide Password Toggle Button
        gbc.gridx = 2;
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

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        UIStyler.stylePanel(buttonPanel);

        Dimension buttonSize = new Dimension(100, 30);
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        UIStyler.styleButton(saveButton, buttonSize);
        UIStyler.styleButton(cancelButton, buttonSize);

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadUserData(String userId) {
        if (userId != null) {
            try {
                System.out.println("Loading user data for ID: " + userId);
                User user = userService.getUserById(userId);
                System.out.println("User object retrieved: " + (user != null ? "yes" : "no"));
                if (user != null) {
                    System.out.println("User details - ID: " + user.getUserId() + 
                                     ", Name: " + user.getFullName() + 
                                     ", Email: " + user.getEmail());
                    userTypeCombo.setSelectedItem(userId.startsWith("ST") ? "Staff" : "Resident");
                    userIdField.setText(user.getUserId());
                    fullNameField.setText(user.getFullName());
                    emailField.setText(user.getEmail());
                    passwordField.setText(user.getPassword());
                    System.out.println("Fields populated successfully");
                }
            } catch (Exception e) {
                System.out.println("Error loading user data: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Error loading user data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("No user ID provided");
        }
    }

    private void handleSave() {
        String userType = (String) userTypeCombo.getSelectedItem();
        String userId = userIdField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (validateInput(userId, fullName, email, password)) {
            try {
                // If editing an existing user, get their current data to preserve the security keyword
                String securityKeyword = "";
                if (!getTitle().startsWith("Add")) {
                    User existingUser = userService.getUserById(userId);
                    if (existingUser != null) {
                        securityKeyword = existingUser.getSecurityKeyword();
                    }
                }

                // User object
                User user = userType.equals("Staff") ? 
                    new Staff(userId, password, fullName, email, securityKeyword) :
                    new Resident(userId, password, fullName, email, securityKeyword);
                
                // If userId is null, this is a new user
                if (getTitle().startsWith("Add")) {
                    fileHandler.saveUser(user);
                } else {
                    userService.updateUser(user);
                }
                confirmed = true;
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error saving user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput(String userId, String fullName, String email, String password) {
        if (userId.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "All fields are required",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if user ID prefix matches selected user type
        String userType = (String) userTypeCombo.getSelectedItem();
        if (userType.equals("Staff") && !userId.startsWith("ST")) {
            JOptionPane.showMessageDialog(this,
                "Staff user ID must start with 'ST'",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (userType.equals("Resident") && !userId.startsWith("TP")) {
            JOptionPane.showMessageDialog(this,
                "Resident user ID must start with 'TP'",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!userService.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                "Invalid email format",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!userService.isValidFullName(fullName)) {
            JOptionPane.showMessageDialog(this,
                "Invalid full name format",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!userService.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this,
                "Invalid password format",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!userService.isValidUserId(userId)) {
            JOptionPane.showMessageDialog(this,
                "Invalid user ID format",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    private void showResetPasswordDialog() {
        JDialog resetDialog = new JDialog(this, "Reset Password", true);
        resetDialog.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        UIStyler.stylePanel(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // User ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userIdLabel = new JLabel("User ID:");
        UIStyler.styleLabel(userIdLabel);
        mainPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        JTextField userIdField = new JTextField(20);
        UIStyler.styleTextField(userIdField);
        mainPanel.add(userIdField, gbc);

        // Security Keyword field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel securityLabel = new JLabel("Security Keyword:");
        UIStyler.styleLabel(securityLabel);
        mainPanel.add(securityLabel, gbc);

        gbc.gridx = 1;
        JTextField securityField = new JTextField(20);
        UIStyler.styleTextField(securityField);
        mainPanel.add(securityField, gbc);

        // New Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel newPasswordLabel = new JLabel("New Password:");
        UIStyler.styleLabel(newPasswordLabel);
        mainPanel.add(newPasswordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField newPasswordField = new JPasswordField(20);
        UIStyler.styleTextField(newPasswordField);
        mainPanel.add(newPasswordField, gbc);

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
                newPasswordField.setEchoChar((char) 0);
                togglePassword.setText("🔒");
                togglePassword.setToolTipText("Hide Password");
            } else {
                newPasswordField.setEchoChar('•');
                togglePassword.setText("👁");
                togglePassword.setToolTipText("Show Password");
            }
        });
        mainPanel.add(togglePassword, gbc);

        // Reset button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton resetButton = new JButton("Reset Password");
        Dimension buttonSize = new Dimension(150, 30);
        UIStyler.styleButton(resetButton, buttonSize);
        resetButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String keyword = securityField.getText();
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
                    "Error resetting password: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(resetButton, gbc);

        resetDialog.add(mainPanel);
        resetDialog.pack();
        resetDialog.setLocationRelativeTo(this);
        resetDialog.setResizable(false);
        resetDialog.setVisible(true);
    }
}