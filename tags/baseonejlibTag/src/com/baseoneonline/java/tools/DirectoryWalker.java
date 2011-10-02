package com.baseoneonline.java.tools;

import java.io.File;
import java.io.FileFilter;

public abstract class DirectoryWalker
{

	public enum Mode
	{
		FilesOnly, DirsOnly, FilesAndDirs
	}

	private File root;
	private Mode visitMode = Mode.FilesOnly;
	private boolean recursive = true;
	private FileFilter fileFilter;
	private FileFilter filter;

	public DirectoryWalker(final File root)
	{
		setRoot(root);
	}

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

	public File getRoot()
	{
		return root;
	}

	public void setRoot(final File root)
	{
		this.root = root;
	}

	public void start() throws Exception
	{
		if (null == root)
			throw new NullPointerException("Root must be set first.");
		walk(root);
	}

	public abstract void visit(File f) throws Exception;

	private void walk(final File dir) throws Exception
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
