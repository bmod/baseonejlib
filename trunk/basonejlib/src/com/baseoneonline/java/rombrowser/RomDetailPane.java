package com.baseoneonline.java.rombrowser;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RomDetailPane extends JPanel {

	Rom rom;

	private final JLabel titleField;

	public RomDetailPane() {
		titleField = new JLabel();
		titleField.setFont(new Font("sans", Font.BOLD, 16));
		add(titleField);
		add(new JButton(new AbstractAction("Launch!") {

			@Override
			public void actionPerformed(final ActionEvent e) {
				Launcher.get().launch(rom);
			}
		}), BorderLayout.SOUTH);
	}

	public void setRom(final Rom rom) {
		this.rom = rom;
		titleField.setText(rom.getTitle());

	}

}
