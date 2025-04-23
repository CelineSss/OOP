package util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;

public class ImageLoader {
    public static ImageIcon loadImage(String path, int width, int height) {
        try {
            // First try loading from classpath
            java.net.URL imageURL = ImageLoader.class.getResource(path);
            
            if (imageURL == null) {
                // If not found in classpath, try loading from project directory
                String projectPath = System.getProperty("user.dir");
                File imageFile = new File(projectPath + "/src" + path);
                
                if (imageFile.exists()) {
                    imageURL = imageFile.toURI().toURL();
                } else {
                    System.err.println("Image not found: " + path);
                    // Return a default placeholder image
                    return createPlaceholderImage(width, height);
                }
            }
            
            ImageIcon originalIcon = new ImageIcon(imageURL);
            Image originalImage = originalIcon.getImage();
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
            
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            // Return a default placeholder image
            return createPlaceholderImage(width, height);
        }
    }
    
    private static ImageIcon createPlaceholderImage(int width, int height) {
        // Create a simple placeholder image
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        
        // Fill background with a light gray
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);
        
        // Draw a border
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRect(0, 0, width - 1, height - 1);
        
        // Draw an "X" or some indication that it's a placeholder
        g2d.setColor(new Color(180, 180, 180));
        g2d.drawLine(0, 0, width, height);
        g2d.drawLine(0, height, width, 0);
        
        g2d.dispose();
        return new ImageIcon(placeholder);
    }
}