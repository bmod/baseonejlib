package com.baseoneonline.java.media.test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.baseoneonline.java.swing.DockingFrame;
import com.baseoneonline.java.swing.logview.LogView;
import com.baseoneonline.java.tools.FileExensionFilter;
import com.baseoneonline.java.tools.StringUtils;

public class TestMedia extends DockingFrame {

	public static void main(String[] args) {

		Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING);
		new TestMedia(TestMedia.class);
	}

	public TestMedia(Class<?> id) {
		
	}

	@Override
	protected void initFrame() {
		JPanel panTools = new JPanel(new FlowLayout(FlowLayout.LEFT));
		{
			panTools.add(new JButton(new AbstractAction("List All") {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					listAllFiles();
				}
			}));
		}
		addDockable(panTools, "Tools");
		addDockable(new LogView(), "Log");
//		addDockable(new LogView(), "Log2");
	}
	
	
	private void listAllFiles() {
		
	}
	

	private void moveFiles() {

		File dir = new File("N:/music/Artists");
		final String tpl_main =
			"N:/music/Artists/<albumartist>/<album>/"
				+ "<artist> - <album> (<track>) <title>.mp3";

		final String tpl_compilation =
			"N:/music/Compilations/<albumartist>/<album>/"
				+ "<artist> - <album> (<track>) <title>.mp3";

		FileOperation op = new FileOperation() {
			@Override
			public void operate(File f) {
				MediaFile mfile = new MediaFile(f);
				File newFile = new File(constructFileName(mfile, tpl_main));
				if (!newFile.exists()) {
					System.out.println(newFile);
					// moveFile(f, newFile);
				}
			}
		};

		String[] ext = { "mp3" };

		DirectoryWalker walker = new DirectoryWalker(op);

		walker.setFileFilter(new FileExensionFilter(ext));
		walker.walk(dir);

	}

	private String constructFileName(MediaFile f, String template) {
		String s = replace(template, "<artist>", f.getArtist());
		s = replace(s, "<albumartist>", f.getAlbumArtist());
		s = replace(s, "<album>", f.getAlbum());
		s = replace(s, "<title>", f.getTitle());

		String track = StringUtils.padFront(f.getTrack(), 3, "0");
		track = track.replace("\\", "-");
		track = track.replace("/", "-");
		s = s.replace("<track>", track);

		return s;
	}

	private String replace(String src, String pat, String rep) {
		return src.replace(pat, rep.replaceAll(
			"\\\\|/|\\?|%|\\*|:|\\||\"|<|>|\\.|!|'|", ""));
	}

	private void moveFile(File source, File target) {
		target.getParentFile().mkdirs();
		source.renameTo(target);
	}
}
