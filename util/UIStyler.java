package util;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UIStyler {
    // Color scheme
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    public static final Color TITLE_COLOR = new Color(60, 72, 107);
    public static final Color BUTTON_COLOR = new Color(100, 141, 174);
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    public static final Color FIELD_BACKGROUND = new Color(255, 255, 255);
    public static final Color LABEL_COLOR = new Color(90, 103, 140);
    public static final Color LINK_COLOR = new Color(100, 141, 174);
    public static final Color TOGGLE_BUTTON_COLOR = new Color(240, 242, 245);
    public static final Color TOGGLE_BORDER_COLOR = new Color(210, 215, 220);
    public static final Color TABLE_HEADER_COLOR = new Color(240, 242, 245);
    public static final Color TABLE_GRID_COLOR = new Color(220, 223, 228);
    public static final Color SELECTION_COLOR = new Color(210, 225, 245);
    
    // Font settings
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    
    public static void styleTextField(JTextField textField) {
        textField.setBackground(FIELD_BACKGROUND);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(TOGGLE_BORDER_COLOR), 
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        textField.setFont(REGULAR_FONT);
        textField.setSelectionColor(SELECTION_COLOR);
        textField.setCaretColor(LABEL_COLOR);
    }
    
    public static void styleButton(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR.darker());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });
    }
    
    public static void styleToggleButton(JToggleButton button) {
        button.setMinimumSize(new Dimension(60, 25));
        button.setMaximumSize(new Dimension(60, 25));
        button.setPreferredSize(new Dimension(60, 25));
        button.setFont(SMALL_FONT);
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
    
    public static void styleTable(JTable table) {
        table.setFont(REGULAR_FONT);
        table.setSelectionBackground(SELECTION_COLOR);
        table.setSelectionForeground(LABEL_COLOR);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setRowHeight(25);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(LABEL_COLOR);
        header.setFont(BUTTON_FONT);
        header.setBorder(new LineBorder(TABLE_GRID_COLOR));
    }
    
    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
    }
    
    public static void styleLabel(JLabel label) {
        label.setForeground(LABEL_COLOR);
        label.setFont(REGULAR_FONT);
    }
    
    public static void styleTitleLabel(JLabel label) {
        label.setForeground(TITLE_COLOR);
        label.setFont(TITLE_FONT);
    }
    
    public static void styleTextArea(JTextArea textArea) {
        textArea.setFont(REGULAR_FONT);
        textArea.setBackground(FIELD_BACKGROUND);
        textArea.setForeground(LABEL_COLOR);
        textArea.setSelectionColor(SELECTION_COLOR);
        textArea.setCaretColor(LABEL_COLOR);
    }
    
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(FIELD_BACKGROUND);
        comboBox.setForeground(LABEL_COLOR);
        comboBox.setFont(REGULAR_FONT);
        ((JComponent) comboBox.getRenderer()).setBackground(FIELD_BACKGROUND);
    }
} 