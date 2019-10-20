package org.chori.gsg.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.chori.gsg.model.*;
import org.chori.gsg.view.*;

public class SearchData {

    private static HashMap<String, JTextArea> whichTextArea = new HashMap<>();

	public SearchData() {
        whichTextArea.put("HLA", B12xGui.resultsTextAreaHla);
        whichTextArea.put("NAME", B12xGui.resultsTextAreaName);
	}

	public void searchThroughData(File file, String regex, String dataFormat, String whichTab) {

        int i = 0;
        String line,
               timeStamp = LocalDateTime.now().toString();
        HashMap<String, String> unsortedData = new HashMap();
        LinkedHashMap<String, String> sortedDataMatches = new LinkedHashMap();
        // boolean hlaWriteToFileChecked = prefs.getBoolean("BSG_HLA_SAVE_FILE", false);        
        JTextArea printToMe = whichTextArea.get("HLA"); //whichTab);
        System.out.println("print to me: " + printToMe);
        
        System.out.println("Made it to SearchData: " + regex);
            
        try {    
            // Read the File
            BufferedReader br = new BufferedReader(new FileReader(file));
            String fileDate = br.readLine();
            String version = br.readLine();

            // the first dataline to check which side is GFE
            String[] gfeAlleles = br.readLine().split(",");

            // which side is the GFE? (Old files use names as key, new ones gfe)
            // we want the side that doesn't contain the asterisk.
            int gfe = 1;
            if(gfeAlleles[1].contains("*")) { gfe = 0; }

            switch(whichTab) {
                case "HLA":
                    if (gfeAlleles[gfe].matches(regex)){
                            
                        // if the first data line matches the regex, add to hashmap
                        unsortedData.put(gfeAlleles[gfe], gfeAlleles[1 - gfe]);
                        i++;
                    }
            
                    // screen the rest of the data
                    while ((line = br.readLine()) != null) {
                        
                        // use comma as separator
                        gfeAlleles = line.split(",");
                        if (gfeAlleles[gfe].matches(regex)){
                            
                            unsortedData.put(gfeAlleles[gfe], gfeAlleles[1 - gfe]);
                            i++;
                        }
                    }
                    break;
                case "NAME":
                    if (gfeAlleles[1 - gfe].matches(regex)){
                            
                        // if the first data line matches the regex, add to hashmap
                        unsortedData.put(gfeAlleles[gfe], gfeAlleles[1 - gfe]);
                        i++;
                    }
            
                    // screen the rest of the data
                    while ((line = br.readLine()) != null) {
                        
                        // use comma as separator
                        gfeAlleles = line.split(",");
                        if (gfeAlleles[1 - gfe].matches(regex)){
                            
                            unsortedData.put(gfeAlleles[gfe], gfeAlleles[1 - gfe]);
                            i++;
                        }
                    }
                    break;
                default:
                    System.out.println("I don't know how to process the data");
            }

            // Close the buffer
            br.close();

        } catch (Exception ex) { System.out.println(ex); }

        // sort the data: passed on with GFE as key
        SortData sorting = new SortData();
        sortedDataMatches = sorting.sortTheData(unsortedData);
        
        // print the sorted data to the appropriate screen 
        for (Map.Entry me:sortedDataMatches.entrySet()) {

            switch (dataFormat){
                case "CSV":
                    // System.out.println("Reached CSV in switch in SearchData");
                    // whichTextArea.get(whichTab).append("test");
                    // B12xGui.resultsTextAreaHla.append("Test");
                    printToMe.append(me.getValue() + "," + me.getKey());
                    printToMe.append(System.lineSeparator());
                    break;
                case "TSV":
                    // System.out.println("Reached TSV in switch in SearchData");
                    printToMe.append((String)me.getValue()+ "\t" + (String)me.getKey());
                    // printToMe.append("Test");
                    printToMe.append(System.lineSeparator());
                    break;
                default:
                    System.out.println("SearchData is looking for a format that isn't listed.");
            }
        }

        // Footer
        if (i == 0){
            B12xGui.resultsTextAreaHla.append("No results found");
        } else {
            B12xGui.resultsTextAreaHla.append("Total Results: " + i);
        }



        // Write contents of textbox to file
        // if(hlaWriteToFileChecked){
        //     WriteFile fileWriter = new WriteFile();
        //     fileWriter.writeToFile(locus, version, "HLA", dataType);
			
        // }
        
    }
}