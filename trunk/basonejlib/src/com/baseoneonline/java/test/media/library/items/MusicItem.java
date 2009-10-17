package com.baseoneonline.java.test.media.library.items;

import java.io.File;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;

public class MusicItem extends MediaItem {

	public MusicItem(final File f) {
		super(f);

	}

	@Override
	public void readMetadata() {
		try {
			final Tag tag = AudioFileIO.read(file.value).getTag();
			mtime.value = file.value.lastModified();
			artist.value = tag.getFirstArtist();
			album.value = tag.getFirstAlbum();
			title.value = tag.getFirstTitle();
			if (tag.getArtworkList().size() > 0) {
				image.value = tag.getArtworkList().get(0).getImage();
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getDisplayName() {
		return artist + " | " + album + " | " + title;
	}

}
