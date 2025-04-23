package gui.dialogs;

import model.HostelInfo;
import service.HostelInfoService;
import util.UIStyler;
import util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HostelInfoDialog extends JDialog {
    private final boolean isManager;
    private final JTextArea textArea;
    private final JFrame parent;

    public HostelInfoDialog(JFrame parent, boolean isManager) {
        super(parent, "APU Hostel Information", true);
        this.parent = parent;
        this.isManager = isManager;
        this.textArea = new JTextArea();
        initializeUI();
        // Center the dialog on the screen
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(800, 900);
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        UIStyler.stylePanel(mainPanel);

        // Add title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        UIStyler.stylePanel(centerPanel);
        JLabel titleLabel = new JLabel("APU Hostel Information");
        UIStyler.styleTitleLabel(titleLabel);
        centerPanel.add(titleLabel);

        titlePanel.add(backButton, BorderLayout.WEST);
        titlePanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(titlePanel);
        
        // Add spacing between title and content
        mainPanel.add(Box.createVerticalStrut(10));

        // Setup text area
        UIStyler.styleTextArea(textArea);
        textArea.setEditable(false);
        textArea.setText(HostelInfoService.getFormattedHostelInfo());
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add text area to scroll pane
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.getViewport().setBackground(UIStyler.BACKGROUND_COLOR);
        textScrollPane.setBorder(BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR));
        textScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(textScrollPane);

        // Add some space before buttons
        mainPanel.add(Box.createVerticalStrut(10));

        // Create button panel with Modify button (for manager only)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        UIStyler.stylePanel(buttonPanel);
        
        if (isManager) {
            JButton modifyButton = new JButton("Modify Information");
            Dimension buttonSize = new Dimension(150, 30);
            UIStyler.styleButton(modifyButton, buttonSize);
            modifyButton.addActionListener(e -> openEditDialog());
            buttonPanel.add(modifyButton);
            
            // Add the button panel only if there's a modify button
            mainPanel.add(buttonPanel);
            mainPanel.add(Box.createVerticalStrut(20));
        }

        // Add room images
        addImagePanel(mainPanel);

        // Add the main panel to a scroll pane
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getViewport().setBackground(UIStyler.BACKGROUND_COLOR);
        mainScrollPane.setBorder(null);
        mainScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(mainScrollPane, BorderLayout.CENTER);
    }

    private void openEditDialog() {
        HostelInfoEditDialog editDialog = new HostelInfoEditDialog(parent);
        editDialog.setVisible(true);
    }

    public void refreshContent() {
        textArea.setText(HostelInfoService.getFormattedHostelInfo());
        textArea.setCaretPosition(0);
    }

    private void addImagePanel(JPanel mainPanel) {
        // Image panel
        JPanel mainImagePanel = new JPanel();
        mainImagePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR),
            "Room Images"
        ));
        mainImagePanel.setLayout(new BoxLayout(mainImagePanel, BoxLayout.Y_AXIS));
        UIStyler.stylePanel(mainImagePanel);

        // Define room categories and their image paths
        String[][] roomCategories = {
            {"Single Room", "/images/shared_1.png", "/images/shared_1b.png"},
            {"Twin Sharing", "/images/shared_2.png", "/images/shared_2b.png"},
            {"Triple Sharing", "/images/shared_4.png", "/images/shared_4b.png"},
            {"Resident's Lounge", "/images/lounge.png", "/images/loungeb.png"}
        };

        for (String[] category : roomCategories) {
            JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            UIStyler.stylePanel(categoryPanel);
            categoryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR),
                category[0]
            ));
            
            // Add images for this category
            for (int i = 1; i < category.length; i++) {
                ImageIcon imageIcon = ImageLoader.loadImage(category[i], 300, 200);
                if (imageIcon != null) {
                    JLabel imageLabel = new JLabel(imageIcon);
                    imageLabel.setBorder(BorderFactory.createLineBorder(UIStyler.TOGGLE_BORDER_COLOR));
                    categoryPanel.add(imageLabel);
                }
            }
            
            mainImagePanel.add(categoryPanel);
            mainImagePanel.add(Box.createVerticalStrut(10));
        }

        // Add the image panel directly to the main panel
        mainPanel.add(mainImagePanel);
    }

    private void saveChanges() {
        // TODO: Implement saving changes through HostelInfoService
        String updatedInfo = textArea.getText();
        // We'll need to parse this text and update the info in HostelInfoService
        JOptionPane.showMessageDialog(this, 
            "Changes saved successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}