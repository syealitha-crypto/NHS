package com.nhs.view;

import com.nhs.model.*;
import com.nhs.util.ImageUtil;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GenericAddEditDialog extends JDialog {

    private Object item;
    private Map<String, JTextField> fields = new HashMap<>();
    private String[] fieldNames;
    private String[] fieldLabels;
    private boolean isEditMode;

    public GenericAddEditDialog(JFrame parent, String title, Object existingItem,
                                String[] labels, String[] names, Class<?> clazz) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        this.isEditMode = existingItem != null;
        this.item = existingItem;
        this.fieldLabels = labels;
        this.fieldNames = names;

        initComponents();
        if (isEditMode) {
            populateFields();
        }
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(550, 450);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 94, 184));
        header.setPreferredSize(new Dimension(550, 60));
        JLabel title = new JLabel(getTitle(), JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < fieldLabels.length; i++) {
            // Label
            gbc.gridx = 0; gbc.gridy = i; gbc.gridwidth = 1;
            JLabel label = new JLabel(fieldLabels[i] + ":");
            label.setFont(new Font("Arial", Font.BOLD, 12));
            form.add(label, gbc);

            // Field
            gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField field = new JTextField(20);
            field.setFont(new Font("Arial", Font.PLAIN, 12));
            fields.put(fieldNames[i], field);
            form.add(field, gbc);
        }

        JScrollPane scroll = new JScrollPane(form);
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.setBackground(Color.WHITE);

        JButton saveBtn = new JButton("Save", ImageUtil.loadScaledImage("save_icon.png", 20, 20));
        saveBtn.setBackground(new Color(76, 175, 80));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 14));
        saveBtn.setPreferredSize(new Dimension(100, 35));
        saveBtn.setOpaque(true);
        saveBtn.setBorderPainted(false);
        saveBtn.addActionListener(e -> dispose());

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.LIGHT_GRAY);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.addActionListener(e -> {
            fields.clear(); // Clear fields on cancel
            dispose();
        });

        buttons.add(cancelBtn);
        buttons.add(saveBtn);
        add(buttons, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(saveBtn);
    }

    private void populateFields() {
        if (item != null) {
            try {
                // Use reflection to get existing values
                if (item instanceof Patient) {
                    Patient p = (Patient) item;
                    setFieldValue("firstName", p.getFirstName());
                    setFieldValue("lastName", p.getLastName());
                    setFieldValue("nhsNumber", p.getNhsNumber());
                    setFieldValue("phoneNumber", p.getPhoneNumber());
                    setFieldValue("dateOfBirth", p.getDateOfBirth());
                } else if (item instanceof Clinician) {
                    Clinician c = (Clinician) item;
                    setFieldValue("firstName", c.getFirstName());
                    setFieldValue("lastName", c.getLastName());
                    setFieldValue("speciality", c.getSpeciality());
                } else if (item instanceof Facility) {
                    Facility f = (Facility) item;
                    setFieldValue("facilityName", f.getFacilityName());
                    setFieldValue("facilityType", f.getFacilityType());
                    setFieldValue("address", f.getAddress());
                } else if (item instanceof Appointment) {
                    Appointment a = (Appointment) item;
                    setFieldValue("patientId", a.getPatientId());
                    setFieldValue("clinicianId", a.getClinicianId());
                    setFieldValue("date", a.getAppointmentDate());
                    setFieldValue("time", a.getAppointmentTime());
                } else if (item instanceof Prescription) {
                    Prescription p = (Prescription) item;
                    setFieldValue("patientId", p.getPatientId());
                    setFieldValue("medication", p.getMedicationName());
                    setFieldValue("dosage", p.getDosage());
                    setFieldValue("frequency", p.getFrequency());
                } else if (item instanceof Referral) {
                    Referral r = (Referral) item;
                    setFieldValue("patientId", r.getPatientId());
                    setFieldValue("reason", r.getReferralReason());
                    setFieldValue("urgency", r.getUrgencyLevel());
                } else if (item instanceof Staff) {
                    Staff s = (Staff) item;
                    setFieldValue("firstName", s.getFirstName());
                    setFieldValue("lastName", s.getLastName());
                    setFieldValue("role", s.getRole());
                    setFieldValue("department", s.getDepartment());
                }
            } catch (Exception e) {
                System.err.println("Error populating fields: " + e.getMessage());
            }
        }
    }

    private void setFieldValue(String fieldName, String value) {
        JTextField field = fields.get(fieldName);
        if (field != null && value != null) {
            field.setText(value);
        }
    }

    public Map<String, String> getFieldValues() {
        Map<String, String> values = new HashMap<>();
        for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
            values.put(entry.getKey(), entry.getValue().getText().trim());
        }
        return values;
    }

    public Object getItem() { return item; }
}
