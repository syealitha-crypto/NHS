package com.nhs.controller;

import com.nhs.model.Patient;
import com.nhs.view.PatientPanel;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
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
                        CSVReader.getValue(row, 0),
                        CSVReader.getValue(row, 1),
                        CSVReader.getValue(row, 2),
                        CSVReader.getValue(row, 3),
                        CSVReader.getValue(row, 4),
                        CSVReader.getValue(row, 5),
                        CSVReader.getValue(row, 6),
                        CSVReader.getValue(row, 7),
                        CSVReader.getValue(row, 8),
                        CSVReader.getValue(row, 9),
                        CSVReader.getValue(row, 10),
                        CSVReader.getValue(row, 11),
                        CSVReader.getValue(row, 12),
                        CSVReader.getValue(row, 13)
                );

                patients.add(patient);
                addPatientToTable(patient);
            }
        }
    }

    private void addPatientToTable(Patient patient) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getNhsNumber(),
                patient.getPhoneNumber(),
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
        JOptionPane.showMessageDialog(view,
                "Add Patient functionality - To be implemented",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void editPatient() {
        int selectedRow = view.getPatientTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Please select a patient to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(view,
                "Edit Patient functionality - To be implemented",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deletePatient() {
        int selectedRow = view.getPatientTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Please select a patient to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete this patient?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

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
                    patient.getNhsNumber().contains(searchTerm) ||
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
        data.add(new String[]{"patient_id", "first_name", "last_name", "date_of_birth",
                "nhs_number", "gender", "phone_number", "email", "address",
                "postcode", "emergency_contact_name", "emergency_contact_phone",
                "registration_date", "gp_surgery_id"});

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
