package com.baseoneonline.java.test.testMusicOrganizer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import com.baseoneonline.java.tools.DirectoryWalker;

public class TestOrganizeMusic extends JFrame {

	JLabel label;
	
	public static void main(String[] args) {
		TestOrganizeMusic app = new TestOrganizeMusic();
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setSize(500, 400);
		app.setVisible(true);
	}

	String path = "s:/music";

	public TestOrganizeMusic() {
		Logger.getLogger("org.jaudiotagger").setLevel(Level.SEVERE);
		
		label = new JLabel("Blaat");
		add(label);
		
		getAllFiles(new File(path));
		
	}

	private void getAllFiles(final File dir) {
		final ArrayList<File> files = new ArrayList<File>();
		new SwingWorker<File, File>() {
			@Override
			protected File doInBackground() throws Exception {
				DirectoryWalker walker = new DirectoryWalker() {
					@Override
					public void visit(File f) {
						publish(f);
					};
				};
				walker.setRoot(dir);
				walker.start();
				return null;
			}
			
			@Override
			protected void process(List<File> chunks) {
				files.addAll(chunks);
				setStatus("Counting files: "+files.size());
			}
			
			@Override
			protected void done() {
				setStatus("Found "+files.size()+" files.");
			}
		}.execute();
	}
	
	private void setStatus(String status) {
		label.setText(status);
	}

}

class GetAllFilesWorker extends SwingWorker<File, File> {

	public GetAllFilesWorker() {}

	@Override
	protected File doInBackground() throws Exception {

		return null;
	}
}
