package gui.panels.resident;

import javax.swing.*;
import java.awt.*;
import util.ImageLoader;
import main.HostelManagementSystem;
import gui.dialogs.HostelInfoDialog;
import gui.dialogs.HostelRulesDialog;

public class ResidentDashboardPanel extends JPanel {
    private final Color LIGHT_BLUE = new Color(230, 240, 255);
    private final Color BUTTON_BLUE = new Color(200, 220, 255);
    private final Color TEXT_COLOR = new Color(50, 50, 50);
    private JPanel contentPanel;
    private String residentId;

    public ResidentDashboardPanel(String residentId) {
        this.residentId = residentId;
        initializeUI();
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
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window instanceof JFrame) {
                    ((JFrame) window).dispose();
                    HostelManagementSystem system = new HostelManagementSystem();
                    system.setVisible(true);
                }
            }
        });

        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome to Resident Menu");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(homeButton, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBackground(LIGHT_BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton[] buttons = {
            createStyledButton("Update Account", "👤", 250, 250),
            createStyledButton("View Payment Records", "📊", 250, 250),
            createStyledButton("Hostel Information", "🏠", 250, 250),
            createStyledButton("Rules & Regulations", "📜", 250, 250),
            createStyledButton("", "", 250, 250),
            createStyledButton("", "", 250, 250)
        };

        // Hide the empty buttons
        buttons[4].setVisible(false);
        buttons[5].setVisible(false);

        for (JButton button : buttons) {
            panel.add(button);
        }

        // Add action listeners for each button
        buttons[0].addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                ((JFrame) window).getContentPane().removeAll();
                ((JFrame) window).getContentPane().add(new ResidentProfilePanel(residentId));
                ((JFrame) window).getContentPane().revalidate();
                ((JFrame) window).getContentPane().repaint();
            }
        });

        buttons[1].addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                ((JFrame) window).getContentPane().removeAll();
                ((JFrame) window).getContentPane().add(new ResidentViewReceiptPanel(residentId));
                ((JFrame) window).getContentPane().revalidate();
                ((JFrame) window).getContentPane().repaint();
            }
        });

        buttons[2].addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                HostelInfoDialog dialog = new HostelInfoDialog((JFrame) window, false);
                dialog.setVisible(true);
            }
        });

        buttons[3].addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                HostelRulesDialog dialog = new HostelRulesDialog(window, false);
                dialog.setVisible(true);
            }
        });

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