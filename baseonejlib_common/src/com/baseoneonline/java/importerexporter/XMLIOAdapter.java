package com.baseoneonline.java.importerexporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.baseoneonline.java.nanoxml.XMLElement;

public class XMLIOAdapter extends IOAdapter
{

	@Override
	public Storable read(InputStream is) throws IOException
	{
		XMLElement xml = new XMLElement();
		xml.parseFromReader(new InputStreamReader(is));
		try
		{
			return fromXML(xml);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(OutputStream os, Storable storable)
	{
		XMLElement xml = toXML(storable);
		OutputStreamWriter writer = new OutputStreamWriter(os);
		try
		{
			xml.write(writer);
			writer.flush();
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		} finally
		{
			try
			{
				writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	private static Storable fromXML(XMLElement xml)
	{
		try
		{
			String className = xml.getName();
			@SuppressWarnings("unchecked")
			Class<Storable> type = (Class<Storable>) Class.forName(className);
			Storable stor = type.newInstance();
			stor.read(new XMLCapsule(xml));
			return stor;
		} catch (Exception e)
		{
			throw new RuntimeException();
		}
	}

	private static XMLElement toXML(Storable stor)
	{
		XMLElement xml = new XMLElement(stor.getClass().getName());
		stor.write(new XMLCapsule(xml));
		return xml;
	}

	static class XMLCapsule implements Capsule
	{

		private final XMLElement xml;

		public XMLCapsule(XMLElement xml)
		{
			this.xml = xml;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends Storable> List<T> read(String string, Class<T> type)

		{
			List<T> list = new ArrayList<>();
			for (XMLElement x : xml.getChildren(type.getName()))
			{
				list.add((T) fromXML(x));
			}
			return list;
		}

		@Override
		public String read(String key, String defaultValue)
		{
			return xml.getStringAttribute(key, defaultValue);
		}

		@Override
		public void write(String key, List<? extends Storable> list)
		{
			XMLElement xml = new XMLElement(key);
			for (Storable stor : list)
			{
				xml.addChild(toXML(stor));
			}
		}

		@Override
		public void write(String key, String value)
		{
			xml.setAttribute(key, value);
		}

		@Override
		public Storable read(String key, Storable defaultValue)
		{
			// TODO Auto-generated method stub
			throw new NotImplementedException();
		}

		@Override
		public void write(String key, Storable stor)
		{
			// TODO Auto-generated method stub
			throw new NotImplementedException();
		}

		@Override
		public boolean read(String key, boolean defaultValue)
		{
			// TODO Auto-generated method stub
			throw new NotImplementedException();
		}

		@Override
		public void write(String key, boolean value)
		{
			// TODO Auto-generated method stub
			throw new NotImplementedException();
		}
	}

}
