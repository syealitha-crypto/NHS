package com.nhs.view;

import com.nhs.util.ImageUtil;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("NHS Referral Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setIconImage(ImageUtil.loadImage("logo.png").getImage());

        createHeaderPanel();
        createMenuBar();
        createTabbedPane();
        createFooterPanel();

        setVisible(true);
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 94, 184));
        headerPanel.setPreferredSize(new Dimension(1200, 100));

        JLabel logoLabel = new JLabel(ImageUtil.loadScaledImage("logo.png", 150, 60));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("NHS Referral Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenuItem refreshItem = new JMenuItem("Refresh Data");
        refreshItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Data refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        tabbedPane.addTab("Patients",
                ImageUtil.loadScaledImage("patient_icon.png", 24, 24),
                new PatientPanel(), "Manage patients");

        tabbedPane.addTab("Clinicians",
                ImageUtil.loadScaledImage("doctor_icon.png", 24, 24),
                new ClinicianPanel(), "Manage clinicians");

        tabbedPane.addTab("Facilities",
                ImageUtil.loadScaledImage("facility_icon.png", 24, 24),
                new JPanel(), "Manage facilities");

        tabbedPane.addTab("Appointments",
                ImageUtil.loadScaledImage("appointment_icon.png", 24, 24),
                new JPanel(), "Manage appointments");

        tabbedPane.addTab("Prescriptions",
                ImageUtil.loadScaledImage("prescription_icon.png", 24, 24),
                new JPanel(), "Manage prescriptions");

        tabbedPane.addTab("Referrals",
                ImageUtil.loadScaledImage("referral_icon.png", 24, 24),
                new JPanel(), "Manage referrals");

        tabbedPane.addTab("Staff",
                ImageUtil.loadScaledImage("staff_icon.png", 24, 24),
                new JPanel(), "Manage staff");

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setPreferredSize(new Dimension(1200, 30));

        JLabel footerLabel = new JLabel("© 2025 NHS Referral Management System | MVC Architecture");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.DARK_GRAY);

        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void showAboutDialog() {
        String message = "NHS Referral Management System\n\n" +
                "Version: 1.0\n" +
                "Architecture: MVC Pattern\n" +
                "Design Pattern: Singleton (ReferralManager)\n\n" +
                "Software Architecture Assignment 2025";

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
