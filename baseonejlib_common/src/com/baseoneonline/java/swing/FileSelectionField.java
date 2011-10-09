package com.baseoneonline.java.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;

public class FileSelectionField extends JPanel
{
	private JTextField textField;

	private int fileSelectionMode;

	/**
	 * Create the panel.
	 */
	public FileSelectionField()
	{

		initComponents();
	}

	/**
	 * @param fileSelectionMode
	 *            {@link JFileChooser} selection mode.
	 * @see JFileChooser#setFileSelectionMode(int)
	 */
	public void setFileSelectionMode(final int fileSelectionMode)
	{
		this.fileSelectionMode = fileSelectionMode;
	}

	@Override
	public void setEnabled(final boolean enabled)
	{
		super.setEnabled(enabled);
		textField.setEnabled(enabled);
		button.setEnabled(enabled);
	}

	public void setFile(final File f)
	{
		if (null == f)
		{
			textField.setText(null);
		} else
		{
			textField.setText(f.getAbsolutePath());
		}
	}

	public File getFile()
	{
		return new File(textField.getText());
	}

	public Document getDocument()
	{
		return textField.getDocument();
	}

	private void initComponents()
	{
		setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		add(textField, BorderLayout.CENTER);
		textField.setColumns(10);

		button = new JButton("...");
		button.addActionListener(browseActionListener);
		add(button, BorderLayout.EAST);
	}

	private final ActionListener browseActionListener = new ActionListener()
	{

		@Override
		public void actionPerformed(final ActionEvent e)
		{
			final JFileChooser fc = new JFileChooser(getFile());
			fc.setFileSelectionMode(fileSelectionMode);
			if (JFileChooser.APPROVE_OPTION == fc
					.showOpenDialog(FileSelectionField.this))
			{
				setFile(fc.getSelectedFile());
			}
		}
	};
	private JButton button;

}
