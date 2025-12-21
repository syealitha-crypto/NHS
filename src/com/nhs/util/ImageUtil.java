package com.nhs.util;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageUtil {

    private static final String IMAGE_PATH = "resources/images/";

    public static ImageIcon loadImage(String fileName) {
        try {
            String fullPath = IMAGE_PATH + fileName;
            File file = new File(fullPath);

            if (file.exists()) {
                return new ImageIcon(fullPath);
            } else {
                System.err.println("Image not found: " + fullPath + " - Using placeholder");
                return createPlaceholderIcon(32, 32);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + fileName);
            return createPlaceholderIcon(32, 32);
        }
    }

    public static ImageIcon loadScaledImage(String fileName, int width, int height) {
        ImageIcon icon = loadImage(fileName);
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private static ImageIcon createPlaceholderIcon(int width, int height) {
        java.awt.image.BufferedImage placeholder =
                new java.awt.image.BufferedImage(width, height,
                        java.awt.image.BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(0, 0, width - 1, height - 1);
        g2d.setColor(Color.RED);
        g2d.drawLine(0, 0, width, height);
        g2d.drawLine(0, height, width, 0);
        g2d.dispose();

        return new ImageIcon(placeholder);
    }
}
