package org.example;

import java.util.ArrayList;
import java.util.List;

public class Command {
    String Title;
    String Section;
    String Subsection;
    List<String> columnNumberList = new ArrayList<>();

    public Command() {
        this.Title = "";
        this.Section = "";
        this.Subsection = "";
    }

    public void setTitle(String Title){
        this.Title = Title;
    }

    public void setSection(String Section){
        this.Section = Section;
    }

    public void setSubsection(String Subsection){
        this.Subsection = Subsection;
    }

    public void appendColumnList(String columnNumber){
        this.columnNumberList.add(columnNumber);
    }
}
