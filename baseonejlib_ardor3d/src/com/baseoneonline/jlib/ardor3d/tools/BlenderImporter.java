package com.baseoneonline.jlib.ardor3d.tools;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.ardor3d.extension.model.collada.jdom.data.ColladaStorage;

public class BlenderImporter {
	private static final File BLENDER_LOCATION = new File(
			"C:/Program Files/Blender Foundation/Blender/blender.exe");

	public static void main(String[] args) throws IOException
	{
		new BlenderImporter().load("test/bike.blend");
	}

	public BlenderImporter() {}

	public ColladaStorage load(String model) throws IOException
	{
		runBlender(model);
		return null;
	}

	private File resFile(String res)
	{
		URL url = getClass().getClassLoader().getResource(res);
		if (null == url)
			throw new RuntimeException("Resource not found in classpath: "
					+ res);
		return new File(url.getFile());
	}

	private void runBlender(String model) throws IOException
	{
		File scriptFile = resFile("exportCollada.py");
		File modelFile = resFile(model);

		if (!BLENDER_LOCATION.exists())
			throw new RuntimeException("Blender not found at: "
					+ BLENDER_LOCATION.getAbsolutePath());

		if (!scriptFile.exists())
			throw new RuntimeException("Script file not found: "
					+ scriptFile.getAbsolutePath());

		String cmd = String.format("\"%s\" -b \"%s\" -P \"%s\"",
				BLENDER_LOCATION.getAbsolutePath(),
				modelFile.getAbsolutePath(), scriptFile.getAbsolutePath());

		ProcessBuilder builder = new ProcessBuilder(cmd);
		Process proc = builder.start();

		IOUtils.copy(proc.getInputStream(), System.out);
		IOUtils.copy(proc.getErrorStream(), System.out);

		try
		{
			proc.waitFor();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}
}
