package com.company;

public class TextCell extends Cell {
    private String inputValue;
    private String value;

    //Turns value of cell into printable string
    public String toString() {
        String returnMe;
        if(this.getValueString().length() > 9) {
            returnMe = this.getValueString().substring(0,9);
        } else {
            returnMe = this.getValueString().substring(0,this.getValueString().length()-1);
        }
        return returnMe;
    }

    //When cell is initialized calls needed methods to set up values
    public TextCell(String input) {
        this.setCommand(input);
        this.setValue(input);
    }

    //Sets the assigning command to the assigned command
    public void setCommand(String input) {
        this.inputValue = input;
    }

    //Sets the actual value of the cell
    public void setValue(String input) {
        this.value = input.substring(input.indexOf("=")+3, input.length());
    }

    //Gets the statement needed to set cell up
    public String getCommand() {
        return this.inputValue;
    }

    //Gets the actual value of the cell
    public String getValueString() {
        return this.value;
    }

    //Returns the type of the cell
    public String getType() {
        return "TextCell";
    }
}
