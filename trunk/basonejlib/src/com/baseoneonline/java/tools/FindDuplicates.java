package com.baseoneonline.java.tools;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class FindDuplicates extends JFrame {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final FindDuplicates app = new FindDuplicates();
		app.setSize(600,400);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	public FindDuplicates() {
		createUI();
	}
	
	
	
	private JTextArea taOutput;
	private JTextField tfFolder;
	
	private void createUI() {
		taOutput = new JTextArea();
		final JScrollPane spOutput = new JScrollPane(taOutput);
		add(spOutput);
		
		final JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		tfFolder = new JTextField();
		tfFolder.setText("f:/music");
		northPanel.add(tfFolder);
		
		final JButton btScan = new JButton("Scan");
		btScan.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				scan(new File(tfFolder.getText()));
			}
		});
		northPanel.add(btScan, BorderLayout.EAST);
		
		add(northPanel, BorderLayout.NORTH);
		
	}
	
	private int fileCount = 0;
	
	private void scan(final File f) {
		output("Done.");
	}
	
	
	private void output(final String msg) {
		taOutput.append(msg+"\n");
	}
	
	private class ScanWorker<String, Void> extends SwingWorker<String, Void> {

		private final File directory;
		
		public ScanWorker(final File dir) {
			output("Starting scan on "+dir.getAbsolutePath());
			directory = dir;
		}
			
		@Override
		protected String doInBackground() throws Exception {
			
			countFiles(directory);
			output("Files found: "+fileCount);
			return null;
		}
		
		private void countFiles(final File dir) {
			for (final File f : dir.listFiles()) {
				if (f.isFile()) {
					fileCount++;
				} else if (f.isDirectory()) {
					countFiles(f);
				}
			}
		}
		
		@Override
		protected void done() {
			output("Done");
		}
		
	}
	
}
