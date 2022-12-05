package org.example;

import java.util.ArrayList;
import java.util.List;

public class Command {
    String columnName;
    String title;
    String section;
    String subsection;
    String columnNumber;

    public Command() {
        this.columnName = "";
        this.title = "";
        this.section = "";
        this.subsection = "";
        this.columnNumber = "";
    }

    public void setColumnName(String columnName){
        this.columnName = columnName;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setSection(String section){
        this.section = section;
    }

    public void setSubsection(String subsection){
        this.subsection = subsection;
    }

    public void setColumnNumber(String columnNumber){
        this.columnNumber = columnNumber;
    }
}
