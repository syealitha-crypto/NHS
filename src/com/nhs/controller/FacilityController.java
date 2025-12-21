package com.nhs.controller;

import com.nhs.model.Facility;
import com.nhs.view.FacilityPanel;
import com.nhs.view.GenericAddEditDialog;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class FacilityController {

    private FacilityPanel view;
    private List<Facility> facilities;
    private String csvFilePath = "data/facilities.csv";

    public FacilityController(FacilityPanel view) {
        this.view = view;
        this.facilities = new ArrayList<>();
        loadFacilities();
        attachListeners();
    }

    private void loadFacilities() {
        List<String[]> data = CSVReader.readCSV(csvFilePath);
        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                Facility facility = new Facility(
                        CSVReader.getValue(row, 0), CSVReader.getValue(row, 1),
                        CSVReader.getValue(row, 2), CSVReader.getValue(row, 3),
                        CSVReader.getValue(row, 4), CSVReader.getValue(row, 5),
                        CSVReader.getValue(row, 6), CSVReader.getValue(row, 7),
                        CSVReader.getValue(row, 8), CSVReader.getValue(row, 9),
                        CSVReader.getValue(row, 10)
                );
                facilities.add(facility);
                addFacilityToTable(facility);
            }
        }
    }

    private void addFacilityToTable(Facility facility) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                facility.getFacilityId(), facility.getFacilityName(),
                facility.getFacilityType(), facility.getAddress(),
                facility.getPostcode(), facility.getPhoneNumber(),
                facility.getManagerName()
        });
    }

    private void attachListeners() {
        view.getAddButton().addActionListener(e -> addFacility());
        view.getEditButton().addActionListener(e -> editFacility());
        view.getDeleteButton().addActionListener(e -> deleteFacility());
        view.getSearchButton().addActionListener(e -> searchFacilities());
    }

    private void addFacility() {
        String[] labels = {"Facility Name*", "Facility Type*", "Address"};
        String[] names = {"facilityName", "facilityType", "address"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Add New Facility", null, labels, names, Facility.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("facilityName") != null && !values.get("facilityName").isEmpty()) {
            String newId = "F" + String.format("%03d", facilities.size() + 1);
            Facility newFacility = new Facility(newId, values.get("facilityName"),
                    values.get("facilityType"), values.get("address"),
                    "SW1A 1AA", "02071234567", "admin@example.com",
                    "Mon-Fri 9-5", "John Manager", "100", "General");

            facilities.add(newFacility);
            addFacilityToTable(newFacility);
            saveFacilities();
            JOptionPane.showMessageDialog(view, "Facility " + newId + " added successfully!");
        }
    }

    private void editFacility() {
        int selectedRow = view.getFacilityTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a facility to edit");
            return;
        }

        String[] labels = {"Facility Name", "Facility Type", "Address"};
        String[] names = {"facilityName", "facilityType", "address"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Edit Facility", facilities.get(selectedRow), labels, names, Facility.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("facilityName") != null && !values.get("facilityName").isEmpty()) {
            Facility facility = facilities.get(selectedRow);
            facility.setFacilityName(values.get("facilityName"));
            facility.setFacilityType(values.get("facilityType"));
            facility.setAddress(values.get("address"));
            refreshTable();
            saveFacilities();
            JOptionPane.showMessageDialog(view, "Facility updated successfully!");
        }
    }

    private void deleteFacility() {
        int selectedRow = view.getFacilityTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a facility to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this facility?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.getTableModel().removeRow(selectedRow);
            facilities.remove(selectedRow);
            saveFacilities();
            JOptionPane.showMessageDialog(view, "Facility deleted successfully!");
        }
    }

    private void searchFacilities() {
        String searchTerm = view.getSearchField().getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }
        view.getTableModel().setRowCount(0);
        for (Facility facility : facilities) {
            if (facility.getFacilityName().toLowerCase().contains(searchTerm) ||
                    facility.getFacilityType().toLowerCase().contains(searchTerm) ||
                    facility.getFacilityId().toLowerCase().contains(searchTerm) ||
                    facility.getPostcode().toLowerCase().contains(searchTerm)) {
                addFacilityToTable(facility);
            }
        }
    }

    private void refreshTable() {
        view.getTableModel().setRowCount(0);
        for (Facility facility : facilities) {
            addFacilityToTable(facility);
        }
    }

    private void saveFacilities() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"facility_id","facility_name","facility_type","address","postcode","phone_number","email","opening_hours","manager_name","capacity","specialities_offered"});
        for (Facility f : facilities) {
            data.add(new String[]{f.getFacilityId(),f.getFacilityName(),f.getFacilityType(),f.getAddress(),f.getPostcode(),f.getPhoneNumber(),f.getEmail(),f.getOpeningHours(),f.getManagerName(),f.getCapacity(),f.getSpecialitiesOffered()});
        }
        CSVWriter.writeCSV(csvFilePath, data);
    }
}
