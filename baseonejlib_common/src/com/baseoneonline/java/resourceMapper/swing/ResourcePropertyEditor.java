package com.baseoneonline.java.resourceMapper.swing;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import com.baseoneonline.java.resourceMapper.Resource;
import com.baseoneonline.java.resourceMapper.ResourceNode;
import com.baseoneonline.java.resourceMapper.ResourceProperty;

public class ResourcePropertyEditor extends JPanel
{
	private JLabel lbTitle;
	private JScrollPane scrollPane;
	private JTable table;

	private final PropertyTableModel tableModel = new PropertyTableModel();

	public ResourcePropertyEditor()
	{
		initComponents();
		table.setModel(tableModel);
	}

	private void initComponents()
	{
		setBorder(new EmptyBorder(8, 8, 8, 8));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 434, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 14, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lbTitle = new JLabel("New label");
		lbTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lbTitle = new GridBagConstraints();
		gbc_lbTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lbTitle.anchor = GridBagConstraints.NORTH;
		gbc_lbTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_lbTitle.gridx = 0;
		gbc_lbTitle.gridy = 0;
		add(lbTitle, gbc_lbTitle);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
	}

	public void setResource(ResourceNode node)
	{
		lbTitle.setText(node.id + " : " + node.res.getClass().getSimpleName());
		tableModel.setResource(node.res);
	}

}

class PropertyTableModel extends AbstractTableModel
{
	private final String[] columnNames =
	{ "Key", "Value" };
	private Resource res = null;

	public void setResource(Resource res)
	{
		this.res = res;
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}

	@Override
	public int getRowCount()
	{
		if (null == res)
			return 0;

		return res.getProperties().size();
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		ResourceProperty prop = res.getProperties().get(rowIndex);
		switch (columnIndex)
		{
		case 0:
			return prop.id;
		case 1:
			return prop.value;
		default:
			return null;
		}
	}

}
