package model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Rate {
    private String id;
    private String type;
    private String description;
    private double amount;
    private LocalDate lastUpdated;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    public Rate(String id, String type, String description, double amount) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.lastUpdated = LocalDate.now();
    }
    
    // Add new constructor for parsing existing rates
    public Rate(String id, String type, String description, double amount, String dateStr) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        
        try {
            // First try dd-MM-yyyy format
            this.lastUpdated = LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                // If that fails, try yyyy-MM-dd format
                this.lastUpdated = LocalDate.parse(dateStr);
                // Convert to our desired format by formatting it again
                this.lastUpdated = LocalDate.parse(this.lastUpdated.format(DATE_FORMATTER), DATE_FORMATTER);
            } catch (DateTimeParseException e2) {
                // If all parsing fails, use current date
                this.lastUpdated = LocalDate.now();
            }
        }
    }

    public String getId() { return id; }
    public String getType() { return type; }    
    public double getAmount() { return amount; }
    public LocalDate getLastUpdated() { return lastUpdated; }
    public String getDescription() { return description; }
    
    public void setAmount(double amount) {
        this.amount = amount;
        this.lastUpdated = LocalDate.now();
    }
    
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%.1f,%s", 
            id, type, description, amount, lastUpdated.format(DATE_FORMATTER));
    }
}