package org.chori.bsg.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import javax.swing.JTextArea;

import org.chori.bsg.model.*;
import org.chori.bsg.view.*;

public class PrettyData {

	private	static HashMap<String, JTextArea> whichTextArea = new HashMap();
	private HashMap<String, String> dataMatches = new HashMap();
	private LinkedHashMap<String, String> sortedDataMatches = new LinkedHashMap();

	public PrettyData() {
		this.whichTextArea.put("HLA", B12xGui.resultsTextAreaHla);
	}

	public void searchThroughData(File file, String regex, String whichTab) {
		try {
			
			String line;
			int[] spacingList = new int[18];
			JTextArea printToMe = whichTextArea.get(whichTab);

			System.out.println("Made it to SearchData: " + regex);
			
			// Read the File
			BufferedReader br = new BufferedReader(new FileReader(file));
			String fileDate = br.readLine();
			String version = br.readLine();
			String firstDataLine = br.readLine();

			// use comma as separator
			String[] gfeAlleles = firstDataLine.split(",");

			// which side is the GFE? (Old files use names as key, new ones gfe)
			// we want the side that doesn't contain the asterisk.
			int gfe = 1;
			if(gfeAlleles[1].contains("*")) { gfe = 0; }
			System.out.println(gfeAlleles[gfe]);

			// flag to know if the first line was a match
			boolean flag = false;

			// Run the GFE portion past the regex
			if (gfeAlleles[gfe].matches(regex)){

				// put gfe and name in holding hashmap for later printing
				dataMatches.put(gfeAlleles[gfe], gfeAlleles[1 - gfe]);

				flag = true;
			}
				
			// split the gfe along the dashes
			String[] splitGfe = gfeAlleles[gfe].split("-");
			spacingList = new int[splitGfe.length];

			int j = 0;

			// cycle through the separated GFE. If the original GFE was a match 
			// use value.length in spacingList, otherwise use 1 to populate it.
			for(String splitUpGfe:splitGfe) {

				spacingList[j] = 1;
				if (flag) { spacingList[j] = splitUpGfe.length(); }
				
				j++;
			}

			
			// parse the gfe spacing for each matching line
			while ((line = br.readLine()) != null) {

				// use comma as separator
				gfeAlleles = line.split(",");

				// Run the GFE portion through the parser
				if (gfeAlleles[gfe].matches(regex)){
					
					// put gfe and name in holding hashmap for later printing
					dataMatches.put(gfeAlleles[gfe], gfeAlleles[1 - gfe]);

					// split the gfe along the dashes
					splitGfe = gfeAlleles[gfe].split("-");
					int k = 0;

					// if a feature String is longer than the length listed 
					// in the spacing array, replace that length with the new one
					for(String splitUpGfe:splitGfe) {
						if (splitUpGfe.length() > spacingList[k]) {
							spacingList[k] = splitUpGfe.length();
						}

						k++;
					}
				}
			}

			// sort the dataMatches hashmap before printing
			SortData sorting = new SortData();
			sortedDataMatches = sorting.sortTheData(dataMatches);

			// Write the data to the appropriate text area
			int m = 0;
			for (Map.Entry me:dataMatches.entrySet()) {
				
			// for (Map.Entry me:sortedDataMatches.entrySet()) {
				// System.out.println("Key: " + me.getKey() + " & Value: " + me.getValue());
				
				// print allele name
				printToMe.append(String.format("%-25s", me.getValue()));

				// cycle through gfe's individual features, 
				// print with appropriate lengths
				int n = 0;
				splitGfe = ((String)me.getKey()).split("-");
				for (String gfeFeature:splitGfe) {
					printToMe.append(String.format("%-" + spacingList[n] + "s", gfeFeature));
					if(n < (spacingList.length - 1)) { printToMe.append("-"); }
					n++;
				}
		        printToMe.append(System.lineSeparator());

		        m++;
			}		

			// Footer
			if (m == 0){
				B12xGui.resultsTextAreaHla.append("No results found");
			} else {
				B12xGui.resultsTextAreaHla.append("Total Results: " + m);
			}

			// Close the buffer
			br.close();

			// Write contents of textbox to file
			// if(hlaWriteToFileChecked){
			//     WriteFile fileWriter = new WriteFile();
			//     fileWriter.writeToFile(locus, version, "HLA", dataType);
				
			// }
		} catch (Exception ex) {
			System.out.println(ex); 
		}
	}
}