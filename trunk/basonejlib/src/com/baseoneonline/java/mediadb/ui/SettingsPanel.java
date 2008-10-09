package com.baseoneonline.java.mediadb.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.baseoneonline.java.mediadb.actions.ScanCollectionAction;
import com.baseoneonline.java.mediadb.util.Conf;

public class SettingsPanel extends JPanel implements com.baseoneonline.java.mediadb.util.Const {

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
		final JTextField tfPath = new JTextField(Conf.getStringArray(MEDIA_FOLDERS)[0]);
		tfPath.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					String[] folders = {tfPath.getText()}; 
					Conf.setStringArray(MEDIA_FOLDERS, folders);
				}
			}
		});
		updatePanelTop.add(tfPath);
		JButton btUpdate = new JButton(new ScanCollectionAction());
		updatePanelTop.add(btUpdate);
		panel.add(taStatus, BorderLayout.CENTER);
		panel.add(updatePanelTop, BorderLayout.NORTH);
		return panel;
	}
	
}
