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

public class Display extends JFrame{
    public JTable GridTable;
    private JPanel MainPanel;
    private JTextField input;
    private JScrollPane Scroll;
    private Grid grid;
    private String userInput = "";
    private String[] header1;
    private DefaultTableModel model;

    Display(Grid grid) {
        super("Display");
        //variables
        this.grid = grid;
        //createUIComponents();
        //Setting up GUI
        setContentPane(MainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        //Creating the table
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        model = tableModel;
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
                    try {
                        Main.inputLoop(userInput, grid);
                    } catch (Exception q) {
                        System.out.println("Error");
                    }
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
        DefaultTableModel temp = model;
        temp.setDataVector(grid.spreadSheet, header1);
        GridTable.setModel(temp);
    }

    String getInput() {
        return userInput;
    }
}
