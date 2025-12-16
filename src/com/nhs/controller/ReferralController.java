package com.nhs.controller;

import com.nhs.model.Referral;
import com.nhs.view.ReferralPanel;
import com.nhs.util.CSVReader;
import com.nhs.util.CSVWriter;
import com.nhs.singleton.ReferralManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
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
                        CSVReader.getValue(row, 13),
                        CSVReader.getValue(row, 14),
                        CSVReader.getValue(row, 15)
                );

                referrals.add(referral);
                addReferralToTable(referral);
            }
        }
    }

    private void addReferralToTable(Referral referral) {
        DefaultTableModel model = view.getTableModel();
        model.addRow(new Object[]{
                referral.getReferralId(),
                referral.getPatientId(),
                referral.getReferringClinicianId(),
                referral.getReferredToClinicianId(),
                referral.getReferralDate(),
                referral.getUrgencyLevel(),
                referral.getStatus(),
                referral.getReferralReason()
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
        JOptionPane.showMessageDialog(view,
                "Add Referral functionality - To be implemented",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void editReferral() {
        int selectedRow = view.getReferralTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Please select a referral to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(view,
                "Edit Referral functionality - To be implemented",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteReferral() {
        int selectedRow = view.getReferralTable().getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Please select a referral to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete this referral?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

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
            JOptionPane.showMessageDialog(view,
                    "Please select a referral to process",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Referral referral = referrals.get(selectedRow);

        // Use Singleton to process referral
        referralManager.addReferral(referral);

        JOptionPane.showMessageDialog(view,
                "Referral processed successfully!\n" +
                        "Email generated and saved to: output/referral_emails/\n" +
                        "Check console for audit log.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
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
        data.add(new String[]{"referral_id", "patient_id", "referring_clinician_id",
                "referred_to_clinician_id", "referring_facility_id",
                "referred_to_facility_id", "referral_date", "urgency_level",
                "referral_reason", "clinical_summary", "requested_investigations",
                "status", "appointment_id", "notes", "created_date", "last_updated"});

        for (Referral r : referrals) {
            data.add(new String[]{
                    r.getReferralId(), r.getPatientId(), r.getReferringClinicianId(),
                    r.getReferredToClinicianId(), r.getReferringFacilityId(), r.getReferredToFacilityId(),
                    r.getReferralDate(), r.getUrgencyLevel(), r.getReferralReason(), r.getClinicalSummary(),
                    r.getRequestedInvestigations(), r.getStatus(), r.getAppointmentId(), r.getNotes(),
                    r.getCreatedDate(), r.getLastUpdated()
            });
        }

        CSVWriter.writeCSV(csvFilePath, data);
    }
}
