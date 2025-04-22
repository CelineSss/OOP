package gui.dialogs;

import service.HostelInfoService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HostelRulesEditDialog extends JDialog {
    private final JTextArea textArea;

    public HostelRulesEditDialog(Window parent) {
        super(parent, "Edit Hostel Rules", ModalityType.APPLICATION_MODAL);
        this.textArea = new JTextArea();
        initializeUI();
    }

    private void initializeUI() {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Setup text area
        textArea.setEditable(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Serif", Font.PLAIN, 16));
        textArea.setBackground(new Color(245, 245, 245));
        textArea.setText(HostelInfoService.getFormattedHostelRules());

        JScrollPane scrollPane = new JScrollPane(textArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            saveChanges();
            dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void saveChanges() {
        String[] lines = textArea.getText().split("\n");
        List<String> generalConduct = new ArrayList<>();
        List<String> checkInOut = new ArrayList<>();
        List<String> latePayment = new ArrayList<>();
        List<String> importantNotes = new ArrayList<>();
        
        int section = 0; // 0=none, 1=general, 2=checkin, 3=payment, 4=notes
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            // Check for section headers
            if (line.contains("GENERAL CONDUCT:")) {
                section = 1;
                continue;
            } else if (line.contains("CHECK-IN AND CHECK-OUT POLICIES:")) {
                section = 2;
                continue;
            } else if (line.contains("LATE PAYMENT POLICY:")) {
                section = 3;
                continue;
            } else if (line.contains("IMPORTANT NOTES:")) {
                section = 4;
                continue;
            } else if (line.startsWith("===") || line.startsWith("---")) {
                continue;
            }
            
            // Remove bullet points if present
            if (line.startsWith("•") || line.startsWith("-")) {
                line = line.substring(1).trim();
            }
            
            // Add to appropriate section
            switch (section) {
                case 1:
                    if (!line.isEmpty()) generalConduct.add(line);
                    break;
                case 2:
                    if (!line.isEmpty()) checkInOut.add(line);
                    break;
                case 3:
                    if (!line.isEmpty()) latePayment.add(line);
                    break;
                case 4:
                    if (!line.isEmpty()) importantNotes.add(line);
                    break;
            }
        }
        
        try {
            HostelInfoService.updateHostelRules(
                generalConduct.toArray(new String[0]),
                checkInOut.toArray(new String[0]),
                latePayment.toArray(new String[0]),
                importantNotes.toArray(new String[0])
            );
            
            JOptionPane.showMessageDialog(this, 
                "Rules updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
                
            // Refresh the parent dialog if it exists
            if (getOwner() instanceof HostelRulesDialog) {
                ((HostelRulesDialog) getOwner()).refreshContent();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error saving rules: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 