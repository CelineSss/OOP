package model;

public class User {
    protected String userId;
    protected String password;
    protected String fullName;
    protected String email;
    private String securityKeyword;

    public User(String userId, String password, String fullName, String email, String securityKeyword) {
        this.userId = userId;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.securityKeyword = securityKeyword;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getSecurityKeyword() {return securityKeyword; }

    // Setters
    public void setUserId(String userId) { this.userId = userId; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setSecurityKeyword(String securityKeyword) { this.securityKeyword = securityKeyword; }

    @Override
    public String toString() {
        return userId + "," + password + "," + fullName + "," + email;
    }
}