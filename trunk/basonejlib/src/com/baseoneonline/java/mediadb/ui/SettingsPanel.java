package com.baseoneonline.java.mediadb.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.baseoneonline.java.jlib.utils.Config;
import com.baseoneonline.java.mediadb.Application;
import com.baseoneonline.java.mediadb.actions.ScanCollectionAction;

public class SettingsPanel extends JPanel {

	private JTextArea taStatus;

	public SettingsPanel() {
		createUI();
	}

	private void createUI() {
		setLayout(new BorderLayout());
		JPanel updatePanel = getUpdatePanel();
		add(updatePanel);
	}
	
	private JPanel getUpdatePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		taStatus = new JTextArea();
		//updatePanel.add(taStatus, BorderLayout.CENTER);
		JPanel updatePanelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//updatePanel.add(updatePanelTop, BorderLayout.NORTH);
		final JTextField tfPath = new JTextField(Config.getConfig().get(Constants.CFG_MEDIA_PATH, System.getProperty("user.home")));
		tfPath.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					Config.getConfig().set("mediaPath", tfPath.getText());
				}
			}
		});
		updatePanelTop.add(tfPath);
		JButton btUpdate = new JButton(new ScanCollectionAction());
		btUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.getInstance().scanCollection();
			}
		});
		updatePanelTop.add(btUpdate);
		panel.add(taStatus, BorderLayout.CENTER);
		panel.add(updatePanelTop, BorderLayout.NORTH);
		return panel;
	}
	
}
