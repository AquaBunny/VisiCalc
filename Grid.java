package com.company;

import java.io.PrintWriter;

public class Grid {
    private static String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Cell[][] spreadSheet;
    //When a grid is initialized all components are set to blank cells
    public Grid(int width, int length) {
        this.spreadSheet = new Cell[width][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                spreadSheet[j][i] = new Cell();
            }
        }
    }

    /**
     * To be able to handle infinite amount of letter spaces, make method that uses alphabet as base counting system
     * So AA = 1 * 26 + 1 * 1 or spot 27 so subtract 1 to get array space
     * Method pulls one char at a time from cell name, checks if its a letter and if so what its value is against string of
     * full alphabet, starts at high end and multiplies by increasing powers of 26
     * */
    static String getSpace(String value){
        String tempFirstVal = "";
        int i = 0, firstValue = 0;
        while(letters.contains(value.substring(i,i+1))) {
            tempFirstVal += value.substring(i, i+1);
            value = value.substring(i+1);
        }
        for(int j = tempFirstVal.length() - 1, k = 0; j > -1; j--, k++) {
            int tempVal;
            if(j == tempFirstVal.length() - 1) {
                tempVal = letters.indexOf(tempFirstVal.substring(j)) + 1;
            } else {
                tempVal = letters.indexOf(tempFirstVal.substring(j, j+1)) + 1;
            }
            firstValue += Math.pow(26, k) * tempVal;
        }
        return (Integer.parseInt(value) - 1) + ", " + (firstValue - 1);
    }

    /**
     * Does the opposite of getSpace, turns a coordinate pair into the cell value
     **/
    static String getCell(int x, int y) {
        String answer = "";
        ++y;
        do {
            if(x / 26 >= 1) {
                answer += letters.substring(x%26,x%26+1);
                x /= 26;
            } else {
                answer += letters.substring(x,x+1);
            }
        } while(x > 26);

        answer += "" + y;
        return answer;
    }

    //Saves the command used to assign each cell to a given text file
    void saveGrid(PrintWriter writer) {
        for(int i = 0; i < this.spreadSheet.length; i++) {
            for(int j = 0; j < this.spreadSheet[0].length; j++) {
                String temp = this.spreadSheet[i][j].getCommand();
                if(temp != null) {
                    writer.printf("%s\n", temp);
                }
            }
        }
        writer.close();
    }

    //Sets all cells in grid to blank
    void clear() {
        for(int i = 0; i < this.spreadSheet.length; i++) {
            for(int j = 0; j < this.spreadSheet[0].length; j++) {
                this.spreadSheet[i][j] = new Cell();
            }
        }
    }

    //Sets given cell to blank
    void clear(String cell) {
        String cellCoords = getSpace(cell);
        int column = Integer.parseInt(cellCoords.substring(0,cellCoords.indexOf(",")));
        int row = Integer.parseInt(cellCoords.substring(cellCoords.indexOf(",")+2));
        this.spreadSheet[row][column] = new Cell();
    }
}
