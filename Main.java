/**
 * Daniel Bachler
 * 4/7/16
 * VisiCalc.java
 *  An implementation of VisiCalc in java
 */

/**If you get an exit code 4 that means you made a mistake.  Exit code 4 is the exit code designated for failed input
 * and things that the program knows it cant handle*/
/**
 * Extra Credit: PEMDAS (Parentheses, SUM, AVG, Multiplication, Division, Addition, Subtraction), nested parentheses, some error checking
 * SUM, AVG, and SORT can do rectangular areas, grid can be any size(only limit is array sizes in java)
 * */

package com.company;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;

// TODO: Add forms for help, boot up message, and quit.  Get user input from window to work

public class Main {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException{
        //Setting up background tasks
        String input;
        //Scanner inputReader = new Scanner(System.in); //Reads Console Input
        Grid grid = new Grid(7, 10); //Creating the grid
        Display display = new Display(grid);
        //The Writer and reader used to load/save grids
        PrintWriter writer = new PrintWriter("temp.txt");
        Scanner fileReader = new Scanner("temp.txt");

        //Program begins
        System.out.print("Welcome to VisiCalc, enter help for a list of commands.\n>");
        input = display.getInput();
        //Boolean pointer for use in the load function
        boolean[] keepGoing = {true};
        while(!input.toLowerCase().equals("quit") && keepGoing[0]) {
            //Checks input against commands, defaults to treating input as assigning
            if (input.toLowerCase().equals("help")) {
                help();
            } else if (input.toLowerCase().equals("print")) {
                grid.print();
            } else if (input.length() > 4 && input.toLowerCase().contains("save")) {
                if (input.substring(0, 4).toLowerCase().equals("save")) {
                    if (input.toLowerCase().contains(".txt")) {
                        writer = new PrintWriter(input.substring(5), "utf-8");
                        grid.saveGrid(writer);
                    }
                }
            } else if (input.length() > 4 && input.toLowerCase().contains("load")) {
                if (input.substring(0, 4).toLowerCase().equals("load")) {
                    if (input.toLowerCase().contains(".txt")) {
                        File temp = new File(input.substring(5));
                        fileReader = new Scanner(temp);
                        processFile(fileReader, grid, writer, keepGoing);
                    }
                }
            } else if (input.toLowerCase().equals("clear")) {
                grid.clear();
            } else if (input.toLowerCase().contains("clear")) {
                grid.clear(input.substring(input.indexOf(" ") + 1));
            } else if(input.length() > 5 && input.contains("sorta")) {
                if(input.toLowerCase().substring(0,5).equals("sorta")) {
                    //Give range values
                    sortMath(input.substring(6), grid, true);
                }
            } else if(input.length() > 5 && input.contains("sortd")) {
                if(input.toLowerCase().substring(0,5).equals("sortd")) {
                    //Give range values
                    sortMath(input.substring(6), grid, false);
                }
            } else if(input.equals("window")) {
                Display test = new Display(grid);
            } else {
                processInput(input, grid);
            }
            if(keepGoing[0])
                System.out.print(">");
                do {
                    input = display.getInput();
                    System.out.print
                }   while(input.equals(""));
        }
        //Closes file i/o and message
        writer.close();
        fileReader.close();
        System.out.printf("\nThanks for using VisiCalc!");
    }

    //Help command, prints out useful info
    public static void help() {
        System.out.print("Commands:\n" +
                "Print: Prints the grid\n" +
                "Quit: Exits program\n" +
                "Save: Saves grid to a specified text file\n" +
                "Load: Loads grid from a specified text file\n");
    }

    //This method is called if the input is not a command, thus meaning a cell is being assigned
    /**
     * Checks if input is valid, then determines type of input
     * Gets spot in array of cell and changes it to needed type
     * Seperate input into cell to assign, and value its being given
     * Determine what the type the value is, then create that type of cell
     * At the toAssign spot, also save input to Cells command value for saving
     */
    public static void processInput(String input, Grid grid) {
        //If input does not contain the '=' operator skips it since its invalid
        if(input.contains("=")) {
            //Gets cell and value given to cell into 2 separate strings
            if(!input.contains(" ")) {
                System.out.println("Bad Input TBQH Fam");
            } else {
                String cellToAssign = input.substring(0, input.indexOf(" "));
                String assignValue = input.substring(input.indexOf("=") + 2);
                // 00/00/0000
                // 0123456789
                //Gets the spot in the array of the cell and puts them into their respective ints
                String cellCoords = Grid.getSpace(cellToAssign);
                int column = Integer.parseInt(cellCoords.substring(0, cellCoords.indexOf(","))), row = Integer.parseInt(cellCoords.substring(cellCoords.indexOf(",") + 2));
                //Figures out which kind of cell the input is and creates a new cell of that type at the
                //specified spot
                if (assignValue.length() == 10 && assignValue.charAt(2) == '/' && assignValue.charAt(5) == '/') {
                    grid.spreadSheet[row][column] = new DateCell(input);
                } else if (assignValue.contains("(")) {
                    try {
                        grid.spreadSheet[row][column] = new FormulaCell(input, grid);
                    } catch (Exception e) {
                        System.out.println("Invalid attempt try again");
                    }
                } else if (assignValue.contains("\"")) {
                    grid.spreadSheet[row][column] = new TextCell(input);
                } else {
                    //Scanner makes sure that the input is valid
                    Scanner findDouble = new Scanner(input.substring(input.indexOf("=") + 2));
                    if (findDouble.hasNextDouble()) {
                        if(!(row > grid.spreadSheet.length || column > grid.spreadSheet[0].length)) {
                            grid.spreadSheet[row][column] = new NumberCell(input);
                        } else {System.out.println("Terrible job TBQH Fam");}
                    } else {
                        System.out.printf("%s is invalid\n", input);
                    }
                    findDouble.close();
                }
            }
        } else {
            //Checks whether or not input is a valid location to return input value of
            String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String numbers = "0123456789";
            boolean isGood = false;
            for(int i = 0; i < letters.length(); i++) {
                if(i < letters.length() - 1) {
                    if(input.contains(letters.substring(i,i+1))) {
                        isGood = true;
                    }
                } else {
                    if(input.contains(letters.substring(i))) {
                        isGood = true;
                    }
                }
            }
            if(isGood) {
                isGood = false;
                for(int i = 0; i < numbers.length(); i++) {
                    if(i < numbers.length() - 1) {
                        if(input.contains(numbers.substring(i, i+1))) {
                            isGood = true;
                        }
                    } else {
                        if(input.contains(numbers.substring(i))) {
                            isGood = true;
                        }
                    }
                }
            }
            //Prints either value of single cell or warns of invalid input
            if(isGood) {
                String cellCoords = Grid.getSpace(input);
                int column = Integer.parseInt(cellCoords.substring(0,cellCoords.indexOf(","))), row = Integer.parseInt(cellCoords.substring(cellCoords.indexOf(",")+2));
                if(row <= grid.spreadSheet.length && column <= grid.spreadSheet[0].length) {
                    System.out.println(grid.spreadSheet[row][column].getCommand());
                } else {
                    System.out.println("Area out of bounds try again");
                }
            } else {
                System.out.printf("%s is invalid, try again\n", input);
            }
        }
    }

    //Reads each line of a file and runs it through the input processor, same as main method just uses nextLine instead of input
    public static void processFile(Scanner fileReader, Grid grid, PrintWriter writer, boolean[] keepGoing) throws FileNotFoundException, UnsupportedEncodingException{
        while(fileReader.hasNextLine()) {
            String nextLine = fileReader.nextLine();
            if(nextLine.toLowerCase().equals("help")) {
                help();
            } else if(nextLine.toLowerCase().equals("print")) {
                grid.print();
            } else if(nextLine.length() > 4 && nextLine.toLowerCase().contains("save")) {
                if(nextLine.substring(0,4).toLowerCase().equals("save")) {
                    if (nextLine.toLowerCase().contains(".txt")) {
                        writer = new PrintWriter(nextLine.substring(5), "utf-8");
                        grid.saveGrid(writer);
                    }
                }
            } else if(nextLine.length() > 4 && nextLine.toLowerCase().contains("load")) {
                if (nextLine.substring(0, 4).toLowerCase().equals("load")) {
                    if (nextLine.toLowerCase().contains(".txt")) {
                        File temp = new File(nextLine.substring(5));
                        fileReader = new Scanner(temp);
                        processFile(fileReader, grid, writer, keepGoing);
                    }
                }
            } else if(nextLine.toLowerCase().equals("clear")) {
                grid.clear();
            } else if(nextLine.toLowerCase().contains("clear")){
                grid.clear(nextLine.substring(nextLine.indexOf(" ")+1));
            } else if(nextLine.toLowerCase().equals("quit")) {
                keepGoing[0] = false;
            } else if(nextLine.length() > 5 && nextLine.contains("sorta")) {
                if(nextLine.toLowerCase().substring(0,5).equals("sorta")) {
                    //Give range values
                    sortMath(nextLine.substring(6), grid, true);
                }
            } else if(nextLine.length() > 5 && nextLine.contains("sortd")) {
                if(nextLine.toLowerCase().substring(0,5).equals("sortd")) {
                    //Give range values
                    sortMath(nextLine.substring(6), grid, false);
                }
            } else {
                processInput(nextLine, grid);
            }
        }
        fileReader.close();
    }

    //Repeating logic for both sort types
    public static void sortMath(String range, Grid grid, boolean AorD) {
        //Logic for range
        int fromX = 0, fromY = 0, toX = 0, toY = 0;
        if(range.contains("-")) {
            //Gets the cell values, then string of coord pair
            String firstCell = range.substring(0, range.indexOf(" "));
            String secondCell = range.substring(range.indexOf("-")+2);
            firstCell = Grid.getSpace(firstCell);
            secondCell = Grid.getSpace(secondCell);
            //Getting coords of cells into ints
            fromY = Integer.parseInt(firstCell.substring(0, firstCell.indexOf(",")));
            fromX = Integer.parseInt(firstCell.substring(firstCell.indexOf(",") + 2));
            toY = Integer.parseInt(secondCell.substring(0, secondCell.indexOf(",")));
            toX = Integer.parseInt(secondCell.substring(secondCell.indexOf(",") + 2));
        } else {
           System.exit(4);
        }
        //Creating array by counting size
        int size = 0;
        for(int y = fromY; y <= toY; ++y) {
            for(int x = fromX; x <= toX; ++x) {
                if(grid.spreadSheet[x][y].getType().equals("NumberCell")) {
                    ++size;
                } else {
                    System.exit(4);
                }
            }
        }
        Cell[] sortMe = new Cell[size];
        //Filling Array
        for(int y = fromY, i = 0; y <= toY; ++y) {
            for(int x = fromX; x <= toX; ++x, ++i) {
                sortMe[i] = grid.spreadSheet[x][y];
            }
        }
        //Sorting Array
        if(AorD)
            Arrays.sort(sortMe);
        else
             //Uses an inverse comparator to sort backwards
             Arrays.sort(sortMe, Collections.reverseOrder());
        //Putting values back
        for(int y = fromY, i = 0; y <= toY; ++y) {
            for(int x = fromX; x <= toX; ++x, ++i) {
                grid.spreadSheet[x][y] = sortMe[i];
            }
        }
    }
}
