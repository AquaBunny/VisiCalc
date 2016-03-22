package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class FormulaCell extends Cell{
    private String inputValue;
    private String formulaValue;
    private double value;
    private Grid grid;
    private boolean isBad;
    private ArrayList<String> dependencies = new ArrayList<>();
    private ArrayList<Double> valuesOfDependencies = new ArrayList<>();

    //turns value to display into string
    public String toString() {
        double temp = this.getValueDouble();
        String returnMe;
        if(temp % 1 == 0) {
            returnMe =  "" + (int) temp;
        } else {
            returnMe = "" + this.getValueDouble();
        }
        if(isBad)
            return "NaN";
        if(returnMe.length() > 9) {
            return returnMe.substring(0,9);
        }
        return returnMe;
    }

    //Creates the cell and calls all needed methods to properly initialize the cell
    public FormulaCell(String input, Grid grid) {
        setCommand(input);
        setFormulaValue(input);
        this.grid = grid;
        setValue(this.getFormulaValue());
    }

    //Sets the input value used to save the cells value
    public void setCommand(String input) {
        this.inputValue = input;
    }

    //Sets the formulaValue to the unsimplified value
    public void setFormulaValue(String input) {
        this.formulaValue = input.substring(input.indexOf("=")+2);
    }

    //Sets the value to the simplified value that will be displayed
    public void setValue(String formula) {
        this.value = calculations(formula.substring(formula.indexOf("(")+2, formula.length()-2));
    }

    //Gets the command to assign cell
    public String getCommand() {
        return this.inputValue;
    }

    //Gets the value of the unsimplified formula
    public String getFormulaValue() {
        return this.formulaValue;
    }

    //Gets the value of the formula
    public double getValueDouble() {
        return this.value;
    }

    //Returns the type of the cell
    public String getType() {
        return "FormulaCell";
    }

    //Turns unsimplified formulas into simplified ones
    /**
     * Given a formula calculates the value in a float format
     * Order of Ops is: () > SUM == AVG > * == / > + == -
     */
    public double calculations(String input) {
        isBad = false;
        double answer = 0;
        boolean isGoodToSolve = true;
        //All this determines the order of operations for the calculations
        Scanner reader = new Scanner(input);
        int length = 0;
        //Gets the amount of tokens in input
        while(reader.hasNext()) {
            reader.next();
            ++length;
        }
        //Resets the scanner and creates a String array with the correct length
        reader = new Scanner(input);
        String[] components = new String[length];
        for(int i = 0; i < length; ++i) {
            components[i] = reader.next();
        }
        //Gets the total number of operators
        int opLength = 0;
        for(int i = 0; i < length; ++i) {
            String temp = components[i];
            if(temp.equals("SUM") || temp.equals("AVG")) {
                ++opLength;
                if(components[i+2].equals("-")) {
                    components[i+2] = "_";
                }
            }
            if(temp.equals("*") || temp.equals("/") || temp.equals("+") || temp.equals("-") || temp.equals("(") || temp.equals(")")) {
                ++opLength;
            }

        }
        //Creates an array that holds the order in which operations will be called
        int[] OrdOfOps = new int[opLength];
        int OrdOfOpsCounter = 0;
        //Checks for the location of SUM, AVG, and parentheses
        for(int i = 0; i < length; ++i) {
            //With parentheses the first number is the open and the second is the close
            if(components[i].equals(")")) {
                ++OrdOfOpsCounter;
                OrdOfOps[OrdOfOpsCounter] = i;
                int temp = i;
                while(!components[temp].equals("(")) {
                    --temp;
                }
                OrdOfOps[OrdOfOpsCounter-1] = temp;
                components[i] = "end";
                components[temp] = "start";
                ++OrdOfOpsCounter;
                //To keep the "-" in SUM and AVG from being counted as an operator it is replaced with an "_"
            } else if(components[i].equals("SUM") || components[i].equals("AVG")){
                OrdOfOps[OrdOfOpsCounter] = i;
                ++OrdOfOpsCounter;
                for(int j = i; j < length; ++j) {
                    if(components[j].equals("-")) {
                        components[j] = "_";
                        --opLength;
                        break;
                    }
                }
            }
        }
        //Gets the spaces of * and / into OrdOfOps
        for(int i = 0; i < length; ++i){
            if(components[i].equals("*") || components[i].equals("/")) {
                OrdOfOps[OrdOfOpsCounter] = i;
                ++OrdOfOpsCounter;
            }
        }
        //Gets the spaces of + and - into OrdOfOps
        for(int i = 0; i < length; ++i){
            if(components[i].equals("+") || components[i].equals("-")) {
                OrdOfOps[OrdOfOpsCounter] = i;
                ++OrdOfOpsCounter;
            }
        }
        //Error checking in formula
        boolean isGood = true;
        for(int i = 1; i < OrdOfOps.length; ++i) {
            if(OrdOfOps[i] == OrdOfOps[i-1] - 1) {
                isGood = false;
            }
        }



        //Actual Calculations
        if(isGood) {
            String letters = "ABCDEFGHIJKLMNOPQRSTUUVWXYZ";
            String numbers = "123456789000000000000000000";
            for(int i: OrdOfOps) {
                if(components[i].equals("SUM")) {
                    if(components.length >= i+3) {
                        //Gets the 2 coordinates for SUM
                        String from = components[i+1];
                        String to = components[i+3];
                        boolean bothCoords = false;
                        boolean fromLet = false, toLet = false, fromNum = false, toNum = false;
                        for(int j = 0; j < 26; ++j) {
                            if(from.contains(""+letters.charAt(j))) {
                                fromLet = true;
                            }
                            if(from.contains(""+numbers.charAt(j))) {
                                fromNum = true;
                            }
                            if(to.contains(""+letters.charAt(j))) {
                                toLet = true;
                            }
                            if(to.contains(""+numbers.charAt(j))) {
                                toNum = true;
                            }
                            if(fromLet && fromNum && toLet && toNum) {
                                bothCoords = true;
                                break;
                            }
                        }
                        //Makes sure that both inputs are coordinates
                        if(bothCoords) {
                            //Turns them into array spaces
                            from = Grid.getSpace(from);
                            to = Grid.getSpace(to);
                            //Setting up int values of coordinates
                            int fromY = Integer.parseInt(from.substring(0, from.indexOf(",")));
                            int fromX = Integer.parseInt(from.substring(from.indexOf(",") + 2));
                            int toY = Integer.parseInt(to.substring(0, to.indexOf(",")));
                            int toX = Integer.parseInt(to.substring(to.indexOf(",") + 2));
                            String space1Type = grid.spreadSheet[fromX][fromY].getType();
                            String space2Type = grid.spreadSheet[toX][toY].getType();
                            boolean invalid = false;
                            if (space1Type.equals("NumberCell") || space1Type.equals("FormulaCell") || space1Type.equals("Cell") && space2Type.equals("NumberCell") || space2Type.equals("FormulaCell") || space2Type.equals("Cell")) {
                                //Add up all cells between the given values
                                double total = 0;
                                for (int y = fromY; y <= toY; ++y) {
                                    for (int x = fromX; x <= toX; ++x) {
                                        if(grid.spreadSheet[x][y].getType().equals("NumberCell") || grid.spreadSheet[x][y].getType().equals("Cell") || grid.spreadSheet[x][y].getType().equals("FormulaCell")) {
                                            total += grid.spreadSheet[x][y].getValueDouble();
                                            dependencies.add(Grid.getCell(x,y));
                                            valuesOfDependencies.add(grid.spreadSheet[x][y].getValueDouble());
                                        }
                                        else {
                                            System.out.println("A cell in the given region is invalid");
                                            invalid = true;
                                            isGoodToSolve = false;
                                            isBad = true;
                                            break;
                                        }
                                    }
                                }
                                if(!invalid) {
                                    components[i] = "" + total;
                                    components[i + 3] = "" + total;
                                }
                            }
                        } else {
                            isGoodToSolve = false;
                            break;
                        }
                    }
                } else if(components[i].equals("AVG")) {
                    if(components.length >= i+3) {
                        //Gets the 2 coordinates for SUM
                        String from = components[i+1];
                        String to = components[i+3];
                        boolean bothCoords = false;
                        boolean fromLet = false, toLet = false, fromNum = false, toNum = false;
                        for(int j = 0; j < 26; ++j) {
                            if(from.contains(""+letters.charAt(j))) {
                                fromLet = true;
                            }
                            if(from.contains(""+numbers.charAt(j))) {
                                fromNum = true;
                            }
                            if(to.contains(""+letters.charAt(j))) {
                                toLet = true;
                            }
                            if(to.contains(""+numbers.charAt(j))) {
                                toNum = true;
                            }
                            if(fromLet && fromNum && toLet && toNum) {
                                bothCoords = true;
                                break;
                            }
                        }
                        //Makes sure that both inputs are coordinates
                        if(bothCoords) {
                            //Turns them into array spaces
                            from = Grid.getSpace(from);
                            to = Grid.getSpace(to);
                            //Setting up int values of coordinates
                            int fromY = Integer.parseInt(from.substring(0, from.indexOf(",")));
                            int fromX = Integer.parseInt(from.substring(from.indexOf(",") + 2));
                            int toY = Integer.parseInt(to.substring(0, to.indexOf(",")));
                            int toX = Integer.parseInt(to.substring(to.indexOf(",") + 2));
                            String space1Type = grid.spreadSheet[fromX][fromY].getType();
                            String space2Type = grid.spreadSheet[toX][toY].getType();
                            boolean invalid = false;
                            if (space1Type.equals("NumberCell") || space1Type.equals("FormulaCell") || space1Type.equals("Cell") && space2Type.equals("NumberCell") || space2Type.equals("FormulaCell") || space2Type.equals("Cell")) {
                                //Add up all cells between the given values
                                double total = 0;
                                int amount = 0;
                                for (int y = fromY; y <= toY; ++y) {
                                    for (int x = fromX; x <= toX; ++x) {
                                        if(grid.spreadSheet[x][y].getType().equals("NumberCell") || grid.spreadSheet[x][y].getType().equals("Cell") || grid.spreadSheet[x][y].getType().equals("FormulaCell")) {
                                            total += grid.spreadSheet[x][y].getValueDouble();
                                            ++amount;
                                            dependencies.add(Grid.getCell(x,y));
                                            valuesOfDependencies.add(grid.spreadSheet[x][y].getValueDouble());

                                        }
                                        else {
                                            System.out.println("A cell in the given region is invalid");
                                            invalid = true;
                                            isGoodToSolve = false;
                                            isBad = true;
                                            break;
                                        }
                                    }
                                }
                                if(!invalid) {
                                    components[i] = "" + total / amount;
                                    components[i + 3] = "" + total / amount;
                                }
                            }
                        } else {
                            isGoodToSolve = false;
                            break;
                        }
                    }
                } else if(components[i].equals("+")) {
                    double num1 = findNumberVal(components[i-1]);
                    double num2 = findNumberVal(components[i+1]);
                    double temp = num1 + num2;
                    components[i-1] = ""+temp;
                    components[i] = ""+temp;
                    components[i+1] = ""+temp;
                } else if(components[i].equals("-")) {
                    double num1 = findNumberVal(components[i-1]);
                    double num2 = findNumberVal(components[i+1]);
                    double temp = num1 - num2;
                    components[i-1] = ""+temp;
                    components[i] = ""+temp;
                    components[i+1] = ""+temp;
                } else if(components[i].equals("*")) {
                    double num1 = findNumberVal(components[i-1]);
                    double num2 = findNumberVal(components[i+1]);
                    double temp = num1 * num2;
                    components[i-1] = ""+temp;
                    components[i] = ""+temp;
                    components[i+1] = ""+temp;
                } else if(components[i].equals("/")) {
                    double num1 = findNumberVal(components[i-1]);
                    double num2 = findNumberVal(components[i+1]);
                    double temp = num1 / num2;
                    components[i-1] = ""+temp;
                    components[i] = ""+temp;
                    components[i+1] = ""+temp;
                }
            }
            if(OrdOfOps.length == 0) {
                answer = findNumberVal(components[0]);
            } else {
                if(isGoodToSolve)
                    answer = Double.parseDouble(components[OrdOfOps[OrdOfOps.length - 1]]);
                else
                    System.out.printf("Part of your formula was invalid try again\n");
            }
        }
        return answer;
    }

    //Given a component of the equation returns the number value
    public double findNumberVal(String input) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUUVWXYZ";
        boolean isCoord = false;
        double value;
        for(int i = 0; i < 26; ++i) {
            if(input.substring(0,1).contains(""+letters.charAt(i))) {
                isCoord = true;
            }
        }
        if(isCoord) {
            String temp = Grid.getSpace(input);
            int y = Integer.parseInt(temp.substring(0,temp.indexOf(",")));
            int x = Integer.parseInt(temp.substring(temp.indexOf(",")+2));
            value = this.grid.spreadSheet[x][y].getValueDouble();
            valuesOfDependencies.add(this.grid.spreadSheet[x][y].getValueDouble());
            dependencies.add(input);
        } else {
            value = Double.parseDouble(input);
        }
        return value;
    }

    //Will run through the list of dependencies and update the cells contents
    public void upDateCell() {
        boolean valuesSame = true;
        for(int i = 0; i < dependencies.size(); ++i) {
            String coords = Grid.getSpace(dependencies.get(i));
            int x = Integer.parseInt(coords.substring(0,coords.indexOf(",")));
            int y = Integer.parseInt(coords.substring(coords.indexOf(",")+2));
            if(grid.spreadSheet[x][y].getType().equals("Formula Cell")) {
                grid.spreadSheet[x][y].upDateCell();
            }
            if(!(valuesOfDependencies.get(i) == grid.spreadSheet[x][y].getValueDouble())) {
                valuesSame = false;
            }
        }
        if(!valuesSame)
            this.value = calculations(this.formulaValue.substring(this.formulaValue.indexOf("(")+2, this.formulaValue.length()-2));
    }
}
