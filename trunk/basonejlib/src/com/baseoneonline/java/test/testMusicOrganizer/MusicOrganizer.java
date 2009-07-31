package com.baseoneonline.java.test.testMusicOrganizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.TagFieldKey;

import com.baseoneonline.java.tools.DirectoryWalker;
import com.baseoneonline.java.tools.DirectoryWalker.Mode;

public class MusicOrganizer {

	SwingWorker<Void, File> worker;
	private static MusicOrganizer instance;
	
	private File rootPath;

	private MusicOrganizer() {

	}

	public static MusicOrganizer get() {
		if (null == instance)
			instance = new MusicOrganizer();
		return instance;
	}
	
	public void setRootPath(File rootPath) {
		this.rootPath = rootPath;
	}

	public File getRootPath() {
		return rootPath;
	}
	
	public void organize() {
		worker = new SwingWorker<Void, File>() {
			@Override
			protected Void doInBackground() throws Exception {
				
				List<File> files = getAllFiles();
				
				// Process
				for (File f : files ) {
					processFile(f);
				}
				

				return null;
			}
		};
		worker.execute();
	}

	public List<File> getAllFiles() {
		// Gather
		final ArrayList<File> files = new ArrayList<File>();
		DirectoryWalker walker = new DirectoryWalker(rootPath) {
			@Override
			public void visit(File f) {
				files.add(f);
			}
		};
		walker.setFileFilter(new AudioFileFilter());
		walker.setVisitMode(Mode.FilesOnly);
		walker.start();
		return files;
	}
	
	private SortRule getDefaultSortRule() {
		return getSortRules().get(0);
	}

	private void processFile(File infile) {
		AudioFile audioFile = null;
		try {
			audioFile = AudioFileIO.read(infile);
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).warning(
				"Error reading: " + infile);
		}

		if (null != audioFile) {
			SortRule rule = findSortRule(audioFile);
			sortFile(audioFile, rule);
		}
	}

	private SortRule findSortRule(AudioFile file) {
		String genre = file.getTag().getFirstGenre();

		for (SortRule rule : getSortRules()) {
			if (genre.toLowerCase().equals(rule.genre)) {
				return rule;
			}
		}
		return getDefaultSortRule();
	}

	private void sortFile(AudioFile file, SortRule rule) {
		String newFilename = getFilename(file, rule);
		// newFilename = FileUtils.removeIllegalChars(newFilename);
		File newFile = new File(newFilename);
		if (!newFile.exists()) {

		}

		System.out.println("old: " + file.getFile().toString());
		System.out.println("new: " + newFile.toString());
		System.out.println();
	}

	private String getFilename(AudioFile file, SortRule tpl) {
		String temp = tpl.filenameTemplate;
		for (TagFieldKey key : getTagMap().keySet()) {
			String val = file.getTag().getFirst(key);
			if (val.length() == 0 && key == TagFieldKey.ALBUM_ARTIST) {
				val = file.getTag().getFirst(TagFieldKey.ARTIST);
			}
			String tag = getTagMap().get(key);
			try {
				temp = temp.replaceAll(tag, val);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(getClass().getName()).warning(
					e.getMessage() + " in \"" + temp + "\"");
			}

		}
		StringBuffer buf = new StringBuffer();
		buf.append(temp);
		buf.append(".");
		buf.append(file.getAudioHeader().getEncodingType());
		return buf.toString();
	}

	private HashMap<TagFieldKey, String> tagMap;

	private HashMap<TagFieldKey, String> getTagMap() {
		if (null == tagMap) {
			tagMap = new HashMap<TagFieldKey, String>();
			tagMap.put(TagFieldKey.ALBUM_ARTIST, "<albumartist>");
			tagMap.put(TagFieldKey.ALBUM, "<album>");
			tagMap.put(TagFieldKey.ARTIST, "<artist>");
			tagMap.put(TagFieldKey.COMPOSER, "<composer>");
			tagMap.put(TagFieldKey.TITLE, "<title>");
			tagMap.put(TagFieldKey.TRACK, "<track>");
			tagMap.put(TagFieldKey.YEAR, "<year>");
		}
		return tagMap;
	}


	private List<SortRule> sortRules;

	private List<SortRule> getSortRules() {
		if (null == sortRules) {
			sortRules = new ArrayList<SortRule>();
			SortRule t;

			// DEFAULT SORT RULE
			t = new SortRule();
			t.filenameTemplate =
				"s:/music/Artists/<albumartist>/<album>/<artist> - <album> (<track>) <title>";
			sortRules.add(t);

			t = new SortRule();
			t.genre = "game";
			t.filenameTemplate =
				"s:/music/Game/<album>/<album> (<track>) <artist> - <title>";
			sortRules.add(t);

			t = new SortRule();
			t.genre = "film";
			t.filenameTemplate =
				"s:/music/Film/<album>/<album> (<track>) <artist> - <title>";
			sortRules.add(t);

			t = new SortRule();
			t.genre = "classical";
			t.filenameTemplate =
				"s:/music/Classical/<composer>/<composer> - <album> (<track>) <title>";
			sortRules.add(t);

			t = new SortRule();
			t.filenameTemplate =
				"s:/music/Compilations/<album>/<album> (<track>) <artist> - <title>";
			sortRules.add(t);

		}
		return sortRules;
	}

}
