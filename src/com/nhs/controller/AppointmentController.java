package com.nhs.controller;

import com.nhs.model.Appointment;
import com.nhs.view.AppointmentPanel;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.util.*;

public class AppointmentController {

    private AppointmentPanel view;
    private List<Appointment> appointments;
    private String csvFilePath = "data/appointments.csv";

    public AppointmentController(AppointmentPanel view) {
        this.view = view;
        this.appointments = new ArrayList<>();

        loadAppointments();
        attachListeners();
    }

    private void loadAppointments() {
        List<String[]> data = CSVReader.readCSV(csvFilePath);

        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);

                Appointment appointment = new Appointment(
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
                        CSVReader.getValue(row, 12)
                );

                appointments.add(appointment);
                addAppointmentToTable(appointment);
            }
        }
    }

    private void addAppointmentToTable(Appointment appointment) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                appointment.getAppointmentId(),
                appointment.getPatientId(),
                appointment.getClinicianId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getAppointmentType(),
                appointment.getStatus(),
                appointment.getReasonForVisit()
        });
    }

    private void attachListeners() {
        view.getAddButton().addActionListener(e -> addAppointment());
        view.getEditButton().addActionListener(e -> editAppointment());
        view.getDeleteButton().addActionListener(e -> deleteAppointment());
        view.getSearchButton().addActionListener(e -> searchAppointments());
    }

    private void addAppointment() {
        JOptionPane.showMessageDialog(view,
                "Add Appointment functionality - To be implemented",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void editAppointment() {
        int selectedRow = view.getAppointmentTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Please select an appointment to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(view,
                "Edit Appointment functionality - To be implemented",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteAppointment() {
        int selectedRow = view.getAppointmentTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Please select an appointment to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete this appointment?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            view.getTableModel().removeRow(selectedRow);
            appointments.remove(selectedRow);
            saveAppointments();
            JOptionPane.showMessageDialog(view, "Appointment deleted successfully!");
        }
    }

    private void searchAppointments() {
        String searchTerm = view.getSearchField().getText().toLowerCase();

        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }

        view.getTableModel().setRowCount(0);

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().toLowerCase().contains(searchTerm) ||
                    appointment.getPatientId().toLowerCase().contains(searchTerm) ||
                    appointment.getClinicianId().toLowerCase().contains(searchTerm) ||
                    appointment.getStatus().toLowerCase().contains(searchTerm) ||
                    appointment.getAppointmentDate().contains(searchTerm)) {
                addAppointmentToTable(appointment);
            }
        }
    }

    private void refreshTable() {
        view.getTableModel().setRowCount(0);
        for (Appointment appointment : appointments) {
            addAppointmentToTable(appointment);
        }
    }

    private void saveAppointments() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"appointment_id", "patient_id", "clinician_id", "facility_id",
                "appointment_date", "appointment_time", "duration_minutes",
                "appointment_type", "status", "reason_for_visit", "notes",
                "created_date", "last_modified"});

        for (Appointment a : appointments) {
            data.add(new String[]{
                    a.getAppointmentId(), a.getPatientId(), a.getClinicianId(), a.getFacilityId(),
                    a.getAppointmentDate(), a.getAppointmentTime(), a.getDurationMinutes(),
                    a.getAppointmentType(), a.getStatus(), a.getReasonForVisit(), a.getNotes(),
                    a.getCreatedDate(), a.getLastModified()
            });
        }

        CSVWriter.writeCSV(csvFilePath, data);
    }
}
