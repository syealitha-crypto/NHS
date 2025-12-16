package com.nhs.singleton;

import com.nhs.model.Referral;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReferralManager {

    private static ReferralManager instance;
    private Queue<Referral> referralQueue;
    private List<String> auditLog;

    private ReferralManager() {
        referralQueue = new LinkedList<>();
        auditLog = new ArrayList<>();
        System.out.println("ReferralManager Singleton instance created");
    }

    public static synchronized ReferralManager getInstance() {
        if (instance == null) {
            instance = new ReferralManager();
        }
        return instance;
    }

    public void addReferral(Referral referral) {
        referralQueue.offer(referral);
        logAudit("Referral added: " + referral.getReferralId());
        processReferral(referral);
    }

    private void processReferral(Referral referral) {
        try {
            String emailContent = generateEmailContent(referral);
            saveReferralEmail(referral.getReferralId(), emailContent);
            updateEHR(referral);
            logAudit("Referral processed: " + referral.getReferralId());
        } catch (Exception e) {
            logAudit("Error: " + referral.getReferralId() + " - " + e.getMessage());
        }
    }

    private String generateEmailContent(Referral referral) {
        StringBuilder email = new StringBuilder();
        email.append("═══════════════════════════════════════\n");
        email.append("      NHS REFERRAL NOTIFICATION\n");
        email.append("═══════════════════════════════════════\n\n");
        email.append("Referral ID: ").append(referral.getReferralId()).append("\n");
        email.append("Date: ").append(referral.getReferralDate()).append("\n");
        email.append("Urgency: ").append(referral.getUrgencyLevel()).append("\n");
        email.append("Status: ").append(referral.getStatus()).append("\n\n");
        email.append("Patient ID: ").append(referral.getPatientId()).append("\n");
        email.append("Referring Clinician: ").append(referral.getReferringClinicianId()).append("\n");
        email.append("Referred To: ").append(referral.getReferredToClinicianId()).append("\n\n");
        email.append("Reason: ").append(referral.getReferralReason()).append("\n");
        email.append("Summary: ").append(referral.getClinicalSummary()).append("\n");
        email.append("Investigations: ").append(referral.getRequestedInvestigations()).append("\n\n");
        email.append("Generated: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n");
        email.append("═══════════════════════════════════════\n");
        return email.toString();
    }

    private void saveReferralEmail(String referralId, String content) {
        String fileName = "output/referral_emails/referral_" + referralId + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(content);
            System.out.println("Referral email saved: " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving referral: " + e.getMessage());
        }
    }

    private void updateEHR(Referral referral) {
        System.out.println("EHR updated for: " + referral.getReferralId());
        logAudit("EHR updated: " + referral.getReferralId());
    }

    private void logAudit(String message) {
        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        String logEntry = "[" + timestamp + "] " + message;
        auditLog.add(logEntry);
        System.out.println("AUDIT: " + logEntry);
    }

    public Queue<Referral> getReferralQueue() {
        return new LinkedList<>(referralQueue);
    }

    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    public int getQueueSize() {
        return referralQueue.size();
    }
}
