package gui.dialogs;

import model.HostelInfo;
import service.HostelInfoService;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HostelInfoEditDialog extends JDialog {
    private final JTextArea textArea;

    public HostelInfoEditDialog(JFrame parent) {
        super(parent, "Edit Hostel Information", true);
        this.textArea = new JTextArea();
        initializeUI();
    }

    private void initializeUI() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Setup text area
        textArea.setEditable(true);
        textArea.setFont(new Font("Serif", Font.PLAIN, 16));
        textArea.setBackground(new Color(245, 245, 245));
        textArea.setText(HostelInfoService.getFormattedHostelInfo());

        JScrollPane scrollPane = new JScrollPane(textArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            if (saveChanges()) {
                JOptionPane.showMessageDialog(this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private boolean saveChanges() {
        String[] lines = textArea.getText().split("\n");
        String location = "";
        List<String> facilities = new ArrayList<>();
        List<HostelInfo.RoomType> roomTypes = new ArrayList<>();
        List<String> amenities = new ArrayList<>();
        List<String> commonFacilities = new ArrayList<>();
        List<String> loungeHours = new ArrayList<>();
        
        int section = 0; // 0=none, 1=facilities, 2=rooms, 3=amenities, 4=common, 5=lounge
        Pattern roomPattern = Pattern.compile("(\\d+)\\. (.+)\\n\\s*- Rate: RM(\\d+).*\\n\\s*- Size: (\\d+)");
        StringBuilder roomBuffer = new StringBuilder();
        
        try {
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("===") || line.startsWith("--") || 
                    line.startsWith("Press Enter")) continue;
                
                // Check for section headers and location
                if (line.startsWith("Location:")) {
                    location = line.substring("Location:".length()).trim();
                } else if (line.equals("Facilities:")) {
                    section = 1;
                } else if (line.equals("Room Types:")) {
                    section = 2;
                } else if (line.equals("Room Amenities:")) {
                    section = 3;
                } else if (line.equals("Common Facilities:")) {
                    section = 4;
                } else if (line.equals("Lounge Hours:")) {
                    section = 5;
                } else {
                    // Process content based on current section
                    switch (section) {
                        case 1: // Facilities
                            if (line.startsWith("-")) {
                                facilities.add(line.substring(1).trim());
                            }
                            break;
                            
                        case 2: // Room Types
                            if (line.startsWith("-")) {
                                roomBuffer.append(line).append("\n");
                            } else if (line.matches("\\d+\\..*")) {
                                if (roomBuffer.length() > 0) {
                                    parseAndAddRoomType(roomBuffer.toString(), roomTypes);
                                    roomBuffer.setLength(0);
                                }
                                roomBuffer.append(line).append("\n");
                            }
                            break;
                            
                        case 3: // Amenities
                            if (line.startsWith("-")) {
                                amenities.add(line.substring(1).trim());
                            }
                            break;
                            
                        case 4: // Common Facilities
                            if (line.startsWith("-")) {
                                commonFacilities.add(line.substring(1).trim());
                            }
                            break;
                            
                        case 5: // Lounge Hours
                            if (line.startsWith("-")) {
                                loungeHours.add(line.substring(1).trim());
                            }
                            break;
                    }
                }
            }
            
            // Process any remaining room buffer
            if (roomBuffer.length() > 0) {
                parseAndAddRoomType(roomBuffer.toString(), roomTypes);
            }

            // Update the hostel information through the service
            HostelInfoService.updateHostelInfo(
                location,
                facilities.toArray(new String[0]),
                roomTypes.toArray(new HostelInfo.RoomType[0]),
                amenities.toArray(new String[0]),
                commonFacilities.toArray(new String[0]),
                loungeHours.toArray(new String[0])
            );
            
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error saving changes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void parseAndAddRoomType(String roomText, List<HostelInfo.RoomType> roomTypes) {
        try {
            String[] lines = roomText.split("\n");
            if (lines.length >= 3) {
                String name = lines[0].substring(lines[0].indexOf(".") + 1).trim();
                String rateLine = lines[1].trim();
                String sizeLine = lines[2].trim();
                
                int rate = Integer.parseInt(rateLine.replaceAll(".*RM(\\d+).*", "$1"));
                int size = Integer.parseInt(sizeLine.replaceAll(".*: (\\d+).*", "$1"));
                
                roomTypes.add(new HostelInfo.RoomType(name, rate, size));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid room type format: " + roomText);
        }
    }
} 