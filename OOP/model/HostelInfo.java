package model;

public class HostelInfo {
    private String location;
    private String[] facilities;
    private RoomType[] roomTypes;
    private String[] amenities;
    private String[] commonFacilities;
    private String[] loungeHours;
    private RoomImages[] roomImages; 

    public HostelInfo() {
        initializeDefaultInfo();
    }

    public void updateInfo(String location, String[] facilities, RoomType[] roomTypes,
                         String[] amenities, String[] commonFacilities, String[] loungeHours) {
        if (location != null) this.location = location;
        if (facilities != null) this.facilities = facilities;
        if (roomTypes != null) this.roomTypes = roomTypes;
        if (amenities != null) this.amenities = amenities;
        if (commonFacilities != null) this.commonFacilities = commonFacilities;
        if (loungeHours != null) this.loungeHours = loungeHours;
    }

    //Inner class for organizing room images
    public static class RoomImages {
        private String category;
        private String[] imagePaths;

        public RoomImages(String category, String[] imagePaths) {
            this.category = category;
            this.imagePaths = imagePaths;
        }

        public String getCategory() { return category; }
        public String[] getImagePaths() { return imagePaths; }
    }

    private void initializeDefaultInfo() {
        this.location = "APU's Campus in Technology Park Malaysia";
        
        this.facilities = new String[]{
            "24/7 security and access card control",
            "SweatZone",
            "Swimming Pool",
            "Residence Lounge",
            "Kitchen on each floor with stove, microwave, and refrigerator",
            "Self-service laundry (pay per use)",
            "Study rooms and quiet zones",
            "Common areas cleaned twice weekly"
        };

        this.roomTypes = new RoomType[]{
            new RoomType("Single Room (Block A)", 1000, 180),
            new RoomType("Shared Room (2 persons) (Block B)", 700, 250),
            new RoomType("Shared Room (4 persons) (Block C)", 500, 500)
        };

        this.amenities = new String[]{
            "Attached bathroom",
            "Water heater",
            "Study table",
            "Table lamp",
            "Bed with divan",
            "Mini-fridge",
            "Wi-Fi",
            "Air-conditioning"
        };

        this.commonFacilities = new String[]{
            "Kitchen on each floor with stove, microwave, and refrigerator",
            "Self-service laundry (pay per use)",
            "Study rooms and quiet zones",
            "Common areas cleaned twice weekly"
        };

        this.loungeHours = new String[]{
            "Monday to Friday: 6:00 PM to 11:00 PM",
            "Weekends and Public Holidays: 10:00 AM to 11:00 PM"
        };

        // Initialize room images with categories
        this.roomImages = new RoomImages[]{
            new RoomImages("Single Room", new String[]{
                "/images/shared_1.png",
                "/images/shared_1b.png"
            }),
            new RoomImages("Shared (2) Room", new String[]{
                "/images/shared_2.png",
                "/images/shared_2b.png"
            }),
            new RoomImages("Shared (4) Room", new String[]{
                "/images/shared_4.png",
                "/images/shared_4b.png"
            }),
            new RoomImages("Resident's Lounge", new String[]{
                "/images/lounge.png",
                "/images/loungeb.png"
            })
        };
    }

    // Existing getters
    public String getLocation() { return location; }
    public String[] getFacilities() { return facilities; }
    public RoomType[] getRoomTypes() { return roomTypes; }
    public String[] getAmenities() { return amenities; }
    public String[] getCommonFacilities() { return commonFacilities; }
    public String[] getLoungeHours() { return loungeHours; }
    public RoomImages[] getRoomImages() { return roomImages; }  // New getter for roomImages

    public static class RoomType {
        private String name;
        private int ratePerMonth;
        private int sizeInSqFt;

        public RoomType(String name, int ratePerMonth, int sizeInSqFt) {
            this.name = name;
            this.ratePerMonth = ratePerMonth;
            this.sizeInSqFt = sizeInSqFt;
        }

        public String getName() { return name; }
        public int getRatePerMonth() { return ratePerMonth; }
        public int getSizeInSqFt() { return sizeInSqFt; }
    }
}

