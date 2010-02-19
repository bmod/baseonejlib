package com.baseoneonline.java.test.edgetracing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class NewJFrame extends javax.swing.JFrame {

	private JButton btCalculate;
	private JPanel jPanel1;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				final NewJFrame inst = new NewJFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public NewJFrame() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			final GridBagLayout thisLayout = new GridBagLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1 };
			thisLayout.rowHeights = new int[] { 7, 7, 7, 7, 7 };
			thisLayout.columnWeights = new double[] { 0.1, 0.1 };
			thisLayout.columnWidths = new int[] { 7, 7 };
			getContentPane().setLayout(thisLayout);
			{
				btCalculate = new JButton();
				getContentPane().add(
						btCalculate,
						new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.NONE,
								new Insets(0, 0, 0, 0), 0, 0));
				btCalculate.setText("Calculate");
				btCalculate.addActionListener(new ActionListener() {

					public void actionPerformed(final ActionEvent evt) {
						btCalculateActionPerformed(evt);
					}
				});
			}
			{
				jPanel1 = new JPanel();
				getContentPane().add(
						jPanel1,
						new GridBagConstraints(0, 0, 1, 5, 1.0, 1.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 0), 0, 0));
			}
			pack();
			setSize(400, 300);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void btCalculateActionPerformed(final ActionEvent evt) {
		System.out.println("btCalculate.actionPerformed, event=" + evt);
		// TODO add your code for btCalculate.actionPerformed
	}

}
