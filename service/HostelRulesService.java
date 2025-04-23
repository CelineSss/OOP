package service;

import model.HostelRules;

public class HostelRulesService {
    private static final HostelRules hostelRules = new HostelRules();

    public static HostelRules getHostelRules() {
        return hostelRules;
    }

    public static String getFormattedRules() {
        HostelRules rules = getHostelRules();
        StringBuilder content = new StringBuilder();
        
        content.append("===== Hostel Rules and Regulations =====\n\n");
        
        content.append("General Conduct:\n");
        for (int i = 0; i < rules.getGeneralConduct().length; i++) {
            content.append(String.format("%d. %s\n", i + 1, rules.getGeneralConduct()[i]));
        }
        content.append("\n");
        
        content.append("Check-In and Check-Out:\n");
        for (int i = 0; i < rules.getCheckInOut().length; i++) {
            content.append(String.format("%d. %s\n", i + 1, rules.getCheckInOut()[i]));
        }
        content.append("\n");
        
        content.append("Late Payment Policy:\n");
        for (int i = 0; i < rules.getLatePaymentPolicy().length; i++) {
            content.append(String.format("%d. %s\n", i + 1, rules.getLatePaymentPolicy()[i]));
        }
        content.append("\n");
        
        content.append("Important Notes:\n");
        for (int i = 0; i < rules.getImportantNotes().length; i += 2) {
            content.append(String.format("%d. %s\n", (i/2) + 1, rules.getImportantNotes()[i]));
            content.append("   ").append(rules.getImportantNotes()[i + 1]).append("\n\n");
        }
        
        content.append("Press Enter key to return to Main Menu...");
        
        return content.toString();
    }
}