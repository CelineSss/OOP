package service;

import model.*;
import util.FileHandler;
import java.util.*;

public class UserService {
    private final FileHandler fileHandler;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MIN_DIGITS_IN_PASSWORD = 3;
    public static final int MAX_EMAIL_ATTEMPTS = 3;
    private int emailAttempts = 0;
    private Map<String, Integer> loginAttempts = new HashMap<>();

    public UserService() {
        this.fileHandler = new FileHandler();
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void registerUser(String userId, String password, String fullName, String email, String securityKeyword) 
        throws MaxEmailAttemptsExecption, InvalidEmailExecption {
        validateRegistrationInputs(userId, password, fullName, email);
        
        if (!isValidSecurityKeyword(securityKeyword)) {
            throw new IllegalArgumentException("Security keyword must be at least 6 characters long");
        }
        
        User user = createUserObject(userId, password, fullName, email, securityKeyword);
        
        if (isUserExists(userId)) {
            throw new IllegalArgumentException("An account with this User ID already exists in the system");
        }
        fileHandler.savePendingRegistration(user);
    }

    private void validateRegistrationInputs(String userId, String password, String fullName, String email) 
        throws MaxEmailAttemptsExecption, InvalidEmailExecption {
        List<String> errors = new ArrayList<>();

        if (userId == null || userId.trim().isEmpty()) {
            errors.add("User ID cannot be empty");
        } else if (!isValidUserId(userId)) {
            errors.add("Invalid User ID format. Must be TPxxxxxx for residents or STxxxxxx for staff");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.add("Password cannot be empty");
        } else {
            if (password.length() < MIN_PASSWORD_LENGTH) {
                errors.add("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
            }
            if (!password.matches(".*[A-Z].*")) {
                errors.add("Password must contain at least one uppercase letter");
            }
            if (!password.matches(".*\\d.*\\d.*\\d.*")) {
                errors.add("Password must contain at least " + MIN_DIGITS_IN_PASSWORD + " digits");
            }
            if (password.contains(" ")) {
                errors.add("Password cannot contain spaces");
            }
        }
                if (fullName == null || fullName.trim().isEmpty()) {
            errors.add("Full name cannot be empty");
        } else if (!isValidFullName(fullName)) {
            errors.add("Full name can only contain letters and spaces");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email cannot be empty");
        } else if (!isValidEmail(email)) {
            emailAttempts++;
            if (emailAttempts >= MAX_EMAIL_ATTEMPTS) {
                throw new MaxEmailAttemptsExecption("Maximum email attempts reached (" + 
                    MAX_EMAIL_ATTEMPTS + "). Registration failed.");
            }
            throw new InvalidEmailExecption("Invalid email format. Attempts remaining: " + 
                (MAX_EMAIL_ATTEMPTS - emailAttempts));
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Registration validation failed:\n" + 
                String.join("\n", errors));
        }
    }

    private User createUserObject(String userId, String password, String fullName, String email, String securityKeyword) {
        if (userId.startsWith("TP")) {
            return new Resident(userId, password, fullName, email, securityKeyword);
        } else if (userId.startsWith("ST")) {
            return new Staff(userId, password, fullName, email, securityKeyword);
        } else {
            throw new IllegalArgumentException("Invalid user type identifier");
        }
    }

    public boolean isUserExists(String userId) {
        String fileName = getFileNameForUserId(userId);
        if (fileName == null) return false;
        List<String> users = fileHandler.readFile(fileName);
        return users.stream().anyMatch(user -> user.startsWith(userId + ","));
    }

    public boolean login(String username, String password) {
        // Get current attempts for this username
        int attempts = loginAttempts.getOrDefault(username, 0);
        
        // Check if user is locked out
        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            loginAttempts.remove(username);  // Reset attempts when locked out
            throw new SecurityException("Account is locked. Please restart the application to try again.");
        }
        
        // Verify credentials
        boolean isValid = verifyCredentials(username, password);
        
        if (!isValid) {
            // Increment attempts on failed login
            attempts++;  // Increment first
            loginAttempts.put(username, attempts);  // Then store
            
            if (attempts >= MAX_LOGIN_ATTEMPTS) {
                loginAttempts.remove(username);  // Reset attempts when max is reached
                throw new SecurityException("Account is locked. Please restart the application to try again.");
            } else {
                int remainingAttempts = MAX_LOGIN_ATTEMPTS - attempts;
                throw new SecurityException("Invalid credentials. " + remainingAttempts + " attempts remaining.");
            }
        }
        
        // Reset attempts on successful login
        loginAttempts.remove(username);
        return true;
    }
    
    private boolean verifyCredentials(String username, String password) {
        String fileName = getFileNameForUserId(username);
        if (fileName == null) return false;
        List<String> lines = fileHandler.readFile(fileName);
        return lines.stream()
                   .map(line -> line.split(","))
                   .anyMatch(parts -> parts.length >= 2 && 
                                    parts[0].equals(username) && 
                                    parts[1].equals(password));
    }

    public boolean isValidUserId(String userId) {
        if (userId == null || userId.length() != 8) return false;
        return userId.matches("^(ST|TP)\\d{6}$");
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) return false;
        return password.matches(".*[A-Z].*") && 
               password.matches(".*\\d.*\\d.*\\d.*") && 
               !password.contains(" ");
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return false;
        return fullName.matches("^[a-zA-Z ]+$");
    }

    public boolean isValidSecurityKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return false;
        return keyword.trim().length() >= 6;
    }

    private String getFileNameForUserId(String userId) {
        if (userId == null) return null;
        if (userId.startsWith("AD")) return "manager_acc.txt";
        if (userId.startsWith("ST")) return "staff_acc.txt";
        if (userId.startsWith("TP")) return "resident_acc.txt";
        return null;
    }

    public List<String> getPendingRegistrations() {
        return fileHandler.readFile("pending_registration.txt");
    }

    public List<String> getRejectedRegistrations() {
        return fileHandler.readFile("reject_registration.txt");
    }

    public void approvePendingRegistration(String userId) {
        List<String> pendingRegistrations = getPendingRegistrations();
        Optional<String> registrationOpt = pendingRegistrations.stream()
                                                             .filter(reg -> reg.startsWith(userId + ","))
                                                             .findFirst();
        
        if (registrationOpt.isPresent()) {
            String registration = registrationOpt.get();
            String[] parts = registration.split(",");
            if (parts.length >= 5) {
                // First remove from pending
                pendingRegistrations.removeIf(reg -> reg.startsWith(userId + ","));
                fileHandler.writeFile("pending_registration.txt", pendingRegistrations);
                
                // Then save to appropriate file
                String targetFile = getFileNameForUserId(userId);
                List<String> existingUsers = fileHandler.readFile(targetFile);
                existingUsers.add(registration);
                fileHandler.writeFile(targetFile, existingUsers);
            } else {
                throw new IllegalArgumentException("Invalid registration data format");
            }
        } else {
            throw new IllegalArgumentException("No pending registration found for user ID: " + userId);
        }
    }

    public void rejectPendingRegistration(String userId) {
        List<String> pendingRegistrations = getPendingRegistrations();
        Optional<String> registrationOpt = pendingRegistrations.stream()
                                                             .filter(reg -> reg.startsWith(userId + ","))
                                                             .findFirst();
        
        if (registrationOpt.isPresent()) {
            String registration = registrationOpt.get();
            
            // Add to rejected list
            List<String> rejectedList = getRejectedRegistrations();
            rejectedList.add(registration);
            fileHandler.writeFile("reject_registration.txt", rejectedList);
            
            // Remove from pending
            pendingRegistrations.removeIf(reg -> reg.startsWith(userId + ","));
            fileHandler.writeFile("pending_registration.txt", pendingRegistrations);
        } else {
            throw new IllegalArgumentException("No pending registration found for user ID: " + userId);
        }
    }
    
    public String checkDetailedRegistrationStatus(String userId) {
        List<String> pendingUsers = fileHandler.readFile("pending_registration.txt");
        for (String user : pendingUsers) {
            if (user.startsWith(userId + ",")) {
                return "PENDING";
            }
        }

        List<String> rejectedUsers = fileHandler.readFile("reject_registration.txt");
        for (String user : rejectedUsers) {
            if (user.startsWith(userId + ",")) {
                return "REJECTED";
            }
        }

        String fileName = userId.startsWith("ST") ? "staff_acc.txt" : 
                         userId.startsWith("TP") ? "resident_acc.txt" : null;

        if (fileName != null) {
            List<String> approvedUsers = fileHandler.readFile(fileName);
            for (String user : approvedUsers) {
                if (user.startsWith(userId + ",")) {
                    return "APPROVED";
                }
            }
        }

        return "NOT_FOUND";
    }

    public boolean checkRegistrationStatus(String userId) {
        List<String> pendingRegistrations = fileHandler.readFile("pending_registration.txt");
        
        if (pendingRegistrations != null) {
            for (String registration : pendingRegistrations) {
                String[] parts = registration.split(",");
                if (parts.length > 0 && parts[0].equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }
        public int getRemainingEmailAttempts() {
        return MAX_EMAIL_ATTEMPTS - emailAttempts;
    }

    public void resetEmailAttempts() {
        emailAttempts = 0;
    }
    
    public boolean resetPassword(String userId, String keyword, String newPassword) {
        try {
            User user = getUserById(userId);
            
            if (user != null && user.getSecurityKeyword().equals(keyword)) {
                if (!isValidPassword(newPassword)) {
                    throw new IllegalArgumentException("Invalid new password format");
                }
                user.setPassword(newPassword);
                updateUser(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error resetting password", e);
        }
    }
    
    public User getUserById(String userId) {
        String fileName = getFileNameForUserId(userId);
        if (fileName == null) return null;
        
        List<String> users = fileHandler.readFile(fileName);
        System.out.println("Looking for user " + userId + " in file: " + fileName);
        System.out.println("File contents: " + String.join(", ", users));
        
        for (String userStr : users) {
            String[] parts = userStr.trim().split(",");
            System.out.println("Checking user record: " + userStr);
            // Check if this is the user we're looking for
            if (parts.length >= 5 && parts[0].trim().equals(userId.trim())) {
                System.out.println("Found matching user. Creating user object...");
                if (userId.startsWith("ST")) {
                    return new Staff(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim()
                    );
                } else if (userId.startsWith("TP")) {
                    return new Resident(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim()
                    );
                } else if (userId.startsWith("AD")) {
                    return new Manager(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim()
                    );
                }
            }
        }
        System.out.println("No matching user found for ID: " + userId);
        return null;
    }

    public void updateUser(User user) {
        String fileName = getFileNameForUserId(user.getUserId());
        if (fileName == null) {
            throw new IllegalArgumentException("Invalid user ID format");
        }

        List<String> users = fileHandler.readFile(fileName);
        boolean found = false;
        
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).startsWith(user.getUserId() + ",")) {
                users.set(i, String.format("%s,%s,%s,%s,%s",
                    user.getUserId(),
                    user.getPassword(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getSecurityKeyword()
                ));
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("User not found");
        }
        
        fileHandler.writeFile(fileName, users);
    }
}