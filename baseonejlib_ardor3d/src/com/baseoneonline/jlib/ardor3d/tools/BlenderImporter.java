package com.baseoneonline.jlib.ardor3d.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;

public class BlenderImporter {
	private static final File BLENDER_LOCATION = new File(
			"C:/Program Files/Blender Foundation/Blender/blender.exe");
	private static final File EXPORT_SCRIPT;

	static
	{
		final String script = "exportCollada.py";
		final URL scriptFile = BlenderImporter.class.getClassLoader()
				.getResource(script);
		EXPORT_SCRIPT = new File(scriptFile.getFile());
		if (!EXPORT_SCRIPT.exists())
			throw new RuntimeException("File not found: " + script);
	}

	public static void main(final String[] args) throws IOException
	{

		new BlenderImporter().load("test/bike.blend");
	}

	public BlenderImporter() {}

	public ColladaStorage load(final String model) throws IOException
	{
		runBlender(model);
		return null;
	}

	private void runBlender(final String model) throws IOException
	{

		final File modelFile = new File(getClass().getClassLoader()
				.getResource(model).getFile());

		if (!BLENDER_LOCATION.exists())
			throw new RuntimeException("Blender not found at: "
					+ BLENDER_LOCATION.getAbsolutePath());

		final String cmd = String.format("\"%s\" -b \"%s\" -P \"%s\"",
				BLENDER_LOCATION.getAbsolutePath(),
				modelFile.getAbsolutePath(), EXPORT_SCRIPT.getAbsolutePath());

		final ProcessBuilder builder = new ProcessBuilder(cmd);
		final Process proc = builder.start();

		IOUtils.copy(proc.getInputStream(), System.out);
		IOUtils.copy(proc.getErrorStream(), System.out);

		try
		{
			proc.waitFor();
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}

	}

}
