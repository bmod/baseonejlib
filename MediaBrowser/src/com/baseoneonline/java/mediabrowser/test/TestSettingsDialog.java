/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.baseoneonline.java.mediabrowser.test;

import javax.swing.UIManager;

import com.baseoneonline.java.mediabrowser.core.Database;
import com.baseoneonline.java.mediabrowser.gui.SettingsDialog;

/**
 * 
 * @author bmod
 */
public class TestSettingsDialog {
	public static void main(final String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SettingsDialog.get(Database.get());
	}

}
