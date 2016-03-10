package com.company;

public class DateCell extends Cell{
    private String inputValue;
    private String value;

    //Turns value into a printable string
    public String toString() {
        return this.getValueString().substring(0,9);
    }

    //Initializes cell with all needed methods being called to get all values set
    public DateCell(String input) {
        setCommand(input);
        setValue();
    }

    //Sets command to needed console input to assign cell
    public void setCommand(String input) {
        this.inputValue = input;
    }

    //Sets value to value
    public void setValue() {
        this.value = this.inputValue.substring(this.inputValue.indexOf("=")+2);
    }

    //Gets the console command for saving
    public String getCommand() {
        return this.inputValue;
    }

    //Gets the value
    public String getValueString() {
        return this.value;
    }

    //Returns the type of the cell
    public String getType() {
        return "DateCell";
    }
}
