package util;

import model.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JOptionPane;

public class FileHandler {
    private static final String BASE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "data" + File.separator;
    private static final Logger LOGGER = Logger.getLogger(FileHandler.class.getName());
    
    // Define standard file names as constants
    public static final String MANAGER_FILE = "manager_acc.txt";
    public static final String STAFF_FILE = "staff_acc.txt";
    public static final String RESIDENT_FILE = "resident_acc.txt";
    public static final String PENDING_FILE = "pending_registration.txt";
    public static final String REJECT_FILE = "reject_registration.txt";

    public FileHandler() {
        File directory = new File(BASE_PATH);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created data directory at: " + directory.getAbsolutePath());
            } else {
                System.out.println("Failed to create data directory at: " + directory.getAbsolutePath());
            }
        }
        
        // Migrate data from old files to new format
        migrateOldData();
        
        // Initialize standard files
        initializeFile(MANAGER_FILE);
        initializeFile(STAFF_FILE);
        initializeFile(RESIDENT_FILE);
        initializeFile(PENDING_FILE);
        initializeFile(REJECT_FILE);
    }

    private void migrateOldData() {
        try {
            // Migrate residents data
            List<String> oldResidents = readFile("residents.txt");
            for (String line : oldResidents) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    // Format: name,userId,password,type,email,phone,date
                    // Convert to: userId,password,fullName,email,securityKeyword
                    String newFormat = String.format("%s,%s,%s,%s,%s",
                        parts[1].trim(),  // userId
                        parts[2].trim(),  // password
                        parts[0].trim(),  // fullName
                        parts[4].trim(),  // email
                        parts[5].trim()); // phone as security keyword
                    
                    List<String> newUser = new ArrayList<>();
                    newUser.add(newFormat);
                    writeFile(RESIDENT_FILE, newUser);
                }
            }
            
            // Delete old residents file
            deleteIfExists("residents.txt");
            
            // Migrate users data (if it contains staff information)
            List<String> oldUsers = readFile("users.txt");
            for (String line : oldUsers) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    // Format: name,userId,password,type,email,phone,date
                    String type = parts[3].trim();
                    String targetFile = type.equalsIgnoreCase("Staff") ? STAFF_FILE : 
                                     type.equalsIgnoreCase("Resident") ? RESIDENT_FILE : null;
                    
                    if (targetFile != null) {
                        // Convert to: userId,password,fullName,email,securityKeyword
                        String newFormat = String.format("%s,%s,%s,%s,%s",
                            parts[1].trim(),  // userId
                            parts[2].trim(),  // password
                            parts[0].trim(),  // fullName
                            parts[4].trim(),  // email
                            parts[5].trim().replace("-", "")); // phone as security keyword, remove hyphens
                        
                        List<String> newUser = new ArrayList<>();
                        newUser.add(newFormat);
                        writeFile(targetFile, newUser);
                    }
                }
            }
            
            // Delete old users file
            deleteIfExists("users.txt");
            
        } catch (Exception e) {
            System.out.println("Error during data migration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteIfExists(String fileName) {
        File file = new File(BASE_PATH + fileName);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Deleted old format file: " + fileName);
            } else {
                System.out.println("Failed to delete old format file: " + fileName);
            }
        }
    }

    private void initializeFile(String fileName) {
        File file = new File(BASE_PATH + fileName);
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    System.out.println("Created file: " + file.getAbsolutePath());
                    writeFile(fileName, new ArrayList<>());
                } else {
                    System.out.println("Failed to create file: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                System.out.println("Error creating file " + fileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(BASE_PATH + fileName);
        System.out.println("Reading file: " + file.getAbsolutePath());
        
        try {
            if (!file.exists()) {
                System.out.println("File does not exist, creating: " + file.getAbsolutePath());
                initializeFile(fileName);
            }
 
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            System.out.println("Read " + lines.size() + " lines from " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error accessing file: " + fileName, e);
            JOptionPane.showMessageDialog(null, 
                "Error accessing file: " + fileName + "\nError: " + e.getMessage(),
                "File Access Error",
                JOptionPane.ERROR_MESSAGE);
        }
        return lines;
    }

    public void writeFile(String fileName, List<String> content) {
        File file = new File(BASE_PATH + fileName);
        try {
            file.getParentFile().mkdirs();
            
            // Write directly to file, replacing all content
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
                for (String line : content) {
                    if (line != null && !line.trim().isEmpty()) {
                        writer.println(line);
                        System.out.println("Writing to " + fileName + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file: " + fileName, e);
            JOptionPane.showMessageDialog(null, 
                "Error writing to file: " + fileName + "\nError: " + e.getMessage(),
                "File Write Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void savePendingRegistration(User user) {
        if (user == null) {
            String errorMsg = "User cannot be null";
            LOGGER.severe(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        if (isUserExistsInAnyFile(user.getUserId())) {
            String errorMsg = "User ID already exists in the system";
            LOGGER.warning(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        List<String> pendingRegistrations = readFile(PENDING_FILE);
        
        boolean alreadyPending = pendingRegistrations.stream()
            .anyMatch(reg -> reg.startsWith(user.getUserId() + ","));
            
        if (alreadyPending) {
            String errorMsg = "Registration already pending for user: " + user.getUserId();
            LOGGER.warning(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        List<String> rejectedRegistrations = readFile(REJECT_FILE);
        boolean wasRejected = rejectedRegistrations.stream()
            .anyMatch(reg -> reg.startsWith(user.getUserId() + ","));
            
        if (wasRejected) {
            rejectedRegistrations.removeIf(reg -> reg.startsWith(user.getUserId() + ","));
            writeFile(REJECT_FILE, rejectedRegistrations);
            LOGGER.info("Removed rejected registration for user: " + user.getUserId());
        }

        String userRecord = String.format("%s,%s,%s,%s,%s",
            user.getUserId(),
            user.getPassword(),
            user.getFullName(),
            user.getEmail(),
            user.getSecurityKeyword());
            
        pendingRegistrations.add(userRecord);
        writeFile(PENDING_FILE, pendingRegistrations);
        LOGGER.info("Saved pending registration for user: " + user.getUserId());
    }
    
    private boolean isUserExistsInAnyFile(String userId) {
        return isUserExistsInFile(userId, MANAGER_FILE) ||
               isUserExistsInFile(userId, STAFF_FILE) ||
               isUserExistsInFile(userId, RESIDENT_FILE);
    }

    private boolean isUserExistsInFile(String userId, String fileName) {
        List<String> users = readFile(fileName);
        return users.stream().anyMatch(user -> user.startsWith(userId + ","));
    }

    public void saveUser(User user) {
        if (user == null) {
            String errorMsg = "User cannot be null";
            LOGGER.severe(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        String fileName = getFileNameForUser(user);
        if (fileName == null) {
            String errorMsg = "Invalid user type";
            LOGGER.severe(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        String userRecord = String.format("%s,%s,%s,%s,%s",
            user.getUserId(),
            user.getPassword(),
            user.getFullName(),
            user.getEmail(),
            user.getSecurityKeyword());
            
        List<String> users = readFile(fileName);
        List<String> updatedUsers = new ArrayList<>();
        
        boolean userExists = false;
        for (String existingUser : users) {
            if (existingUser.startsWith(user.getUserId() + ",")) {
                updatedUsers.add(userRecord); // Replace with new record
                userExists = true;
            } else {
                updatedUsers.add(existingUser); // Keep existing user
            }
        }
        
        if (!userExists) {
            updatedUsers.add(userRecord); // Add new user
        }
        
        // Write all users back to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(BASE_PATH + fileName)))) {
            for (String line : updatedUsers) {
                writer.println(line);
                System.out.println("Writing user to " + fileName + ": " + line);
            }
        } catch (IOException e) {
            LOGGER.severe("Error saving user: " + e.getMessage());
            throw new RuntimeException("Error saving user", e);
        }
    }

    private String getFileNameForUser(User user) {
        if (user instanceof Manager) return MANAGER_FILE;
        if (user instanceof Staff) return STAFF_FILE;
        if (user instanceof Resident) return RESIDENT_FILE;
        return null;
    }

    // Add getter for BASE_PATH
    public String getBasePath() {
        return BASE_PATH;
    }
}