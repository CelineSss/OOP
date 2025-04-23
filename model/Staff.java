package model;

public class Staff extends User {
    public Staff(String userId, String password, String fullName, String email, String securityKeyword) {
        super(userId, password, fullName, email, securityKeyword);
    }
}