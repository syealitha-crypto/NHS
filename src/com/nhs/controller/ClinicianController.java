package com.nhs.controller;

import com.nhs.model.Clinician;
import com.nhs.view.ClinicianPanel;
import com.nhs.view.GenericAddEditDialog;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class ClinicianController {

    private ClinicianPanel view;
    private List<Clinician> clinicians;
    private String csvFilePath = "data/clinicians.csv";

    public ClinicianController(ClinicianPanel view) {
        this.view = view;
        this.clinicians = new ArrayList<>();
        loadClinicians();
        attachListeners();
    }

    private void loadClinicians() {
        List<String[]> data = CSVReader.readCSV(csvFilePath);
        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                Clinician clinician = new Clinician(
                        CSVReader.getValue(row, 0), CSVReader.getValue(row, 1),
                        CSVReader.getValue(row, 2), CSVReader.getValue(row, 3),
                        CSVReader.getValue(row, 4), CSVReader.getValue(row, 5),
                        CSVReader.getValue(row, 6), CSVReader.getValue(row, 7),
                        CSVReader.getValue(row, 8), CSVReader.getValue(row, 9),
                        CSVReader.getValue(row, 10), CSVReader.getValue(row, 11)
                );
                clinicians.add(clinician);
                addClinicianToTable(clinician);
            }
        }
    }

    private void addClinicianToTable(Clinician clinician) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                clinician.getClinicianId(), clinician.getTitle(),
                clinician.getFirstName(), clinician.getLastName(),
                clinician.getSpeciality(), clinician.getGmcNumber(),
                clinician.getPhoneNumber(), clinician.getWorkplaceId()
        });
    }

    private void attachListeners() {
        view.getAddButton().addActionListener(e -> addClinician());
        view.getEditButton().addActionListener(e -> editClinician());
        view.getDeleteButton().addActionListener(e -> deleteClinician());
        view.getSearchButton().addActionListener(e -> searchClinicians());
    }

    private void addClinician() {
        String[] labels = {"First Name*", "Last Name*", "Speciality*"};
        String[] names = {"firstName", "lastName", "speciality"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Add New Clinician", null, labels, names, Clinician.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("firstName") != null && !values.get("firstName").isEmpty()) {
            String newId = "C" + String.format("%03d", clinicians.size() + 1);
            Clinician newClinician = new Clinician(newId, values.get("firstName"),
                    values.get("lastName"), "Dr", values.get("speciality"),
                    "GMC" + newId, "0712345678", "doctor@example.com",
                    "FAC001", "Hospital", "Full-time", "21/12/2025");

            clinicians.add(newClinician);
            addClinicianToTable(newClinician);
            saveClinicians();
            JOptionPane.showMessageDialog(view, "Clinician " + newId + " added successfully!");
        }
    }

    private void editClinician() {
        int selectedRow = view.getClinicianTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a clinician to edit");
            return;
        }

        String[] labels = {"First Name", "Last Name", "Speciality"};
        String[] names = {"firstName", "lastName", "speciality"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Edit Clinician", clinicians.get(selectedRow), labels, names, Clinician.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("firstName") != null && !values.get("firstName").isEmpty()) {
            Clinician clinician = clinicians.get(selectedRow);
            clinician.setFirstName(values.get("firstName"));
            clinician.setLastName(values.get("lastName"));
            clinician.setSpeciality(values.get("speciality"));
            refreshTable();
            saveClinicians();
            JOptionPane.showMessageDialog(view, "Clinician updated successfully!");
        }
    }

    private void deleteClinician() {
        int selectedRow = view.getClinicianTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a clinician to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this clinician?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.getTableModel().removeRow(selectedRow);
            clinicians.remove(selectedRow);
            saveClinicians();
            JOptionPane.showMessageDialog(view, "Clinician deleted successfully!");
        }
    }

    private void searchClinicians() {
        String searchTerm = view.getSearchField().getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }
        view.getTableModel().setRowCount(0);
        for (Clinician clinician : clinicians) {
            if (clinician.getFirstName().toLowerCase().contains(searchTerm) ||
                    clinician.getLastName().toLowerCase().contains(searchTerm) ||
                    clinician.getSpeciality().toLowerCase().contains(searchTerm) ||
                    clinician.getClinicianId().toLowerCase().contains(searchTerm) ||
                    clinician.getGmcNumber().toLowerCase().contains(searchTerm)) {
                addClinicianToTable(clinician);
            }
        }
    }

    private void refreshTable() {
        view.getTableModel().setRowCount(0);
        for (Clinician clinician : clinicians) {
            addClinicianToTable(clinician);
        }
    }

    private void saveClinicians() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"clinician_id","first_name","last_name","title","speciality","gmc_number","phone_number","email","workplace_id","workplace_type","employment_status","start_date"});
        for (Clinician c : clinicians) {
            data.add(new String[]{c.getClinicianId(),c.getFirstName(),c.getLastName(),c.getTitle(),c.getSpeciality(),c.getGmcNumber(),c.getPhoneNumber(),c.getEmail(),c.getWorkplaceId(),c.getWorkplaceType(),c.getEmploymentStatus(),c.getStartDate()});
        }
        CSVWriter.writeCSV(csvFilePath, data);
    }
}
