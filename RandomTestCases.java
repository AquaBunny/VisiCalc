package com.company;

import java.io.*;

public class RandomTestCases {
    public static void main(String args[]) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File("random.txt"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(stream);
        String[] whyMe = {};
        try {
            Main.main(whyMe);
        } catch (UnsupportedEncodingException badThing) {
            System.setOut(old);
            System.out.print("It broke baus");
            System.exit(2);
        }
    }
}
