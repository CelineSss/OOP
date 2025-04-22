package gui.panels.staff;

import javax.swing.*;
import java.awt.*;
import service.UserService;
import model.User;
import model.Staff;

public class StaffProfilePanel extends JPanel {
    private JTextField userIdField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton saveButton;
    private JButton backButton;
    private final UserService userService;
    private String staffId;

    // Define consistent colors
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color TITLE_COLOR = new Color(60, 72, 107);
    private static final Color BUTTON_COLOR = new Color(100, 141, 174);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color LABEL_COLOR = new Color(90, 103, 140);
    private static final Color FIELD_BACKGROUND = Color.WHITE;
    private static final Color FIELD_BORDER = new Color(210, 215, 220);

    public StaffProfilePanel(String staffId) {
        this.userService = new UserService();
        this.staffId = staffId;
        initComponents();
    }

    private void initComponents() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Update Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize components with styling
        userIdField = createStyledTextField();
        emailField = createStyledTextField();
        passwordField = createStyledPasswordField();
        saveButton = createStyledButton("Save", new Dimension(120, 35));
        backButton = createStyledButton("Back", new Dimension(120, 35));

        // Staff ID field
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel idLabel = createStyledLabel("Staff ID:");
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(userIdField, gbc);

        // Email field
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel emailLabel = createStyledLabel("Email:");
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(emailField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel passwordLabel = createStyledLabel("Password:");
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.setBackground(BACKGROUND_COLOR);
        
        passwordPanel.add(passwordField);
        passwordPanel.add(Box.createHorizontalStrut(5));  // Add some spacing

        // Add show/hide password button
        JToggleButton showPasswordButton = new JToggleButton("Show");
        showPasswordButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordButton.setFocusPainted(false);
        showPasswordButton.setPreferredSize(new Dimension(60, 35));
        showPasswordButton.setMaximumSize(new Dimension(60, 35));
        showPasswordButton.setBackground(new Color(240, 242, 245));  // Light gray background
        showPasswordButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 215, 220)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        showPasswordButton.addActionListener(e -> {
            if (showPasswordButton.isSelected()) {
                passwordField.setEchoChar((char) 0);
                showPasswordButton.setText("Hide");
            } else {
                passwordField.setEchoChar('•');
                showPasswordButton.setText("Show");
            }
        });
        passwordPanel.add(showPasswordButton);
        formPanel.add(passwordPanel, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        // Set fields as non-editable initially
        userIdField.setEditable(false);

        // Load user data
        loadUserData();

        // Add action listeners
        saveButton.addActionListener(e -> saveButtonActionPerformed());
        backButton.addActionListener(e -> backButtonActionPerformed());

        // Center the form panel
        JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centeringPanel.setBackground(BACKGROUND_COLOR);
        centeringPanel.add(formPanel);

        // Main Layout
        add(titlePanel);
        add(Box.createVerticalStrut(20));
        add(centeringPanel);
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
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

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setBackground(FIELD_BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setEchoChar('•');  // Set to dots by default
        return field;
    }

    private JButton createStyledButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
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

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(LABEL_COLOR);
        return label;
    }

    private void loadUserData() {
        try {
            User staff = userService.getUserById(staffId);
            if (staff != null) {
                userIdField.setText(staff.getUserId());
                emailField.setText(staff.getEmail());
                passwordField.setText(staff.getPassword());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading user data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveButtonActionPerformed() {
        String userId = userIdField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Validate staff ID format
        if (!userId.startsWith("ST")) {
            JOptionPane.showMessageDialog(this,
                "Invalid Staff ID format. Must start with 'ST'",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Get existing user to preserve other data
            User existingUser = userService.getUserById(userId);
            if (existingUser == null) {
                JOptionPane.showMessageDialog(this,
                    "User not found in the system",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create updated user object
            User updatedUser = new Staff(
                userId,
                password,
                existingUser.getFullName(),
                email,
                existingUser.getSecurityKeyword()
            );

            // Update the user
            userService.updateUser(updatedUser);
            JOptionPane.showMessageDialog(this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error updating profile: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backButtonActionPerformed() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            ((JFrame) window).getContentPane().removeAll();
            ((JFrame) window).getContentPane().add(new StaffDashboardPanel(staffId));
            ((JFrame) window).getContentPane().revalidate();
            ((JFrame) window).getContentPane().repaint();
        }
    }
}
