package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fit.pdfdom.PDFBoxTree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractPDF {
    List<Command> commandList;
    List<String> pdfList;
    List<String> recursiveDirectories;
    int errorRow;
    int errorColumn;

    public ExtractPDF(){
        this.commandList = new ArrayList<>();
        this.pdfList = new ArrayList<>();
        this.recursiveDirectories = new ArrayList<>();
    }

    public void extract(String command, String input, String output, boolean recursive) throws Exception {
       if(!checkSyntax(command)){
           System.out.println("Error: No valid syntax in command file was given. Error at row: " + ++errorRow + ", column: " + ++errorColumn + " (File location: " + command + ")");
           System.exit(1);
       }

       for(Command com : commandList){
           System.out.println("-----------------");
           System.out.println("Column name: " + com.columnName);
           System.out.println("Title: " + com.title);
           System.out.println("Section: " + com.section);
           System.out.println("Subsection: " + com.subsection);
           System.out.println("Column number: " + com.columnNumber);
           System.out.println("-----------------");
       }

       if(!scanInputDirectory(input, recursive)){
           System.out.println("Error: No PDF files were given! (Directory: " + input + ")");
       }

       System.out.println("PDF files: ");
       for(String pdf : pdfList){
           System.out.println(pdf + " ");
       }

       for(String pdf : pdfList){
           File pdfFile = new File(pdf);
           PDDocument pdfDocument = PDDocument.load(pdfFile);
           /*PDFTextStripper stripper = new PDFTextStripper();
           stripper.setSortByPosition(true);

           String text = stripper.getText(pdfDocument);
           System.out.println(text);*/

           GetData data = new GetData();
           data.setSortByPosition(true);

           String loop = data.getText(pdfDocument);
           System.out.println(loop);

           /*PDFTextStripper pdfStripper = new PDFTextStripper();
           pdfStripper.setSortByPosition( true );

           String text2 = String.valueOf(pdfStripper.getGraphicsState().getTextState().getRenderingMode());
           System.out.println(text2);

           String text = pdfStripper.getText(pdfDocument);
           String[] rows = text.split("\\n");*/
           pdfDocument.close();
       }
    }

    private boolean checkSyntax(String command) throws IOException {
        BufferedReader buffer;

        if(command.equals("-")){
            buffer = new BufferedReader(new InputStreamReader(System.in));
        } else {
            File file = new File(command);
            buffer = new BufferedReader(new FileReader(file));
        }

        int commandRowCounter = 0;
        int commandRowCharCounter = 0;
        String commandRow;

        boolean hasColumnName;
        boolean hasTitleIndex;
        boolean hasTitle;
        boolean hasSectionIndex;
        boolean hasSection;
        boolean hasSubsectionIndex;
        boolean getName;
        boolean sectionChecked;
        boolean subsectionChecked;
        boolean hasColumnNumber;

        while ((commandRow = buffer.readLine()) != null) {
            Command commandObj = new Command();

            hasColumnName = false;
            hasTitleIndex = false;
            hasTitle = false;
            hasSectionIndex = false;
            hasSection = false;
            hasSubsectionIndex = false;
            getName = true;
            sectionChecked = false;
            subsectionChecked = false;
            hasColumnNumber = false;

            while (commandRowCharCounter < commandRow.length()) {
                while (commandRowCharCounter < commandRow.length() && (commandRow.charAt(commandRowCharCounter) == ' ' || commandRow.charAt(commandRowCharCounter) == '\t')) {
                    commandRowCharCounter++;

                    if (commandRowCharCounter == commandRow.length()) {
                        errorRow = commandRowCounter;
                        errorColumn = commandRowCharCounter;
                        return false;
                    }
                }

                if (getName) {
                    if (commandRow.charAt(commandRowCharCounter) == '\"') {
                        commandRowCharCounter++;

                        int titleStartIndex = commandRowCharCounter;
                        while (commandRowCharCounter < commandRow.length()) {

                            if (commandRow.charAt(commandRowCharCounter) == '\"' && commandRow.charAt(commandRowCharCounter - 1) != '\\') {
                                break;
                            }

                            commandRowCharCounter++;
                            if (commandRowCharCounter == commandRow.length()) {
                                errorRow = commandRowCounter;
                                errorColumn = commandRowCharCounter;
                                return false;
                            }
                        }

                        StringBuilder builder = new StringBuilder();
                        while (titleStartIndex < commandRowCharCounter) {
                            builder.append(commandRow.charAt(titleStartIndex));
                            titleStartIndex++;
                        }
                        String name = builder.toString();

                        if (!hasColumnName) {
                            commandObj.setColumnName(name);
                            hasColumnName = true;
                        } else if (!hasTitle) {
                            commandObj.setTitle(name);
                            hasTitle = true;
                        } else if (hasSectionIndex && !hasSection) {
                            commandObj.setSection(name);
                            hasSection = true;
                        } else {
                            commandObj.setSubsection(name);
                        }

                        getName = false;
                        commandRowCharCounter++;
                        continue;

                    } else {
                        errorRow = commandRowCounter;
                        errorColumn = commandRowCharCounter;
                        return false;
                    }
                }

                if (!hasTitleIndex) {
                    if (commandRow.charAt(commandRowCharCounter) == ':') {
                        hasTitleIndex = true;
                        getName = true;
                        commandRowCharCounter++;
                        continue;
                    } else {
                        errorRow = commandRowCounter;
                        errorColumn = commandRowCharCounter;
                        return false;
                    }
                }

                if (!hasSectionIndex && !sectionChecked) {
                    if (commandRow.charAt(commandRowCharCounter) == '-') {
                        hasSectionIndex = true;
                        getName = true;
                        commandRowCharCounter++;
                        sectionChecked = true;
                        continue;
                    } else {
                        sectionChecked = true;
                    }
                }

                if (!hasSubsectionIndex && !subsectionChecked) {
                    if (commandRow.charAt(commandRowCharCounter) == '>') {
                        hasSubsectionIndex = true;
                        getName = true;
                        commandRowCharCounter++;
                        subsectionChecked = true;
                        continue;
                    } else {
                        subsectionChecked = true;
                    }
                }

                if(!hasColumnNumber) {
                    if (commandRow.charAt(commandRowCharCounter) == '[') {
                        commandRowCharCounter++;

                        if (commandRowCharCounter < commandRow.length()) {
                            if (commandRow.charAt(commandRowCharCounter) == '$') {
                                commandRowCharCounter++;

                                StringBuilder builder = new StringBuilder();
                                while (commandRowCharCounter < commandRow.length() && Character.isDigit(commandRow.charAt(commandRowCharCounter))) {
                                    builder.append(commandRow.charAt(commandRowCharCounter));
                                    commandRowCharCounter++;

                                    if (commandRowCharCounter == commandRow.length()) {
                                        errorRow = commandRowCounter;
                                        errorColumn = commandRowCharCounter;
                                        return false;
                                    }
                                }

                                if (!(commandRow.charAt(commandRowCharCounter) == ']')) {
                                    errorRow = commandRowCounter;
                                    errorColumn = commandRowCharCounter;
                                    return false;
                                }

                                String columnNumber = builder.toString();
                                commandObj.setColumnNumber(columnNumber);
                                hasColumnNumber = true;
                                commandRowCharCounter++;
                            } else {
                                errorRow = commandRowCounter;
                                errorColumn = commandRowCharCounter;
                                return false;
                            }
                        } else {
                            errorRow = commandRowCounter;
                            errorColumn = commandRowCharCounter;
                            return false;
                        }

                    } else {
                        errorRow = commandRowCounter;
                        errorColumn = commandRowCharCounter;
                        return false;
                    }
                } else {
                    errorRow = commandRowCounter;
                    errorColumn = commandRowCharCounter;
                    return false;
                }

            }

            commandRowCharCounter = 0;
            commandRowCounter++;
            this.commandList.add(commandObj);
        }

        return true;
    }

    private boolean scanInputDirectory(String input, boolean recursive) {
        if(input.equals("-")){
            input = System.getProperty("user.dir") + '/';
        } else {
            if (input.charAt(input.length() - 1) != '/') {
                input += '/';
            }
        }

        File inputFile = new File(input);
        File[] inputDirFiles = inputFile.listFiles();

        if(inputDirFiles == null){
            return false;
        }

        for(File file : inputDirFiles){
            if(file.isDirectory()){
                this.recursiveDirectories.add(input + file.getName() + '/');
            } else {
                if(isPDF(file.getName())) {
                    this.pdfList.add(input + file.getName());
                }
            }

        }

        if(recursive){
            while(!this.recursiveDirectories.isEmpty()){
                File subdirFile = new File(recursiveDirectories.get(0));
                File[] recursiveDirFiles = subdirFile.listFiles();

                if(recursiveDirFiles == null){
                    recursiveDirectories.remove(0);
                    continue;
                }

                for(File recursiveFile : recursiveDirFiles){
                    if(recursiveFile.isDirectory()){
                        this.recursiveDirectories.add(recursiveDirectories.get(0) + recursiveFile.getName() + '/');
                    } else {
                        if(isPDF(recursiveFile.getName())) {
                            this.pdfList.add(recursiveDirectories.get(0) + recursiveFile.getName());
                        }
                    }
                }

                recursiveDirectories.remove(0);
            }
        }

        return true;
    }

    public static boolean isPDF(String pdfName) {
        int position = pdfName.length() - 1;
        String fileExtension = "";

        while(position >= 0){
            if(pdfName.charAt(position) == '.'){
                position++;

                StringBuilder builder = new StringBuilder();
                while (position < pdfName.length()) {
                    builder.append(pdfName.charAt(position));
                    position++;
                }
                fileExtension = builder.toString();
                break;
            }

            position--;
        }

        Pattern pattern = Pattern.compile("pdf", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileExtension);

        return matcher.find();
    }
}

