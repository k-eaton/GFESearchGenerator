package org.chori.gsg.view.dropdowns;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.chori.gsg.model.*;
import org.chori.gsg.view.*;

	

public class LocusModel {

	// class instantiations
	// private WhereTheDataLives wtdl = new WhereTheDataLives();
	private final String[] fullHlaLoci = {"HLA-A", "HLA-B", "HLA-C", "HLA-DPA1", "HLA-DPB1", "HLA-DQA1", "HLA-DQB1", "HLA-DRB1", "HLA-DRB3", "HLA-DRB4", "HLA-DRB5"};

	private Preferences prefs = Preferences.userNodeForPackage(B12xGui.class);
	private InternetAccess internet = new InternetAccess();
	private LocalData localData = new LocalData();

	public LocusModel() { }

	public DefaultComboBoxModel loci(String version) {
		ArrayList<String> availableLoci = new ArrayList<>();

		// what versions are available online?
		if(onlineVersion(version)) {
			ArrayList<String> allLoci = new ArrayList<>(Arrays.asList(fullHlaLoci));
			availableLoci = allLoci;
		} else {
			// figure out what datafiles are available for selected version
			availableLoci = localData.getLocalDataFiles(version);
		}

		DefaultComboBoxModel model = new DefaultComboBoxModel();

		// return a model available for them
		model = new DefaultComboBoxModel(availableLoci.toArray());
		return model;
	}

	private Boolean onlineVersion(String version) {

		String onlineVersions = prefs.get("GSG_HLA_VERSIONS", null);
		String [] parsedOnlineVersions = new String[3];

		if(onlineVersions != null)
			parsedOnlineVersions = onlineVersions.split(", ");

		// if the version matches a version available online
		try {
			for(int i = 0; i < parsedOnlineVersions.length; i++)
				if (parsedOnlineVersions[i].compareTo(version) == 0)
					return true;

		} catch (Exception ex) { return false; }

		return false;
	}

}


// 	public ArrayList<String> getLocalDataFiles(String whichTab) {
// 		ArrayList<String> availableLoci = new ArrayList<>();
		
// 		// find the BSGData folder
// 		String rawDataPath = wtdl.getRawDataPath();
// 		String version = "";

// 		// get which version
// 		switch(whichTab) {
// 			case "HLA":
// 				version = B12xGui.whatVersionHla.getSelectedItem().toString();
// 				System.out.println("Locus Model selected version (HLA): " + version);
// 				rawDataPath = rawDataPath + version;
// 				System.out.println("Locus Model file path (HLA): " + rawDataPath);
// 				break;
// 			default:
// 				System.out.println("LocusModel: haven't set up that version reader yet");
// 		}


// 		// read the BSGData folder
// 		File[] files = new File(rawDataPath).listFiles();
// 		// System.out.println(Arrays.toString(files));
		
// 		// // get the folders for various versions
// 		int pathLength = (rawDataPath.length() + 7);
// 		// System.out.println("Path length: " + pathLength);

// 		int filePathLength;
// 		String file;
// 		String protoLocus;
// 		String locus = "";
		
// 		for (File aFile:files) {
// 			// get file path length
// 			filePathLength = aFile.toString().length();
// 			file = aFile.toString();
			
// 			// get locus out of file name
// 			protoLocus = file.substring(pathLength, filePathLength);
// 			// System.out.println("subpath: " + protoLocus);

// 			// get file suffix, if csv, extract locus name
// 			String fileSuffix = protoLocus.substring(protoLocus.length() - 3);
// 			if (fileSuffix.compareTo("csv") == 0) {
// 				locus = protoLocus.substring(0, protoLocus.length() - 20);
// 				System.out.println("locus name: " + locus);
// 			}

// 			// check file length, is it more than just a header line?
// 			if (fileLength(locus, version))
// 				availableLoci.add(locus);
// 		}

// 		// sort the loci
// 		Collections.sort(availableLoci);

// 		System.out.println(availableLoci.toString());

// 		return availableLoci;
// 	}

// 	// sometimes files have nothing but a header in them
// 	// makes sure the file is big enough to actually contain data
// 	private boolean fileLength(String whatLocus, String whatVersion) {
// 		//
// 		File data = rawData.getRawData(whatLocus, whatVersion);
// 		if (data != null) {
// 			long fileLength = data.length();
// 			System.out.println("File length of " + whatVersion 
// 				+ ", " + whatLocus + ": " + fileLength);
		
// 			// check that file is longer than just a header
// 			if(data.length() > 100)
// 				return true;
// 		}

// 		return false;
// 	}
// }