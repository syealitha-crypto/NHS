package com.nhs.controller;

import com.nhs.model.Staff;
import com.nhs.view.StaffPanel;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.util.*;

public class StaffController {

    private StaffPanel view;
    private List<Staff> staffList;
    private String csvFilePath = "data/staff.csv";

    public StaffController(StaffPanel view) {
        this.view = view;
        this.staffList = new ArrayList<>();

        loadStaff();
        attachListeners();
    }

    private void loadStaff() {
        List<String[]> data = CSVReader.readCSV(csvFilePath);

        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);

                Staff staff = new Staff(
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
                        CSVReader.getValue(row, 11)
                );

                staffList.add(staff);
                addStaffToTable(staff);
            }
        }
    }

    private void addStaffToTable(Staff staff) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                staff.getStaffId(),
                staff.getFirstName(),
                staff.getLastName(),
                staff.getRole(),
                staff.getDepartment(),
                staff.getFacilityId(),
                staff.getAccessLevel()
        });
    }

    private void attachListeners() {
        view.getAddButton().addActionListener(e -> addStaff());
        view.getEditButton().addActionListener(e -> editStaff());
        view.getDeleteButton().addActionListener(e -> deleteStaff());
        view.getSearchButton().addActionListener(e -> searchStaff());
    }

    private void addStaff() {
        JOptionPane.showMessageDialog(view,
                "Add Staff functionality - To be implemented",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void editStaff() {
        int selectedRow = view.getStaffTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Please select a staff member to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(view,
                "Edit Staff functionality - To be implemented",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteStaff() {
        int selectedRow = view.getStaffTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Please select a staff member to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete this staff member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            view.getTableModel().removeRow(selectedRow);
            staffList.remove(selectedRow);
            saveStaff();
            JOptionPane.showMessageDialog(view, "Staff member deleted successfully!");
        }
    }

    private void searchStaff() {
        String searchTerm = view.getSearchField().getText().toLowerCase();

        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }

        view.getTableModel().setRowCount(0);

        for (Staff staff : staffList) {
            if (staff.getFirstName().toLowerCase().contains(searchTerm) ||
                    staff.getLastName().toLowerCase().contains(searchTerm) ||
                    staff.getStaffId().toLowerCase().contains(searchTerm) ||
                    staff.getRole().toLowerCase().contains(searchTerm) ||
                    staff.getDepartment().toLowerCase().contains(searchTerm)) {
                addStaffToTable(staff);
            }
        }
    }

    private void refreshTable() {
        view.getTableModel().setRowCount(0);
        for (Staff staff : staffList) {
            addStaffToTable(staff);
        }
    }

    private void saveStaff() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"staff_id", "first_name", "last_name", "role",
                "department", "facility_id", "phone_number", "email",
                "employment_status", "start_date", "line_manager", "access_level"});

        for (Staff s : staffList) {
            data.add(new String[]{
                    s.getStaffId(), s.getFirstName(), s.getLastName(), s.getRole(),
                    s.getDepartment(), s.getFacilityId(), s.getPhoneNumber(), s.getEmail(),
                    s.getEmploymentStatus(), s.getStartDate(), s.getLineManager(), s.getAccessLevel()
            });
        }

        CSVWriter.writeCSV(csvFilePath, data);
    }
}
