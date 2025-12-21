package com.nhs.util;

import java.io.*;
import java.util.*;

public class CSVWriter {

    public static void writeCSV(String filePath, List<String[]> data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                String[] escapedRow = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    String value = row[i];
                    if (value == null || value.trim().isEmpty()) {
                        escapedRow[i] = "N/A";
                    } else if (value.contains(",")) {
                        escapedRow[i] = "\"" + value + "\"";
                    } else {
                        escapedRow[i] = value;
                    }
                }
                pw.println(String.join(",", escapedRow));
            }

            System.out.println("Successfully wrote " + (data.size() - 1) + " records to " + filePath);

        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + filePath);
            e.printStackTrace();
        }
    }

    public static void appendToCSV(String filePath, String[] row) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            String[] escapedRow = new String[row.length];
            for (int i = 0; i < row.length; i++) {
                String value = row[i];
                if (value == null || value.trim().isEmpty()) {
                    escapedRow[i] = "N/A";
                } else if (value.contains(",")) {
                    escapedRow[i] = "\"" + value + "\"";
                } else {
                    escapedRow[i] = value;
                }
            }
            pw.println(String.join(",", escapedRow));

            System.out.println("Appended 1 record to " + filePath);

        } catch (IOException e) {
            System.err.println("Error appending to CSV file: " + filePath);
            e.printStackTrace();
        }
    }
}
