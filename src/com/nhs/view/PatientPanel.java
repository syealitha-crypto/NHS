package com.nhs.view;

import com.nhs.util.ImageUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientPanel extends JPanel {

    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, searchButton;
    private JTextField searchField;

    public PatientPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createSearchPanel();
        createTablePanel();
        createButtonPanel();
        // Initialize controller - connects MVC
        new PatientController(this);
    }

    private void createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(new Color(240, 240, 240));

        JLabel searchIcon = new JLabel(ImageUtil.loadScaledImage("search_icon.png", 24, 24));
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));

        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));

        searchButton = new JButton("Search", ImageUtil.loadScaledImage("search_icon.png", 20, 20));

        searchPanel.add(searchIcon);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);
    }

    private void createTablePanel() {
        String[] columns = {
                "Patient ID", "First Name", "Last Name",
                "Date of Birth", "NHS Number", "Phone", "GP Surgery ID"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setFont(new Font("Arial", Font.PLAIN, 12));
        patientTable.setRowHeight(25);
        patientTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        patientTable.getTableHeader().setBackground(new Color(0, 94, 184));
        patientTable.getTableHeader().setForeground(Color.WHITE);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);

        addButton = createButton("Add Patient", "add_icon.png", new Color(76, 175, 80));
        editButton = createButton("Edit Patient", "edit_icon.png", new Color(33, 150, 243));
        deleteButton = createButton("Delete Patient", "delete_icon.png", new Color(244, 67, 54));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, String icon, Color bgColor) {
        JButton button = new JButton(text, ImageUtil.loadScaledImage(icon, 20, 20));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 40));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getSearchButton() { return searchButton; }
    public JTextField getSearchField() { return searchField; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getPatientTable() { return patientTable; }
}
