package com.baseoneonline.java.importerexporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class IOAdapter
{

	public Storable read(File file) throws IOException
	{
		FileInputStream fin = new FileInputStream(file);
		Storable stor = read(fin);
		fin.close();
		return stor;
	}

	public void write(File file, Storable storable) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		write(fos, storable);
		fos.flush();
		fos.close();
	}

	public abstract Storable read(InputStream is) throws IOException;

	public abstract void write(OutputStream os, Storable storable);
}
