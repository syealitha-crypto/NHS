package com.nhs.controller;

import com.nhs.model.Patient;
import com.nhs.view.PatientPanel;
import com.nhs.view.GenericAddEditDialog;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class PatientController {

    private PatientPanel view;
    private List<Patient> patients;
    private String csvFilePath = "data/patients.csv";

    public PatientController(PatientPanel view) {
        this.view = view;
        this.patients = new ArrayList<>();
        loadPatients();
        attachListeners();
    }

    private void loadPatients() {
        List<String[]> data = CSVReader.readCSV(csvFilePath);
        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                Patient patient = new Patient(
                        CSVReader.getValue(row, 0), CSVReader.getValue(row, 1),
                        CSVReader.getValue(row, 2), CSVReader.getValue(row, 3),
                        CSVReader.getValue(row, 4), CSVReader.getValue(row, 5),
                        CSVReader.getValue(row, 6), CSVReader.getValue(row, 7),
                        CSVReader.getValue(row, 8), CSVReader.getValue(row, 9),
                        CSVReader.getValue(row, 10), CSVReader.getValue(row, 11),
                        CSVReader.getValue(row, 12), CSVReader.getValue(row, 13)
                );
                patients.add(patient);
                addPatientToTable(patient);
            }
        }
    }

    private void addPatientToTable(Patient patient) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                patient.getPatientId(), patient.getFirstName(), patient.getLastName(),
                patient.getDateOfBirth(), patient.getNhsNumber(), patient.getPhoneNumber(),
                patient.getGpSurgeryId()
        });
    }

    private void attachListeners() {
        view.getAddButton().addActionListener(e -> addPatient());
        view.getEditButton().addActionListener(e -> editPatient());
        view.getDeleteButton().addActionListener(e -> deletePatient());
        view.getSearchButton().addActionListener(e -> searchPatients());
    }

    private void addPatient() {
        // Updated fields to include Phone Number and NHS Number
        String[] labels = {"First Name*", "Last Name*", "NHS Number*", "Phone Number*", "Date of Birth (DD/MM/YYYY)"};
        String[] names = {"firstName", "lastName", "nhsNumber", "phoneNumber", "dateOfBirth"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Add New Patient", null, labels, names, Patient.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("firstName") != null && !values.get("firstName").isEmpty()) {
            String newId = "P" + String.format("%03d", patients.size() + 1);

            // Get values or use defaults
            String firstName = values.get("firstName").trim();
            String lastName = values.get("lastName").trim();
            String nhsNumber = values.get("nhsNumber") != null && !values.get("nhsNumber").isEmpty()
                    ? values.get("nhsNumber").trim() : "NHS" + newId;
            String phoneNumber = values.get("phoneNumber") != null && !values.get("phoneNumber").isEmpty()
                    ? values.get("phoneNumber").trim() : "0712345678";
            String dateOfBirth = values.get("dateOfBirth") != null && !values.get("dateOfBirth").isEmpty()
                    ? values.get("dateOfBirth").trim() : "01/01/2000";

            Patient newPatient = new Patient(newId, firstName, lastName, dateOfBirth, nhsNumber,
                    "M", phoneNumber, "email@example.com", "123 Street", "SW1A 1AA",
                    "Emergency Contact", "0712345678", "21/12/2025", "GP001");

            patients.add(newPatient);
            addPatientToTable(newPatient);
            savePatients();
            JOptionPane.showMessageDialog(view, "Patient " + newId + " added successfully!");
        }
    }

    private void editPatient() {
        int selectedRow = view.getPatientTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a patient to edit",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Updated fields for editing - include Phone and NHS Number
        String[] labels = {"First Name*", "Last Name*", "NHS Number*", "Phone Number*", "Date of Birth"};
        String[] names = {"firstName", "lastName", "nhsNumber", "phoneNumber", "dateOfBirth"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Edit Patient",
                patients.get(selectedRow), labels, names, Patient.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("firstName") != null && !values.get("firstName").isEmpty()) {
            Patient patient = patients.get(selectedRow);

            // Update all editable fields
            patient.setFirstName(values.get("firstName").trim());
            patient.setLastName(values.get("lastName").trim());
            patient.setNhsNumber(values.get("nhsNumber").trim());
            patient.setPhoneNumber(values.get("phoneNumber").trim());

            if (values.get("dateOfBirth") != null && !values.get("dateOfBirth").isEmpty()) {
                patient.setDateOfBirth(values.get("dateOfBirth").trim());
            }

            refreshTable();
            savePatients();
            JOptionPane.showMessageDialog(view, "Patient updated successfully!");
        }
    }

    private void deletePatient() {
        int selectedRow = view.getPatientTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a patient to delete",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete this patient?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            view.getTableModel().removeRow(selectedRow);
            patients.remove(selectedRow);
            savePatients();
            JOptionPane.showMessageDialog(view, "Patient deleted successfully!");
        }
    }

    private void searchPatients() {
        String searchTerm = view.getSearchField().getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }

        view.getTableModel().setRowCount(0);
        for (Patient patient : patients) {
            if (patient.getFirstName().toLowerCase().contains(searchTerm) ||
                    patient.getLastName().toLowerCase().contains(searchTerm) ||
                    patient.getNhsNumber().toLowerCase().contains(searchTerm) ||
                    patient.getPhoneNumber().toLowerCase().contains(searchTerm) ||
                    patient.getPatientId().toLowerCase().contains(searchTerm)) {
                addPatientToTable(patient);
            }
        }
    }

    private void refreshTable() {
        view.getTableModel().setRowCount(0);
        for (Patient patient : patients) {
            addPatientToTable(patient);
        }
    }

    private void savePatients() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"patient_id","first_name","last_name","date_of_birth",
                "nhs_number","gender","phone_number","email","address","postcode",
                "emergency_contact_name","emergency_contact_phone","registration_date","gp_surgery_id"});

        for (Patient p : patients) {
            data.add(new String[]{
                    p.getPatientId(), p.getFirstName(), p.getLastName(), p.getDateOfBirth(),
                    p.getNhsNumber(), p.getGender(), p.getPhoneNumber(), p.getEmail(),
                    p.getAddress(), p.getPostcode(), p.getEmergencyContactName(),
                    p.getEmergencyContactPhone(), p.getRegistrationDate(), p.getGpSurgeryId()
            });
        }
        CSVWriter.writeCSV(csvFilePath, data);
    }
}
