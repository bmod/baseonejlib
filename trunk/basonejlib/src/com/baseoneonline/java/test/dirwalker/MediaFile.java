package com.baseoneonline.java.test.dirwalker;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagFieldKey;

public class MediaFile {

	private final File file;

	AudioFile audioFile;
	Tag tag;

	public MediaFile(File f) {
		file = f;
	}

	public String getArtist() {
		if (null != getTag()) {
			return getTag().getFirstArtist();
		}
		return "UNDEFINED";
	}

	public String getAlbum() {
		if (null != getTag()) {
			return getTag().getFirstAlbum();
		}
		return "UNDEFINED";
	}

	public String getGenre() {
		if (null != getTag()) {
			return getTag().getFirstGenre();
		}
		return "UNDEFINED";
	}

	private Tag getTag() {
		if (null == audioFile) {
			try {
				audioFile = AudioFileIO.read(file);
				tag = audioFile.getTag();
			} catch (CannotReadException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TagException e) {
				e.printStackTrace();
			} catch (ReadOnlyFileException e) {
				e.printStackTrace();
			} catch (InvalidAudioFrameException e) {
				e.printStackTrace();
			}
		}
		return tag;
	}

	public String getTitle() {
		if (null != getTag()) {
			return getTag().getFirstTitle();
		}
		return null;
	}

	public String getTrack() {
		if (null != getTag()) {
			return getTag().getFirstTrack();
		}
		return null;
	}

	public String getAlbumArtist() {
		if (null != getTag()) {
			return getTag().getFirst(TagFieldKey.ALBUM_ARTIST);
		}
		return null;
	}

}
