package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Popup extends JFrame{
    private JPanel MainPanel;
    private JTextArea text;
    private JButton closeButton;

    Popup(String type) {
        super(type);
        //Setting up popup
        setContentPane(MainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        //Setting text area
        text.setEditable(false);
        switch (type) {
            case "Help":
                text.setText("Commands:\n" +
                        "Print: Prints the grid\n" +
                        "Quit: Exits program\n" +
                        "Save: Saves grid to a specified text file\n" +
                        "Load: Loads grid from a specified text file\n");
                break;
            case "Open":
                text.setText("Welcome to VisiCalc!  Enter help for assistance with commands.");
                break;
            case "":

                break;

        }

        //Displaying Popup
        pack();
        setVisible(true);
        //Setting up listeners
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeMe();
            }
        });
    }

    private void disposeMe() {
        this.dispose();
    }
}
