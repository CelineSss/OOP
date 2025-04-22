package model;

public class Resident extends User {
    public Resident(String userId, String password, String fullName, String email, String securityKeyword) {
        super(userId, password, fullName, email, securityKeyword);
    }
}