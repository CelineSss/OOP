package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import service.RateService;
import util.FileHandler;
import model.Rate;
import util.UIStyler;

public class RateDialog extends JDialog {
    private final RateService rateService;
    private final FileHandler fileHandler;
    private final String editRateId;
    private final String editRateType;
    private boolean confirmed = false;

    private JComboBox<String> rateTypeCombo;
    private JTextField descriptionField;
    private JTextField amountField;

    public RateDialog(Window owner, String title) {
        this(owner, title, null, null);
    }

    public RateDialog(Window owner, String title, String rateId, String rateType) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.rateService = new RateService();
        this.fileHandler = new FileHandler();
        this.editRateId = rateId;
        this.editRateType = rateType;
        initializeUI();
        if (editRateId != null) {
            loadRateData();
        }
    }

    private void initializeUI() {
        setSize(400, 300);
        setLocationRelativeTo(getOwner());

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        UIStyler.stylePanel(mainPanel);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UIStyler.stylePanel(formPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Rate Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel typeLabel = new JLabel("Rate Type:");
        UIStyler.styleLabel(typeLabel);
        formPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        rateTypeCombo = new JComboBox<>(new String[]{"Rental", "Utility", "Penalty"});
        UIStyler.styleComboBox(rateTypeCombo);
        rateTypeCombo.setEnabled(editRateId == null);
        formPanel.add(rateTypeCombo, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descLabel = new JLabel("Description:");
        UIStyler.styleLabel(descLabel);
        formPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        UIStyler.styleTextField(descriptionField);
        formPanel.add(descriptionField, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel amountLabel = new JLabel("Amount (RM):");
        UIStyler.styleLabel(amountLabel);
        formPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        amountField = new JTextField(20);
        UIStyler.styleTextField(amountField);
        formPanel.add(amountField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        UIStyler.stylePanel(buttonPanel);

        Dimension buttonSize = new Dimension(100, 30);
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        UIStyler.styleButton(saveButton, buttonSize);
        UIStyler.styleButton(cancelButton, buttonSize);

        saveButton.addActionListener(e -> handleSave());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadRateData() {
        try {
            String fileName = editRateType.toLowerCase().contains("rental") ? "rent_rate.txt" :
                            editRateType.toLowerCase().contains("utility") ? "utility_rate.txt" :
                            "penalty_rates.txt";

            java.util.List<String> rates = fileHandler.readFile(fileName);
            for (String rate : rates) {
                if (rate.startsWith(editRateId + ",")) {
                    String[] parts = rate.split(",");
                    if (parts.length >= 4) {
                        rateTypeCombo.setSelectedItem(parts[1]);
                        descriptionField.setText(parts[2]);
                        amountField.setText(String.valueOf(parts[3].replaceAll("[^\\d.]", "")));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            showError("Error loading rate data: " + e.getMessage());
        }
    }

    private void handleSave() {
        try {
            String description = descriptionField.getText().trim();
            String amountStr = amountField.getText().trim();
            String rateType = (String) rateTypeCombo.getSelectedItem();

            if (description.isEmpty() || amountStr.isEmpty()) {
                showError("All fields are required.");
                return;
            }

            if (!amountStr.matches("^\\d+(\\.\\d{0,2})?$")) {
                showError("Invalid amount format. Please enter an amount with 0-2 decimal places (e.g., 100, 100.5, or 100.50)");
                return;
            }

            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showError("Amount must be greater than 0.");
                return;
            }

            String id = editRateId != null ? editRateId : rateService.generateNewRateId(getRateFileName(rateType));
            Rate rate = new Rate(id, rateType, description, amount);

            if (editRateId != null) {
                // For editing, we'll use the same save methods since they handle updates
                if (editRateType.equalsIgnoreCase("rental")) {
                    rateService.saveRentalRate(rate);
                } else if (editRateType.equalsIgnoreCase("utility")) {
                    rateService.saveUtilityRate(rate);
                } else {
                    rateService.savePenaltyRate(rate);
                }
            } else {
                if (rateType.equalsIgnoreCase("rental")) {
                    rateService.saveRentalRate(rate);
                } else if (rateType.equalsIgnoreCase("utility")) {
                    rateService.saveUtilityRate(rate);
                } else {
                    rateService.savePenaltyRate(rate);
                }
            }

            confirmed = true;
            dispose();
        } catch (Exception e) {
            showError("Error saving rate: " + e.getMessage());
        }
    }

    private String getRateFileName(String rateType) {
        return switch (rateType.toLowerCase()) {
            case "rental" -> "rent_rate.txt";
            case "utility" -> "utility_rate.txt";
            case "penalty" -> "penalty_rates.txt";
            default -> throw new IllegalArgumentException("Invalid rate type");
        };
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

    
