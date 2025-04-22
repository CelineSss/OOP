package gui;

import main.HostelManagementSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.panels.manager.PendingRegistrationPanel;
import gui.panels.manager.UserManagementPanel;
import gui.dialogs.HostelInfoDialog;
import gui.dialogs.HostelRulesDialog;
import gui.panels.manager.RateManagementPanel;
import gui.panels.manager.ManagerProfilePanel;

public class ManagerDashboardPanel extends JPanel {
    private final HostelManagementSystem mainWindow;
    private static final Color LIGHT_BLUE = new Color(230, 240, 255);
    private static final Color BUTTON_BLUE = new Color(200, 220, 255);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private String managerId;
    private JPanel contentPanel;

    public ManagerDashboardPanel(HostelManagementSystem mainWindow) {
        this.mainWindow = mainWindow;
        this.managerId = "AD123456"; // Default manager ID
        initializeUI();
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(LIGHT_BLUE);

        // Create header panel with home icon and welcome text
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create and add the main content panel
        contentPanel = createMainPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Home button
        JButton homeButton = new JButton("🏠");
        homeButton.setFont(new Font("Dialog", Font.PLAIN, 24));
        homeButton.setToolTipText("Back to Login");
        homeButton.setBackground(LIGHT_BLUE);
        homeButton.setBorderPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.setForeground(TEXT_COLOR);
        
        // Add logout functionality
        homeButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                mainWindow.showLoginPanel();
            }
        });

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome to Manager Menu");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(homeButton, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    public void showMainPanel() {
        removeAll();
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showPanel(JPanel panel) {
        removeAll();
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton[] buttons = {
            createStyledButton("Pending Registration", "📋", 250, 250),
            createStyledButton("User Management", "👥", 250, 250),
            createStyledButton("Rate Management", "💰", 250, 250),
            createStyledButton("Hostel Information", "🏠", 250, 250),
            createStyledButton("Rules & Regulations", "📜", 250, 250),
            createStyledButton("Profile", "👤", 250, 250)
        };

        for (JButton button : buttons) {
            panel.add(button);
        }

        // Add action listeners for each button
        buttons[0].addActionListener(e -> showPanel(new PendingRegistrationPanel()));
        buttons[1].addActionListener(e -> showPanel(new UserManagementPanel()));
        buttons[2].addActionListener(e -> showPanel(new RateManagementPanel()));
        buttons[3].addActionListener(e -> {
            HostelInfoDialog dialog = new HostelInfoDialog((JFrame)SwingUtilities.getWindowAncestor(this), true);
            dialog.setVisible(true);
        });
        buttons[4].addActionListener(e -> {
            HostelRulesDialog dialog = new HostelRulesDialog(SwingUtilities.getWindowAncestor(this), true);
            dialog.setVisible(true);
        });
        buttons[5].addActionListener(e -> showPanel(new ManagerProfilePanel()));

        return panel;
    }

    private JButton createStyledButton(String text, String icon, int width, int height) {
        JButton button = new JButton("<html><table width='" + (width-20) + "'><tr><td align='center'>" +
            "<div style='font-size:24px'>" + icon + "</div>" +
            "<div style='font-size:12px; margin-top:5px; white-space:nowrap'>" + text + "</div>" +
            "</td></tr></table></html>");
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(TEXT_COLOR);
        button.setBackground(BUTTON_BLUE);
        button.setBorder(BorderFactory.createLineBorder(new Color(150, 180, 255), 1));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(width, height));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(180, 200, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BLUE);
            }
        });
        
        return button;
    }
}
