package org.example;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

public class Main {
    public static void main(String[] args) throws Exception {
        ArgParse.parser(args);
        ExtractPDF pdfExtractor = new ExtractPDF();
        pdfExtractor.extract(ArgParse.commandValue, ArgParse.inputValue, ArgParse.outputValue, ArgParse.recursive);

        /*System.out.println("Hello ITnetwork!");
        for (String str: args) {
            System.out.println(str);
        }
        File file = new File("pdf/test_file.pdf");
        PDDocument doc = PDDocument.load(file);

        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setSortByPosition( true );

        String text = pdfStripper.getText(doc);
        String[] rows = text.split("\\n");
        int row_counter = rows.length;

        PDDocumentInformation pdd = doc.getDocumentInformation();
        String producer = pdd.getProducer();
        String author = pdd.getAuthor();

        if(producer.startsWith("XEP")){
            System.out.println("Spolecnost BSCI");
            System.out.println("---------------------------");

            String[] firstRow = rows[0].split("\\s");
            String firstHeader = "";
            String secondHeader = "";

            for(int i = 0; i < firstRow.length; i++){
                if(firstRow[i].equals("Report")){
                    for(; i < firstRow.length; i++){
                        secondHeader += firstRow[i] + " ";
                    }
                }
                else{
                    firstHeader += firstRow[i] + " ";
                }
            }

            System.out.println(firstHeader);
            System.out.println(secondHeader);

            String[] secondRow = rows[0].split("\\s");

        }

        if(author.startsWith("BIOTRONIK")){
            System.out.println("Spolecnost BIO");
        }

        int col_counter = 0;

        int space_counter;

        for (int i = 0; i < row_counter; i++){
            space_counter = 0;

            for(int j = 0; j < rows[i].length(); j++){
                if(rows[i].charAt(j) == ' '){
                    space_counter++;
                }
            }

            if(space_counter > col_counter){
                col_counter = space_counter;
            }
        }

        String[][] stringArray = new String[col_counter][row_counter];

        System.out.println(col_counter);
        System.out.println(text);

        doc.save("pdf/blank.pdf");

        doc.close();*/
    }
}