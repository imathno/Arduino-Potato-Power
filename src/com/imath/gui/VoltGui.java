package com.imath.gui;

import javax.swing.*;
import java.awt.*;

public class VoltGui extends JFrame {

    private static final String GUI_TITLE = "Voltage Meter";

    private static VoltGui instance;

    private JFrame frame;
    private JTextArea textArea;

    private VoltGui() {
        frame = new JFrame(GUI_TITLE);
        textArea = new JTextArea();

        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 600);

        frame.getContentPane().add(BorderLayout.CENTER, textArea);

        textArea.setEditable(false);
        Font font = new Font("Courier New", Font.BOLD, 24);
        textArea.setFont(font);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

    public void updateTextBox(String str) {
        textArea.setText(str);
    }

    public static synchronized VoltGui getInstance() {
        if (instance == null) {
            instance = new VoltGui();
        }
        return instance;
    }
}
