package com.company;

import java.util.ArrayList;

public class Cell implements Comparable<Cell>{
    private String inputValue;
    private ArrayList<String> dependencies = new ArrayList<>();
    private ArrayList<Double> valuesofDependencies = new ArrayList<>();

    //Generic toString, overridden by all children
    public String toString() {

        return "";
    }

    //Generic getCommand overridden by all children
    public String getCommand() {
        return this.inputValue;
    }

    //Generic setCommand overridden by all children
    public void setCommand(String inputValue) {
        this.inputValue = inputValue;
    }

    //Gets the value of the cell
    public String getValueString() {
        return "";
    }

    public double getValueDouble() {
        return 0.0;
    }

    //Returns the type of the cell
    public String getType() {
        return "Cell";
    }

    public int compareTo(Cell temp) {
        String thisType = this.getType();
        String tempType = temp.getType();
        if(thisType.equals("NumberCell") && tempType.equals("NumberCell")) {
            if(this.getValueDouble() > temp.getValueDouble()) {
                return 1;
            } else if(this.getValueDouble() == temp.getValueDouble())
                return 0;
            else
                return -1;
        } else {
            System.exit(4);
            return 0;
        }
    }

    //Will run through the list of dependencies and update the cells contents
    public void upDateCell() {

    }
}
