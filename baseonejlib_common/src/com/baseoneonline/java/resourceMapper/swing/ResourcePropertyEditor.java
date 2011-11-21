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

import com.baseoneonline.java.resourceMapper.ResourceNode;
import com.baseoneonline.java.swing.config.Config;

public class ResourcePropertyEditor extends JPanel
{
	private JLabel lbTitle;
	private JScrollPane scrollPane;
	private EditableTable table;

	private final PropertyTableModel tableModel = new PropertyTableModel();

	public ResourcePropertyEditor()
	{
		initComponents();
		table.setModel(tableModel);
		Config.get().persist(getClass().getName() + "Table", table);
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

		table = new EditableTable();
		table.setRowSelectionAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(false);
		scrollPane.setViewportView(table);
	}

	public void setResource(ResourceNode node)
	{
		lbTitle.setText(node.getId() + " : "
				+ node.getResource().getClass().getSimpleName());
		tableModel.setResource(node.getResource());

	}

}
