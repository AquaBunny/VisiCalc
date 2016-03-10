package com.company;

public class NumberCell extends Cell {
    private String inputValue;
    private double value;

    //Turns number into a printable string
    public String toString() {
        double temp = this.getValueDouble();
        String returnMe;
        if(temp % 1 == 0) {
            returnMe =  "" + (int) temp;
        } else {
            returnMe = "" + this.getValueDouble();
        }
        if(returnMe.length() > 9) {
            return returnMe.substring(0,9);
        }
        return returnMe;
    }

    //Sets inputValue to console command to assign cell
    public void setCommand(String inputValue) {
        this.inputValue = inputValue;
    }

    //Gets the console value for saving
    public String getCommand() {
        return this.inputValue;
    }

    //Sets the value
    public void setValue() {
        this.value = Double.parseDouble(this.inputValue.substring(this.inputValue.indexOf("=")+1));
    }

    //Gets the value
    public double getValueDouble() {
        return this.value;
    }

    //Returns the type of the cell
    public String getType() {
        return "NumberCell";
    }

    //Initializes cell by calling needed methods for all values to be assigned
    public NumberCell(String input) {
        this.setCommand(input);
        this.setValue();
    }
}
