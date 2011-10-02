package com.baseoneonline.java.swing;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.iharder.dnd.FileDrop;

public class FileDropListPanel extends JPanel
{

	private final DefaultListModel<File> model = new DefaultListModel<File>();
	private final JList<File> list;
	private final boolean allowDuplicates = false;

	public FileDropListPanel()
	{
		System.out.println(isFocusable());
		setLayout(new BorderLayout());
		list = new JList<File>(model);
		list.addListSelectionListener(listSelectionListener);
		add(list);

		list.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(final KeyEvent e)
			{

				switch (e.getKeyCode()) {
				case KeyEvent.VK_DELETE:
				case KeyEvent.VK_BACK_SPACE:
					removeSelected();
					break;

				default:
					break;
				}
			}
		});

		new FileDrop(this, new FileDrop.Listener()
		{

			@Override
			public void filesDropped(final File[] arg0)
			{
				for (final File f : arg0)
				{
					add(f);
				}
			}
		});

	}

	private void add(final File f)
	{
		if (!allowDuplicates)
		{
			for (int i = 0; i < model.size(); i++)
			{
				if (f.getAbsolutePath().equals(model.get(i).getAbsolutePath()))
					return;
			}
		}
		model.addElement(f);
	}

	private void removeSelected()
	{
		final int[] indices = list.getSelectedIndices();
		for (int i = indices.length - 1; i >= 0; i--)
		{
			model.remove(indices[i]);
		}
	}

	private final ListSelectionListener listSelectionListener = new ListSelectionListener()
	{
		public void valueChanged(final ListSelectionEvent e)
		{

		};
	};

}
