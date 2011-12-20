package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

public class FileExtensionFilter extends javax.swing.filechooser.FileFilter
		implements FileFilter, Serializable
{

	private boolean includeDirectories = true;
	String[] extensions;
	String description = "Files";

	public FileExtensionFilter(final String[] ext, final String description)
	{
		this(ext);
		this.description = description;
	}

	public FileExtensionFilter(final String[] ext)
	{
		extensions = new String[ext.length];
		for (int i = 0; i < ext.length; i++)
		{
			final String x = ext[i].trim();
			if (!x.startsWith("."))
			{
				extensions[i] = "." + x.trim();
			} else
			{
				extensions[i] = x.trim();
			}
		}
	}

	public FileExtensionFilter(final String extensionsString)
	{
		this(extensionsString, null);
	}

	public FileExtensionFilter(final String extensionsString,
			final boolean includeDirs)
	{
		this(extensionsString);
		includeDirectories = includeDirs;
	}

	public FileExtensionFilter(final String extensionsString,
			final String description)
	{
		extensions = extensionsString.split("[,| |;]");

		this.description = description;
	}

	@Override
	public boolean accept(final File f)
	{
		if (f.isDirectory() && includeDirectories)
			return true;
		for (final String ext : extensions)
		{
			if (f.getName().toLowerCase().endsWith(ext))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription()
	{
		final String ext = "(" + StringUtils.join(extensions, ", ") + ")";
		if (null == description)
			return ext;
		return description + " " + ext;
	}
}
