/**
* to get input use seperate button that retrives input, fix later
* */
package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class Display extends JFrame{
    private JTable GridTable;
    private JPanel MainPanel;
    private JTextField input;
    private JScrollPane Scroll;
    private Grid grid;
    private String userInput = "";
    private String[] header1;

    Display(Grid grid) {
        super("Display");
        //variables
        this.grid = grid;
        //createUIComponents();
        //Setting up GUI
        setContentPane(MainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)screensize.getWidth()/2, (int)screensize.getHeight()/2);

        //Creating the table
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        //tableModel.setColumnIdentifiers(header1);
        tableModel.setDataVector(grid.spreadSheet, header1);
        GridTable.setModel(tableModel);
        //Displaying GUI
        pack();
        setVisible(true);
        GridTable.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        });
        input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        input.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                input.setText("");
            }
        });

        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 10) {
                    userInput = input.getText();
                    input.setText("");
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //Vars
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //grid = new Grid(10,7);
        String[] header1 = new String[grid.spreadSheet.length];

        //Code for creating header
        if(this.grid.spreadSheet.length > 26) {
            for(int j = 0; j < this.grid.spreadSheet.length; j++) {
                int width = j;
                String header = "";
                if(width < 26) {
                    header = "" + letters.charAt(width);
                } else {
                    while(width > 25) {
                        int k = 0;
                        while(Math.pow(26, k) <= width) {
                            k++;
                        }
                        k--;
                        header += "" + letters.charAt((width / ((int) Math.pow(26, k)))-1);
                        width %= (int) Math.pow(26, k);
                    }
                    header += letters.charAt(width);
                }
                header1[j] = header;
            }
        } else {
            for (int j = 0; j < this.grid.spreadSheet.length; j++) {
                header1[j] = ""+letters.charAt(j);
            }
        }
        this.header1 = header1;
        GridTable = new JTable(grid.spreadSheet, header1);
    }

    public void update() {
        pack();
        setVisible(true);
    }

    String getInput() {
        return userInput;
    }
}
