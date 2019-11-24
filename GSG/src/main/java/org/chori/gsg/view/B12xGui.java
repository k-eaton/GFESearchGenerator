package org.chori.gsg.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.prefs.Preferences;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.chori.gsg.model.*;
import org.chori.gsg.view.searchboxes.*;
import org.chori.gsg.view.dropdowns.*;
import org.chori.gsg.view.buttons.*;

public class B12xGui extends JFrame {

	private Preferences prefs = Preferences.userNodeForPackage(this.getClass());

	// default locus settings
	private String hlaSelectedLocus = prefs.get("GSG_HLA_LOCUS_STRING", "HLA-A");
	private String nameSelectedLocus = prefs.get("GSG_NAME_VERSION_1", "HLA-A");
	private String kirSelectedLocus = prefs.get("GSG_KIR_LOCUS_STRING", "KIR2DL4");

	// the panel generators
	private HlaSearchBoxAssembler hlaPanelGenerator = new HlaSearchBoxAssembler();
	
	// component generators
	private static WhatLocus whatLocusGenerator = new WhatLocus();
	private static WhatVersion whatVersionGenerator = new WhatVersion();
	private static ResetButton resetButtonGenerator = new ResetButton();
	private static FileFormatPanel fileFormatPanelGenerator = new FileFormatPanel();
	private static CancelButton cancelButtonGenerator = new CancelButton();
	private static SubmitButton submitButtonGenerator = new SubmitButton();
	private static ResetPrefsButton resetPrefsButtonGenerator = new ResetPrefsButton();
	private static BulkDownloadButton bulkDownloadButtonGenerator = new BulkDownloadButton();

	// need this to add at initialization
	public static JTabbedPane parentTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

	// the tabs, added initially so I can make them 
	// public, static and update them
	public static JPanel hlaGfeTab = new JPanel();
	public static JPanel kirGfeTab = new JPanel();
	public static JPanel nameGfeTab = new JPanel();
	public static JPanel featureGfeTab = new JPanel();
	public static JPanel optionsGfeTab = new JPanel();

	// the holder panels
	// they're embedded in the layout, with contents to be changed
	public static JPanel hlaPanel = new JPanel();
	public static JPanel namePanel = new JPanel();

	// results text areas
	public static JTextArea resultsTextAreaHla = new JTextArea();
	public static JTextArea resultsTextAreaName = new JTextArea();
	public static JTextArea resultsTextAreaFeature = new JTextArea();
	
	// combo boxes for locus and version selection
	public static JComboBox whatVersionHla = new JComboBox();
	public static JComboBox whatLocusHla = new JComboBox();
	public static JComboBox whatVersionName = new JComboBox();
	public static JComboBox whatLocusName = new JComboBox();
	public static JComboBox whatVersionFeature = new JComboBox();
	public static JComboBox whatLocus1Feature = new JComboBox();
	public static JComboBox whatLocus2Feature = new JComboBox();
	public static JComboBox whatVersionBulk = new JComboBox();

	
	// file format panels
	public static JPanel fileFormatHla = fileFormatPanelGenerator.createFileFormatPanel("HLA");
	public static JPanel fileFormatName = fileFormatPanelGenerator.createFileFormatPanel("NAME");
	public static JPanel fileFormatFeature = fileFormatPanelGenerator.createFileFormatPanel("FEATURE");

	// search box for name search
	public static JTextField nameSearchBox = new JTextField("", 20);
	
	public B12xGui() {
		try {

			// error handling: if you have no data and no internet
			// the program doesn't work
			InternetAccess internet = new InternetAccess();
			LocalData localData = new LocalData();

			if(!internet.tester() && localData.localVersionData() == null) {
				throw new NoInternetOrDataException();
			}

			whatVersionHla = whatVersionGenerator.createWhatVersionComboBox("HLA");
			whatLocusHla = whatLocusGenerator.createWhatLocusComboBox("HLA", whatVersionHla.getSelectedItem().toString());
			whatVersionName = whatVersionGenerator.createWhatVersionComboBox("NAME");
			whatLocusName = whatLocusGenerator.createWhatLocusComboBox("NAME", whatVersionName.getSelectedItem().toString());
			whatVersionBulk = whatVersionGenerator.createWhatVersionComboBox("BULK");

		} catch (NoInternetOrDataException ex) { 
			System.exit(0);
		} catch (Exception ex) {
			System.out.println("B12xGui initialization: " + ex);
		}


		// jFrame settings
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(1060,740));
		this.add(parentTabbedPane);
		this.pack();
		this.setLocationRelativeTo(null);

		initComponents();
	}

	private void initComponents() {

	/* tabbed pane */
		parentTabbedPane.setPreferredSize(new Dimension(1000, 700));

	/* HLA GFE tab */

		// add panel to tab pane
		parentTabbedPane.addTab("HLA GFE Search", null, hlaGfeTab, "HLA GFE Search tool");
		
		// generate the HLA GFE panel
		try {
			System.out.println("Generating the initial hlaPanel using whatLocusHla: " + whatLocusHla.getSelectedItem().toString());
			JPanel currentHlaPanel = hlaPanelGenerator.assembleHlaPanel(whatLocusHla.getSelectedItem().toString());
			currentHlaPanel.setName("HLA-GFE");
			hlaPanel.add(currentHlaPanel);
		} catch (IllegalArgumentException iex) {
			PrefProbException ppex = new PrefProbException();
		}
		// results textarea
		JScrollPane resultsScrollPaneHla = new JScrollPane(resultsTextAreaHla);
		resultsTextAreaHla.setFont(new Font("Courier New", 0, 13));
		resultsScrollPaneHla.setPreferredSize(new Dimension(950, 300));
		// resultsTextAreaHla.setEditable(false);

		// labels
		JLabel selectAllLabelHla = new JLabel("Check all");
		JTextArea usageInstructionsHla = new JTextArea("Enter in the terms you are looking for. (Zero represents unsequenced data, and is a valid term.) Empty boxes function as wildcards."
													+ "\nChecking a box will prevent any results containing the number zero (an unsequenced feature) in that feature.");
		usageInstructionsHla.setBackground(hlaPanel.getBackground());
		usageInstructionsHla.setEditable(false);
		usageInstructionsHla.setFocusable(false);

		// buttons
		JButton resetButtonHla = resetButtonGenerator.createResetButton("HLA");
		JButton submitButtonHla = submitButtonGenerator.createSubmitButton("HLA");
		JButton cancelButtonHla = cancelButtonGenerator.createCancelButton();

		// submit/cancel buttons panel
		JPanel bottomButtonsHla = new JPanel();
		bottomButtonsHla.add(submitButtonHla);
		bottomButtonsHla.add(cancelButtonHla);

		// layout / add them to the hlaGfeTab
		hlaGfeTab.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0,0,10,0);
		c.weightx = 0.5;
		
		// line 0
		c.gridx = 0;
		c.gridy = 0;
		hlaGfeTab.add(whatLocusHla, c);
		
		c.gridx = 1;
		hlaGfeTab.add(usageInstructionsHla, c);

		// line 1
		c.insets = new Insets(0,0,0,0);
		c.gridx = 0;
		c.gridy = 1;
		hlaGfeTab.add(selectAllLabelHla, c);

		// line 2
		c.gridy = 2;
		c.gridwidth = 4;
		hlaGfeTab.add(hlaPanel, c);

		// line 3
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1;
		c.gridy = 3;
		hlaGfeTab.add(resetButtonHla, c);

		c.gridx = 1;
		hlaGfeTab.add(whatVersionHla, c);

		// line 4
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 4;
		hlaGfeTab.add(fileFormatHla, c);

		// line 5
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridy = 5;
		hlaGfeTab.add(resultsScrollPaneHla, c);

		// line 6
		c.weightx = 0;
		c.weighty = 0;
		c.gridy = 6;
		hlaGfeTab.add(bottomButtonsHla, c);

	/* KIR GFE tab */
		// parentTabbedPane.addTab("KIR GFE Search", null, kirGfeTab, "KIR GFE Search tool");

	/* Name Search tab */
		parentTabbedPane.addTab("Name Search", null, nameGfeTab, "HLA Name Search tool");
		
		// currentHlaPanel.setName("NAME");
		namePanel.add(nameSearchBox);
		nameSearchBox.addActionListener(submitButtonGenerator.nameListener);
		
		// results textarea
		JScrollPane resultsScrollPaneName = new JScrollPane(resultsTextAreaName);
		resultsTextAreaName.setFont(new Font("Courier New", 0, 13));
		resultsScrollPaneName.setPreferredSize(new Dimension(950, 300));
		// resultsTextAreaHla.setEditable(false);

		// buttons
		JButton resetButtonName = resetButtonGenerator.createResetButton("NAME");
		JButton submitButtonName = submitButtonGenerator.createSubmitButton("NAME");
		JButton cancelButtonName = cancelButtonGenerator.createCancelButton();

		// submit/cancel buttons panel
		JPanel bottomButtonsName = new JPanel();
		bottomButtonsName.add(submitButtonName);
		bottomButtonsName.add(cancelButtonName);

		// layout / add them to the hlaGfeTab
		nameGfeTab.setLayout(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.anchor = GridBagConstraints.NORTHWEST;
		e.insets = new Insets(0,0,10,0);
		e.weightx = 0.5;
		
		// line 0
		e.gridx = 0;
		e.gridy = 0;
		nameGfeTab.add(whatLocusName, e);
		
		// e.gridx = 1;
		// nameGfeTab.add(usageInstructions, e);

		// line 1
		// e.insets = new Insets(0,0,0,0);
		// e.gridx = 0;
		// e.gridy = 1;
		// nameGfeTab.add(selectAllLabel, e);

		// line 2
		e.gridy = 2;
		e.gridwidth = 4;
		nameGfeTab.add(namePanel, e);

		// line 3
		e.anchor = GridBagConstraints.WEST;
		e.gridwidth = 1;
		e.gridy = 3;
		// nameGfeTab.add(resetButtonName, e);

		// e.gridx = 1;
		nameGfeTab.add(whatVersionName, e);

		// line 4
		e.anchor = GridBagConstraints.CENTER;
		e.gridwidth = 4;
		e.gridx = 0;
		e.gridy = 4;
		nameGfeTab.add(fileFormatName, e);

		// line 5
		e.anchor = GridBagConstraints.NORTH;
		e.weightx = 1;
		e.weighty = 1;
		e.gridy = 5;
		nameGfeTab.add(resultsScrollPaneName, e);

		// line 6
		e.weightx = 0;
		e.weighty = 0;
		e.gridy = 6;
		nameGfeTab.add(bottomButtonsName, e);

	/* Features tab */
		parentTabbedPane.addTab("Feature Comparison", null, featureGfeTab, "Feature Comparison tool");

		// results textarea
		JScrollPane resultsScrollPaneFeature = new JScrollPane(resultsTextAreaFeature);
		resultsTextAreaFeature.setFont(new Font("Courier New", 0, 13));
		resultsScrollPaneFeature.setPreferredSize(new Dimension(950, 300));

	/* Options tab */
		parentTabbedPane.addTab("Options", null, optionsGfeTab, "Options");

		// local data only option/can java ping for internet access?

		// buttons
		JButton bulkDownloadButton = bulkDownloadButtonGenerator.createBulkDownloadButton();
		JButton resetPrefsButton = resetPrefsButtonGenerator.createResetPrefsButton();
		JButton cancelButtonOptions = cancelButtonGenerator.createCancelButton();

		// layout / add them to the hlaGfeTab
		optionsGfeTab.setLayout(new GridBagLayout());
		GridBagConstraints f = new GridBagConstraints();
		f.anchor = GridBagConstraints.NORTHWEST;
		f.insets = new Insets(0,0,10,0);
		f.weightx = 0.5;
		
		// line 0
		f.gridx = 0;
		f.gridy = 0;
		optionsGfeTab.add(bulkDownloadButton, f);

		f.gridx = 1;
		optionsGfeTab.add(whatVersionBulk, f);

		// line 1
		f.gridx = 0;
		f.gridy = 1;
		optionsGfeTab.add(resetPrefsButton, f);


		// line 6
		f.weightx = 0;
		f.weighty = 0;
		f.gridy = 6;
		optionsGfeTab.add(cancelButtonOptions, f);


	/* Get and set open tab */
		try {
			parentTabbedPane.setSelectedIndex(prefs.getInt("GSG_OPEN_TAB", 0));
		} catch (IllegalArgumentException iex) { 
			System.out.println("GUI set selected Index error: " + iex); 
			PrefProbException ppex = new PrefProbException();
		}


		parentTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				prefs.putInt("GSG_OPEN_TAB", parentTabbedPane.getSelectedIndex());
			}
		});

	}

	public static void main(String args[]) {


  //   	try {
		// 	for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		// 		if ("Nimbus".equals(info.getName())) {
		// 			javax.swing.UIManager.setLookAndFeel(info.getClassName());
		// 			break;
		// 		}
		// 	}
		// } catch (Exception ex) { System.out.println(ex); }
		
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new B12xGui().setVisible(true);
				
				InternetAccess internet = new InternetAccess();

				if(!internet.tester()) {
					resultsTextAreaHla.append("No internet access available, local data only");
					resultsTextAreaName.append("No internet access available, local data only");
				}
			}
		});
	}
}
