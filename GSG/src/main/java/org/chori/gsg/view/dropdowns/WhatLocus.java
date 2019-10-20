package org.chori.gsg.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.chori.gsg.view.searchboxes.*;
import org.chori.gsg.view.*;

public class WhatLocus { 

	HlaSearchBoxGenerator hlaSBG = new HlaSearchBoxGenerator();
	private final String[] fullHlaLoci = {"HLA-A", "HLA-B", "HLA-C", "HLA-DPA1", "HLA-DPB1", "HLA-DQA1", "HLA-DQB1", "HLA-DRB1", "HLA-DRB3", "HLA-DRB4", "HLA-DRB5"};
	private Preferences prefs = Preferences.userNodeForPackage(B12xGui.class);
	private String whichLocus = "HLA-A";

	public WhatLocus() {

	}

	public JComboBox createWhatLocusComboBox(String whichComboBox) {
		System.out.println("Generating the which locus combo box");
		
		// instantiate combobox and its model
		JComboBox whatLocus = new JComboBox();
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		// JPanel newlayout = new JPanel();

		// who is this combobox for?
		switch(whichComboBox) {
			case "HLA":
				comboBoxModel = new DefaultComboBoxModel(fullHlaLoci);
				whatLocus.setModel(comboBoxModel);
				whatLocus.setSelectedIndex(prefs.getInt("GSG_HLA_LOCUS", 0));
				hlaListener(whatLocus);
				break;
			case "NAME1":
				comboBoxModel = new DefaultComboBoxModel(fullHlaLoci);
				whatLocus.setModel(comboBoxModel);
				whatLocus.setSelectedIndex(prefs.getInt("GSG_NAME_LOCUS_1", 0));
				// whatLocus.setName("HLA-dropdown");
				nameListener(whatLocus);
				break;
			default:
				System.out.println("Locus: Haven't set up that combobox model yet");
		}

    	// whatLocus.setModel(comboBoxModel);

		return whatLocus;
	}

	private void hlaListener(JComboBox hlaWhatLocus) {
		hlaWhatLocus.addActionListener(new ActionListener() {
    		@Override
            public void actionPerformed(ActionEvent evt) {
            	whichLocus = hlaWhatLocus.getSelectedItem().toString();
				JPanel newGfePanel = hlaSBG.generateHlaPanel(whichLocus);
            	newGfePanel.setName("HLA-GFE");
            	JPanel oldPanel = findPanel(B12xGui.hlaPanel, "HLA-GFE");
            	B12xGui.hlaPanel.remove(oldPanel);
            	B12xGui.hlaPanel.add(newGfePanel).revalidate();
            	B12xGui.hlaPanel.repaint();
                System.out.println("Which Loci listener triggered");
            	prefs.putInt("GSG_HLA_LOCUS", hlaWhatLocus.getSelectedIndex());
            	prefs.put("GSG_HLA_LOCUS_STRING", whichLocus);
            }
        });
	}

	private void nameListener(JComboBox nameWhatLocus) {
		nameWhatLocus.addActionListener(new ActionListener() {
    		@Override
            public void actionPerformed(ActionEvent evt) {
            	whichLocus = nameWhatLocus.getSelectedItem().toString();
                System.out.println("Which Loci listener triggered");
            	prefs.putInt("GSG_NAME_LOCUS_1", nameWhatLocus.getSelectedIndex());
            	prefs.put("GSG_NAME_LOCUS_STRING_1", whichLocus);
            }
        });
	}

	private JPanel findPanel(JPanel whichTab, String whichPanel) {
		Component selectedPanel = B12xGui.hlaPanel;
		for (Component component : whichTab.getComponents()) {
            if (component.getName().equals(whichPanel)){
                selectedPanel = component;
                System.out.println("panel: " + selectedPanel);
            } 
        }
        return (JPanel)selectedPanel;
	}
}