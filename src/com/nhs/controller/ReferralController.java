package com.nhs.controller;

import com.nhs.model.Referral;
import com.nhs.view.ReferralPanel;
import com.nhs.view.GenericAddEditDialog;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import com.nhs.singleton.ReferralManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class ReferralController {

    private ReferralPanel view;
    private List<Referral> referrals;
    private String csvFilePath = "data/referrals.csv";
    private ReferralManager referralManager;

    public ReferralController(ReferralPanel view) {
        this.view = view;
        this.referrals = new ArrayList<>();
        this.referralManager = ReferralManager.getInstance();
        loadReferrals();
        attachListeners();
    }

    private void loadReferrals() {
        List<String[]> data = CSVReader.readCSV(csvFilePath);
        if (data.size() > 0) {
            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                Referral referral = new Referral(
                        CSVReader.getValue(row, 0), CSVReader.getValue(row, 1),
                        CSVReader.getValue(row, 2), CSVReader.getValue(row, 3),
                        CSVReader.getValue(row, 4), CSVReader.getValue(row, 5),
                        CSVReader.getValue(row, 6), CSVReader.getValue(row, 7),
                        CSVReader.getValue(row, 8), CSVReader.getValue(row, 9),
                        CSVReader.getValue(row, 10), CSVReader.getValue(row, 11),
                        CSVReader.getValue(row, 12), CSVReader.getValue(row, 13),
                        CSVReader.getValue(row, 14), CSVReader.getValue(row, 15)
                );
                referrals.add(referral);
                addReferralToTable(referral);
            }
        }
    }

    private void addReferralToTable(Referral referral) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                referral.getReferralId(), referral.getPatientId(),
                referral.getReferringClinicianId(), referral.getReferredToClinicianId(),
                referral.getReferralDate(), referral.getUrgencyLevel(),
                referral.getStatus(), referral.getReferralReason()
        });
    }

    private void attachListeners() {
        view.getAddButton().addActionListener(e -> addReferral());
        view.getEditButton().addActionListener(e -> editReferral());
        view.getDeleteButton().addActionListener(e -> deleteReferral());
        view.getSearchButton().addActionListener(e -> searchReferrals());
        view.getProcessButton().addActionListener(e -> processReferral());
    }

    private void addReferral() {
        String[] labels = {"Patient ID*", "Referral Reason*", "Urgency (Routine/Urgent)*"};
        String[] names = {"patientId", "reason", "urgency"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Add New Referral", null, labels, names, Referral.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("patientId") != null && !values.get("patientId").isEmpty()) {
            String newId = "R" + String.format("%03d", referrals.size() + 1);
            Referral newReferral = new Referral(newId, values.get("patientId"),
                    "C001", "C002", "F001", "F002", "21/12/2025", values.get("urgency"),
                    values.get("reason"), "Clinical summary", "Blood tests", "Pending",
                    "N/A", "Referral notes", "21/12/2025", "21/12/2025");

            referrals.add(newReferral);
            addReferralToTable(newReferral);
            saveReferrals();
            JOptionPane.showMessageDialog(view, "Referral " + newId + " added successfully!");
        }
    }

    private void editReferral() {
        int selectedRow = view.getReferralTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a referral to edit");
            return;
        }

        String[] labels = {"Reason", "Urgency"};
        String[] names = {"reason", "urgency"};

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);
        GenericAddEditDialog dialog = new GenericAddEditDialog(parent, "Edit Referral", referrals.get(selectedRow), labels, names, Referral.class);
        dialog.setVisible(true);

        Map<String, String> values = dialog.getFieldValues();
        if (!values.isEmpty() && values.get("reason") != null && !values.get("reason").isEmpty()) {
            Referral referral = referrals.get(selectedRow);
            referral.setReferralReason(values.get("reason"));
            referral.setUrgencyLevel(values.get("urgency"));
            refreshTable();
            saveReferrals();
            JOptionPane.showMessageDialog(view, "Referral updated successfully!");
        }
    }

    private void deleteReferral() {
        int selectedRow = view.getReferralTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a referral to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this referral?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            view.getTableModel().removeRow(selectedRow);
            referrals.remove(selectedRow);
            saveReferrals();
            JOptionPane.showMessageDialog(view, "Referral deleted successfully!");
        }
    }

    private void processReferral() {
        int selectedRow = view.getReferralTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a referral to process");
            return;
        }
        Referral referral = referrals.get(selectedRow);
        referralManager.addReferral(referral);
        JOptionPane.showMessageDialog(view, "Referral processed successfully!\nEmail saved to: output/referral_emails/\nCheck console for audit log.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchReferrals() {
        String searchTerm = view.getSearchField().getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            refreshTable();
            return;
        }
        view.getTableModel().setRowCount(0);
        for (Referral referral : referrals) {
            if (referral.getReferralId().toLowerCase().contains(searchTerm) ||
                    referral.getPatientId().toLowerCase().contains(searchTerm) ||
                    referral.getStatus().toLowerCase().contains(searchTerm) ||
                    referral.getUrgencyLevel().toLowerCase().contains(searchTerm)) {
                addReferralToTable(referral);
            }
        }
    }

    private void refreshTable() {
        view.getTableModel().setRowCount(0);
        for (Referral referral : referrals) {
            addReferralToTable(referral);
        }
    }

    private void saveReferrals() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"referral_id","patient_id","referring_clinician_id","referred_to_clinician_id","referring_facility_id","referred_to_facility_id","referral_date","urgency_level","referral_reason","clinical_summary","requested_investigations","status","appointment_id","notes","created_date","last_updated"});
        for (Referral r : referrals) {
            data.add(new String[]{r.getReferralId(),r.getPatientId(),r.getReferringClinicianId(),r.getReferredToClinicianId(),r.getReferringFacilityId(),r.getReferredToFacilityId(),r.getReferralDate(),r.getUrgencyLevel(),r.getReferralReason(),r.getClinicalSummary(),r.getRequestedInvestigations(),r.getStatus(),r.getAppointmentId(),r.getNotes(),r.getCreatedDate(),r.getLastUpdated()});
        }
        CSVWriter.writeCSV(csvFilePath, data);
    }
}
