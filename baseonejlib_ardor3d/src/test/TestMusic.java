package test;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;

import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.jlib.ardor3d.GameBase;

public class TestMusic extends GameBase {

	public static void main(final String[] args)
	{
		new TestMusic().start();
	}

	@Override
	protected void init()
	{

		try
		{
			final Audio audio = AudioLoader.getStreamingAudio("OGG", getClass().getClassLoader().getResource("assets/e9Theme3.ogg"));
			audio.playAsMusic(1, 1, false);

		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void update(final ReadOnlyTimer timer)
	{
		SoundStore.get().poll(0);
	}

}
