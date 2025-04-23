package service;

import model.Resident;
import util.FileHandler;
import java.util.List;
import java.util.ArrayList;

public class ResidentService {
    private final FileHandler fileHandler;
    private static final String RESIDENTS_FILE = "resident_acc.txt";

    public ResidentService() {
        this.fileHandler = new FileHandler();
    }

    public List<Resident> getAllResidents() {
        List<String> lines = fileHandler.readFile(RESIDENTS_FILE);
        List<Resident> residents = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 5) {  
                Resident resident = new Resident(
                    parts[0],  // userId
                    parts[1],  // password
                    parts[2],  // fullName
                    parts[3],  // email
                    parts[4]   // securityKeyword
                );
                residents.add(resident);
            }
        }
        return residents;
    }

    public List<Resident> searchResidents(String searchTerm) {
        List<Resident> allResidents = getAllResidents();
        List<Resident> filteredResidents = new ArrayList<>();
        searchTerm = searchTerm.toLowerCase();

        for (Resident resident : allResidents) {
            if (resident.getUserId().toLowerCase().contains(searchTerm) ||
                resident.getFullName().toLowerCase().contains(searchTerm) ||
                resident.getEmail().toLowerCase().contains(searchTerm)) {
                filteredResidents.add(resident);
            }
        }
        return filteredResidents;
    }
}