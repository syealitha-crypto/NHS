package com.nhs.controller;

import com.nhs.model.Prescription;
import com.nhs.view.PrescriptionPanel;
import com.nhs.view.GenericAddEditDialog;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class PrescriptionController {

    private PrescriptionPanel view;
    private List<Prescription> prescriptions;
    private String csvFilePath = "data/prescriptions.csv";

    public PrescriptionController(PrescriptionPanel view) {
        this.view = view;
        this.prescriptions = new ArrayList<>();
        loadPrescriptions();
        attachListeners();
    }

    private void loadPrescriptions() {
        List<String[]> data = CSVReader.readCSV(csvFilePath);
        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                Prescription prescription = new Prescription(
                        CSVReader.getValue(row, 0), CSVReader.getValue(row, 1),
                        CSVReader.getValue(row, 2), CSVReader.getValue(row, 3),
                        CSVReader.getValue(row, 4), CSVReader.getValue(row, 5),
                        CSVReader.getValue(row, 6), CSVReader.getValue(row, 7),
                        CSVReader.getValue(row, 8), CSVReader.getValue(row, 9),
                        CSVReader.getValue(row, 10), CSVReader.getValue(row, 11),
                        CSVReader.getValue(row, 12), CSVReader.getValue(row, 13),
                        CSVReader.getValue(row, 14)
                );
                prescriptions.add(prescription);
                addPrescriptionToTable(prescription);
            }
        }
    }

    private void addPrescriptionToTable(Prescription prescription) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                prescription.getPrescriptionId(), prescription.getPatientId(),
                prescription.getMedicationName(), prescription.getDosage(),
                prescription.getFrequency(), prescription.getDurationDays() + " days",
                prescription.getStatus(), prescription.getPharmacyName()
        });
    }

    private void attachListeners() {
        view.getAddButton().addActionListener(e -> addPrescription());
        view.getEditButton().addActionListener(e -> editPrescription());
        view.getDeleteButton().addActionListener(e -> deletePrescription());
        view.getSearchButton().addActionListener(e -> searchPrescriptions());
    }

    private void addPrescription() {
        String[] labels = {"Patient ID*", "Medication Name*", "Dosage*", "Frequency*"};
        String[] names = {"patientId", "medication", "dosage", "frequency"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Add New Prescription", null, labels, names, Prescription.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("patientId") != null && !values.get("patientId").isEmpty()) {
            String newId = "RX" + String.format("%03d", prescriptions.size() + 1);
            Prescription newPrescription = new Prescription(newId, values.get("patientId"),
                    "C001", "A001", "21/12/2025", values.get("medication"), values.get("dosage"),
                    values.get("frequency"), "30", "30 tablets", "Take with food",
                    "Boots Pharmacy", "Pending", "N/A", "N/A");

            prescriptions.add(newPrescription);
            addPrescriptionToTable(newPrescription);
            savePrescriptions();
            JOptionPane.showMessageDialog(view, "Prescription " + newId + " added successfully!");
        }
    }

    private void editPrescription() {
        int selectedRow = view.getPrescriptionTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a prescription to edit");
            return;
        }

        String[] labels = {"Medication Name", "Dosage", "Frequency"};
        String[] names = {"medication", "dosage", "frequency"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Edit Prescription", prescriptions.get(selectedRow), labels, names, Prescription.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("medication") != null && !values.get("medication").isEmpty()) {
            Prescription prescription = prescriptions.get(selectedRow);
            prescription.setMedicationName(values.get("medication"));
            prescription.setDosage(values.get("dosage"));
            prescription.setFrequency(values.get("frequency"));
            refreshTable();
            savePrescriptions();
            JOptionPane.showMessageDialog(view, "Prescription updated successfully!");
        }
    }

    private void deletePrescription() {
        int selectedRow = view.getPrescriptionTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a prescription to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this prescription?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.getTableModel().removeRow(selectedRow);
            prescriptions.remove(selectedRow);
            savePrescriptions();
            JOptionPane.showMessageDialog(view, "Prescription deleted successfully!");
        }
    }

    private void searchPrescriptions() {
        String searchTerm = view.getSearchField().getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }
        view.getTableModel().setRowCount(0);
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionId().toLowerCase().contains(searchTerm) ||
                    prescription.getPatientId().toLowerCase().contains(searchTerm) ||
                    prescription.getMedicationName().toLowerCase().contains(searchTerm) ||
                    prescription.getStatus().toLowerCase().contains(searchTerm)) {
                addPrescriptionToTable(prescription);
            }
        }
    }

    private void refreshTable() {
        view.getTableModel().setRowCount(0);
        for (Prescription prescription : prescriptions) {
            addPrescriptionToTable(prescription);
        }
    }

    private void savePrescriptions() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"prescription_id","patient_id","clinician_id","appointment_id","prescription_date","medication_name","dosage","frequency","duration_days","quantity","instructions","pharmacy_name","status","issue_date","collection_date"});
        for (Prescription p : prescriptions) {
            data.add(new String[]{p.getPrescriptionId(),p.getPatientId(),p.getClinicianId(),p.getAppointmentId(),p.getPrescriptionDate(),p.getMedicationName(),p.getDosage(),p.getFrequency(),p.getDurationDays(),p.getQuantity(),p.getInstructions(),p.getPharmacyName(),p.getStatus(),p.getIssueDate(),p.getCollectionDate()});
        }
        CSVWriter.writeCSV(csvFilePath, data);
    }
}
