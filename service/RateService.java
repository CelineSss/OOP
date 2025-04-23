package service;

import model.Rate;
import util.FileHandler;
import java.util.*;

public class RateService {
    private final FileHandler fileHandler;

    // Change the prefixes to single letters
    private static final String RENTAL_PREFIX = "R";
    private static final String UTILITY_PREFIX = "U";
    private static final String PENALTY_PREFIX = "P";

    public RateService() {
        this.fileHandler = new FileHandler();
    }

    private Rate loadRateFromString(String rateString) {
        String[] parts = rateString.split(",");
        if (parts.length >= 5) {
            return new Rate(parts[0], parts[1], parts[2], 
                          Double.parseDouble(parts[3]), parts[4]);
        }
        throw new IllegalArgumentException("Invalid rate format");
    }

    public void saveRentalRate(Rate rate) {
        List<String> rates = fileHandler.readFile("rent_rate.txt");
        List<String> updatedRates = new ArrayList<>();
        boolean exists = false;

        // Format the rate string correctly using Rate's toString()
        String rateString = rate.toString();

        // Check existing rates
        for (String existingRate : rates) {
            String[] parts = existingRate.split(",");
            if (parts.length > 0 && parts[0].equals(rate.getId())) {
                // Update existing rate
                updatedRates.add(rateString);
                exists = true;
            } else {
                updatedRates.add(existingRate);
            }
        }
        
        // Add new rate if it doesn't exist
        if (!exists) {
            updatedRates.add(rateString);
        }

        fileHandler.writeFile("rent_rate.txt", updatedRates);
    }

    public void saveUtilityRate(Rate rate) {
        List<String> rates = fileHandler.readFile("utility_rate.txt");
        List<String> updatedRates = new ArrayList<>();
        boolean exists = false;

        // Format the rate string correctly using Rate's toString()
        String rateString = rate.toString();

        // Check existing rates
        for (String existingRate : rates) {
            String[] parts = existingRate.split(",");
            if (parts.length > 0 && parts[0].equals(rate.getId())) {
                // Update existing rate
                updatedRates.add(rateString);
                exists = true;
            } else {
                updatedRates.add(existingRate);
            }
        }
        
        // Add new rate if it doesn't exist
        if (!exists) {
            updatedRates.add(rateString);
        }

        fileHandler.writeFile("utility_rate.txt", updatedRates);
    }

    public void savePenaltyRate(Rate rate) {
        List<String> rates = fileHandler.readFile("penalty_rates.txt");
        List<String> updatedRates = new ArrayList<>();
        boolean exists = false;

        // Format the rate string correctly using Rate's toString()
        String rateString = rate.toString();

        // If the file is empty, just add the new rate
        if (rates.isEmpty()) {
            updatedRates.add(rateString);
        } else {
            // Check existing rates
            for (String existingRate : rates) {
                String[] parts = existingRate.split(",");
                if (parts.length >= 2 && parts[0].equals(rate.getId())) {
                    // Update existing rate
                    updatedRates.add(rateString);
                    exists = true;
                } else {
                    updatedRates.add(existingRate);
                }
            }
            
            // Add new rate if it doesn't exist
            if (!exists) {
                updatedRates.add(rateString);
            }
        }

        fileHandler.writeFile("penalty_rates.txt", updatedRates);
    }

    public void updatePenaltyRate(String type, double newRate) {
        List<String> rates = fileHandler.readFile("penalty_rates.txt");
        List<String> updatedRates = new ArrayList<>();
        boolean found = false;

        // Generate new ID if needed
        String rateId = "001";
        if (!rates.isEmpty()) {
            rateId = generateNewRateId("penalty_rates.txt");
        }

        // Create new rate object with type as both type and description
        Rate newPenaltyRate = new Rate(rateId, "Penalty", type, newRate);

        // Update or add the rate
        if (rates.isEmpty()) {
            updatedRates.add(newPenaltyRate.toString());
        } else {
            for (String line : rates) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(type)) {
                    updatedRates.add(newPenaltyRate.toString());
                    found = true;
                } else {
                    updatedRates.add(line);
                }
            }
            
            if (!found) {
                updatedRates.add(newPenaltyRate.toString());
            }
        }

        fileHandler.writeFile("penalty_rates.txt", updatedRates);
        System.out.println("Penalty rate updated: " + newPenaltyRate.toString());
    }

    public String generateRateId(String rateType, int sequence) {
        String prefix;
        switch (rateType.toLowerCase()) {
            case "rental":
                prefix = RENTAL_PREFIX;
                break;
            case "utility":
                prefix = UTILITY_PREFIX;
                break;
            case "penalty":
                prefix = PENALTY_PREFIX;
                break;
            default:
                throw new IllegalArgumentException("Invalid rate type");
        }
        
        return prefix + String.format("%03d", sequence);
    }

    public String generateNewRateId(String fileName) {
        List<String> rates = fileHandler.readFile(fileName);
        int maxId = 0;
        
        for (String rate : rates) {
            String[] parts = rate.split(",");
            if (parts.length > 0) {
                try {
                    // Extract only the numeric part (skip the first character which is the prefix)
                    String numericPart = parts[0].substring(1);
                    int id = Integer.parseInt(numericPart);
                    maxId = Math.max(maxId, id);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        
        // Determine the prefix based on the filename
        String prefix;
        if (fileName.contains("rent")) {
            prefix = RENTAL_PREFIX;
        } else if (fileName.contains("utility")) {
            prefix = UTILITY_PREFIX;
        } else if (fileName.contains("penalty")) {
            prefix = PENALTY_PREFIX;
        } else {
            throw new IllegalArgumentException("Invalid file name");
        }
        
        return prefix + String.format("%03d", maxId + 1);
    }
}