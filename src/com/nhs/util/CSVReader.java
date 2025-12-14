package com.nhs.util;

import java.io.*;
import java.util.*;

public class CSVReader {

    public static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                    if (values[i].startsWith("\"") && values[i].endsWith("\"")) {
                        values[i] = values[i].substring(1, values[i].length() - 1);
                    }
                    if (values[i].equals("N/A")) {
                        values[i] = "";
                    }
                }

                data.add(values);
            }

            System.out.println("Successfully loaded " + (data.size() - 1) + " records from " + filePath);

        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + filePath);
            e.printStackTrace();
        }

        return data;
    }

    public static String getValue(String[] row, int index) {
        if (index >= 0 && index < row.length) {
            return row[index];
        }
        return "";
    }
}
