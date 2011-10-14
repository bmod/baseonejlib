package com.baseoneonline.java.swing;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

public class SwingUtils {
	public static void setContextMenu(final Component comp,
			final JPopupMenu menu) {
		comp.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				showPopup(e);
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				showPopup(e);
			}

			private void showPopup(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					menu.show(comp, e.getX(), e.getY());
				}
			}
		});
	}
}
