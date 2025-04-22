package gui.dialogs;

import service.HostelInfoService;
import util.UIStyler;
import javax.swing.*;
import java.awt.*;

public class HostelRulesDialog extends JDialog {
    private final boolean isManager;
    private final JTextArea rulesTextArea;
    private final Window parent;

    public HostelRulesDialog(Window owner, boolean isManager) {
        super(owner, "Hostel Rules", ModalityType.APPLICATION_MODAL);
        this.parent = owner;
        this.isManager = isManager;
        this.rulesTextArea = new JTextArea();
        initializeUI();
    }

    public void refreshContent() {
        rulesTextArea.setText(HostelInfoService.getFormattedHostelRules());
        rulesTextArea.setCaretPosition(0);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 500);
        setLocationRelativeTo(getOwner());

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        UIStyler.stylePanel(mainPanel);

        // Title panel with back button
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
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

        // Center title
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        UIStyler.stylePanel(centerPanel);
        JLabel titleLabel = new JLabel("Hostel Rules & Regulations");
        UIStyler.styleTitleLabel(titleLabel);
        centerPanel.add(titleLabel);

        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Setup text area
        UIStyler.styleTextArea(rulesTextArea);
        rulesTextArea.setEditable(false);
        rulesTextArea.setLineWrap(true);
        rulesTextArea.setWrapStyleWord(true);
        rulesTextArea.setText(HostelInfoService.getFormattedHostelRules());

        // Add text area to scroll pane
        JScrollPane scrollPane = new JScrollPane(rulesTextArea);
        scrollPane.getViewport().setBackground(UIStyler.BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        UIStyler.stylePanel(buttonPanel);

        if (isManager) {
            JButton modifyButton = new JButton("Modify Rules");
            Dimension buttonSize = new Dimension(150, 30);
            UIStyler.styleButton(modifyButton, buttonSize);
            modifyButton.addActionListener(e -> openEditDialog());
            buttonPanel.add(modifyButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        add(mainPanel);
    }

    private void openEditDialog() {
        HostelRulesEditDialog editDialog = new HostelRulesEditDialog(parent);
        editDialog.setVisible(true);
        refreshContent();
    }
}
