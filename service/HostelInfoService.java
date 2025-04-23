package service;

import model.HostelInfo;
import util.FileHandler;
import java.util.ArrayList;
import java.util.List;

public class HostelInfoService {
    private static final FileHandler fileHandler = new FileHandler();
    private static final String HOSTEL_INFO_FILE = "hostel_info.txt";
    private static final String HOSTEL_RULES_FILE = "hostel_rules.txt";

    public static String getFormattedHostelInfo() {
        List<String> lines = fileHandler.readFile(HOSTEL_INFO_FILE);
        StringBuilder content = new StringBuilder();
        for (String line : lines) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

    public static String getFormattedHostelRules() {
        List<String> lines = fileHandler.readFile(HOSTEL_RULES_FILE);
        StringBuilder content = new StringBuilder();
        for (String line : lines) {
            content.append(line).append("\n");
        }
        return content.toString();
    }

    public static void updateHostelInfo(String location, String[] facilities, 
                                      HostelInfo.RoomType[] roomTypes, 
                                      String[] amenities, 
                                      String[] commonFacilities, 
                                      String[] loungeHours) {
        List<String> lines = new ArrayList<>();
        
        // Add location
        lines.add("Location: " + location);
        lines.add("");
        
        // Add facilities
        lines.add("Facilities:");
        for (String facility : facilities) {
            lines.add("- " + facility);
        }
        lines.add("");
        
        // Add room types
        lines.add("Room Types:");
        int count = 1;
        for (HostelInfo.RoomType room : roomTypes) {
            lines.add(count + ". " + room.getName());
            lines.add("- Rate: RM" + room.getRatePerMonth());
            lines.add("- Size: " + room.getSizeInSqFt());
            lines.add("");
            count++;
        }
        
        // Add amenities
        lines.add("Room Amenities:");
        for (String amenity : amenities) {
            lines.add("- " + amenity);
        }
        lines.add("");
        
        // Add common facilities
        lines.add("Common Facilities:");
        for (String facility : commonFacilities) {
            lines.add("- " + facility);
        }
        lines.add("");
        
        // Add lounge hours
        lines.add("Lounge Hours:");
        for (String hours : loungeHours) {
            lines.add("- " + hours);
        }
        
        fileHandler.writeFile(HOSTEL_INFO_FILE, lines);
    }

    public static void updateHostelRules(String[] generalConduct, 
                                       String[] checkInOut, 
                                       String[] latePayment, 
                                       String[] importantNotes) {
        List<String> lines = new ArrayList<>();
        
        // Add general conduct
        lines.add("GENERAL CONDUCT:");
        for (String rule : generalConduct) {
            lines.add("- " + rule);
        }
        lines.add("");
        
        // Add check-in/out policies
        lines.add("CHECK-IN AND CHECK-OUT POLICIES:");
        for (String policy : checkInOut) {
            lines.add("- " + policy);
        }
        lines.add("");
        
        // Add late payment policy
        lines.add("LATE PAYMENT POLICY:");
        for (String policy : latePayment) {
            lines.add("- " + policy);
        }
        lines.add("");
        
        // Add important notes
        lines.add("IMPORTANT NOTES:");
        for (String note : importantNotes) {
            lines.add("- " + note);
        }
        
        fileHandler.writeFile(HOSTEL_RULES_FILE, lines);
    }
}