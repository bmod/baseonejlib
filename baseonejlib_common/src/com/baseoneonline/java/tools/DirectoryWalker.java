package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;

public abstract class DirectoryWalker
{

	public enum Mode
	{
		FilesOnly, DirsOnly, FilesAndDirs
	}

	private Mode visitMode = Mode.FilesOnly;
	private boolean recursive = true;
	private FileFilter fileFilter;
	private FileFilter filter;

	public DirectoryWalker()
	{
	}

	public FileFilter getFileFilter()
	{
		return fileFilter;
	}

	public void setFileFilter(final FileFilter fileFilter)
	{
		this.fileFilter = fileFilter;
	}

	public Mode getVisitMode()
	{
		return visitMode;
	}

	public void setVisitMode(final Mode visitMode)
	{
		this.visitMode = visitMode;
	}

	public boolean isRecursive()
	{
		return recursive;
	}

	public void setRecursive(final boolean recursive)
	{
		this.recursive = recursive;
	}

	public abstract void visit(File f) throws Exception;

	public void walk(final File dir) throws Exception
	{
		final File[] files = dir.listFiles(getFilter());
		for (final File f : files)
		{
			if (!f.getName().startsWith("."))
			{
				if (visitMode == Mode.DirsOnly && f.isDirectory())
				{
					visit(f);
				} else if (visitMode == Mode.FilesOnly && !f.isDirectory())
				{
					visit(f);
				}
				if (recursive && f.isDirectory())
				{
					walk(f);
				}
			}
		}
	}

	private FileFilter getFilter()
	{
		if (null == filter)
		{
			filter = new FileFilter()
			{
				@Override
				public boolean accept(final File f)
				{
					if (f.isDirectory())
						return true;
					if (null != fileFilter)
						return fileFilter.accept(f);
					return true;
				}
			};
		}
		return filter;
	}

}
