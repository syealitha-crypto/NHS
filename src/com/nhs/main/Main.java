package com.nhs.main;

import com.nhs.view.MainFrame;
import com.nhs.singleton.ReferralManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            System.out.println("═══════════════════════════════════════════════");
            System.out.println("  NHS Referral Management System");
            System.out.println("  MVC Architecture with Singleton Pattern");
            System.out.println("═══════════════════════════════════════════════");

            // Initialize Singleton
            ReferralManager referralManager = ReferralManager.getInstance();
            System.out.println("ReferralManager initialized: " + referralManager);

            // Launch main frame
            new MainFrame();

            System.out.println("Application launched successfully!");
            System.out.println("═══════════════════════════════════════════════");
        });
    }
}
