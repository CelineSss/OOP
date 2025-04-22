package main;

import service.UserService;
import service.RateService;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import model.*;
import service.*;
import util.FileHandler;
import gui.LoginPanel;
import gui.RegistrationPanel;
import gui.ManagerDashboardPanel;
import gui.panels.staff.StaffDashboardPanel;
import gui.panels.resident.ResidentDashboardPanel;

public class HostelManagementSystem extends JFrame {
    private static final UserService USER_SERVICE = new UserService();
    private static final RateService RATE_SERVICE = new RateService();
    private static final FileHandler FILE_HANDLER = new FileHandler();
    private static final Logger LOGGER = Logger.getLogger(HostelManagementSystem.class.getName());
  
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;
    private ManagerDashboardPanel managerDashboardPanel;
    
    public HostelManagementSystem() {
        initializeFrame();
        initializePanels();
    }
    
    private void initializeFrame() {
        setTitle("APU Hostel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }
    
    private void initializePanels() {
        // Create panels
        loginPanel = new LoginPanel(this);
        registrationPanel = new RegistrationPanel(this);
        managerDashboardPanel = new ManagerDashboardPanel(this);
        
        // Add panels to card layout
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registrationPanel, "REGISTER");
        mainPanel.add(managerDashboardPanel, "DASHBOARD");
        
        // Show login panel by default
        showLoginPanel();
    }
    
    public void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    public void showRegistrationPanel() {
        cardLayout.show(mainPanel, "REGISTER");
    }
    
    public void showManagerDashboard(String managerId) {
        managerDashboardPanel.setManagerId(managerId);
        cardLayout.show(mainPanel, "DASHBOARD");
    }
    
    public void showStaffDashboard(String staffId) {
        mainPanel.removeAll();
        mainPanel.add(new StaffDashboardPanel(staffId), "STAFF_DASHBOARD");
        cardLayout.show(mainPanel, "STAFF_DASHBOARD");
        revalidate();
        repaint();
    }
    
    public void showResidentDashboard(String residentId) {
        mainPanel.removeAll();
        mainPanel.add(new ResidentDashboardPanel(residentId), "RESIDENT_DASHBOARD");
        cardLayout.show(mainPanel, "RESIDENT_DASHBOARD");
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        initializeSystem();
        SwingUtilities.invokeLater(() -> {
            HostelManagementSystem system = new HostelManagementSystem();
            system.setVisible(true);
        });
    }

    private static void initializeSystem() {
        try {
            initializeManagerAccount();
            initializeRates();
            LOGGER.info("System initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing system", e);
        }
    }
    
    private static void initializeManagerAccount() {
        List<String> users = FILE_HANDLER.readFile("manager_acc.txt");
        boolean managerExists = users.stream()
                .anyMatch(user -> user.split(",")[0].startsWith("AD"));
        
        if (!managerExists) {
            User manager = new Manager("AD001", "Admin123", "System Admin", "admin@apu.edu.my", "admin");
            users.add(String.format("%s,%s,%s,%s,%s",
                manager.getUserId(),
                manager.getPassword(),
                manager.getFullName(),
                manager.getEmail(),
                manager.getSecurityKeyword()));
            FILE_HANDLER.writeFile("manager_acc.txt", users);
            LOGGER.info("Default manager account created");
        }
    }
    
    private static void initializeRates() {
        List<String> rates = FILE_HANDLER.readFile("rent_rate.txt");
        if (rates.isEmpty()) {
            List<String> defaultRates = new ArrayList<>();
            defaultRates.add("R001,Single Room,800,2024-01-01");
            defaultRates.add("R002,Double Room,600,2024-01-01");
            defaultRates.add("R003,Triple Room,500,2024-01-01");
            FILE_HANDLER.writeFile("rent_rate.txt", defaultRates);
            LOGGER.info("Default rates initialized");
        }
    }
}


