package model;

public class HostelRules {
    private String[] generalConduct;
    private String[] checkInOut;
    private String[] latePaymentPolicy;
    private String[] importantNotes;

    public HostelRules() {
        initializeDefaultRules();
    }

    private void initializeDefaultRules() {
        this.generalConduct = new String[]{
            "Residents must adhere to the university's code of conduct at all times.",
            "Noise levels should be kept to a minimum to maintain a peaceful environment.\nQuiet hours are observed from 10:00 PM to 7:00 AM.",
            "Respect for fellow residents, staff, and university property is mandatory.",
            "Acts of vandalism, harassment, or any behavior that disrupts the hostel\nenvironment will result in disciplinary action."
        };

        this.checkInOut = new String[]{
            "Students must complete the check-in process at the Hostel Office upon arrival\nand receive their room key and identification card.",
            "Room changes are permitted only with prior approval from the Hostel Office.",
            "Residents must vacate their rooms within 48 hours after their academic\nprogram ends or as instructed by the administration."
        };

        this.latePaymentPolicy = new String[]{
            "A flat penalty fee of RM 50 will be imposed for late payment.",
            "An additional 5% of the total outstanding amount will be charged\nfor each week the payment remains overdue.",
            "Services such as Wi-Fi and laundry facilities will be suspended\nfor overdue payments.",
            "Payments delayed beyond 30 days may result in eviction."
        };

        this.importantNotes = new String[]{
            "Suspension of Services:",
            "Residents with overdue payments will have services such as Wi-Fi, laundry facilities,\nand common area access suspended until all dues are cleared.",
            "Extended Delays:",
            "Payments delayed beyond 30 days will result in further disciplinary action,\nincluding potential eviction from the hostel."
        };
    }

    public void updateRules(String[] generalConduct, String[] checkInOut, 
                          String[] latePaymentPolicy, String[] importantNotes) {
        if (generalConduct != null) this.generalConduct = generalConduct;
        if (checkInOut != null) this.checkInOut = checkInOut;
        if (latePaymentPolicy != null) this.latePaymentPolicy = latePaymentPolicy;
        if (importantNotes != null) this.importantNotes = importantNotes;
    }

    // Getters
    public String[] getGeneralConduct() { return generalConduct; }
    public String[] getCheckInOut() { return checkInOut; }
    public String[] getLatePaymentPolicy() { return latePaymentPolicy; }
    public String[] getImportantNotes() { return importantNotes; }
}
