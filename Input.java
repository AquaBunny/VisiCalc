package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends JFrame{
    private JTextField input;
    private JLabel Label;
    private JPanel Panel1;
    private String userInput;

    public Input() {
        super("Input");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(Panel1);
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)screensize.getWidth()/2, (int)screensize.getHeight()/2);
        userInput = "";
        pack();
        setVisible(true);
        input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInput = input.getText();
                input.setText("");
            }
        });
        input.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                input.setText("");
            }
        });
    }

    String returnInput() {
        return this.userInput;
    }
}
