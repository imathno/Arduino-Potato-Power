package com.imath.gui;

import javax.swing.*;
import java.awt.*;

public class VoltGui extends JFrame {

    private static final String GUI_TITLE = "Voltage Meter";

    private static VoltGui instance;

    private JFrame frame;
    private JTextArea textArea;

    private VoltGui() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame = new JFrame(GUI_TITLE);
        textArea = new JTextArea();

        frame.setLayout(new BorderLayout());
        frame.setSize(600, 600);

        frame.getContentPane().add(BorderLayout.CENTER, textArea);

        textArea.setEditable(false);
        textArea.setText("Configuring Text Box");

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
