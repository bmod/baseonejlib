package com.baseoneonline.java.mediadb.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.baseoneonline.java.mediadb.db.DatabaseListModel;
import com.db4o.ObjectContainer;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class FilteredList extends JPanel {
	
	private DatabaseListModel listModel;
	private JList list;
	private JLabel lbStatus;
	
	private SwingWorker<Void, Void> worker;
	
	public FilteredList() {
		initGUI();
	}
	
	public void refresh() {
		
		listModel.clear();
		
		if (worker != null && !worker.isDone()) {
			worker.cancel(true);
		}
		worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				listModel.refresh();
				lbStatus.setText(listModel.getSize()+" items.");
				return null;
			}
			@Override
			protected void done() {
				listModel.fireChange();
			}
		};
		worker.execute();
	}
	
	public void setDatabase(ObjectContainer db) {
		listModel = new DatabaseListModel(db);
		list.setModel(listModel);
		refresh();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		final JCheckBox cbMusic = new JCheckBox("Music", true);
		cbMusic.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listModel.showMusic = cbMusic.isSelected();
				refresh();
			}
		});
		topPanel.add(cbMusic);
		
		final JCheckBox cbImages = new JCheckBox("Images");
		cbImages.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listModel.showImages = cbImages.isSelected();
				refresh();
			}
		});
		topPanel.add(cbImages);
		
		final JCheckBox cbMovies = new JCheckBox("Movies");
		cbMovies.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listModel.showMovies = cbMovies.isSelected();
				refresh();
			}
		});
		topPanel.add(cbMovies);
		
		JTextField tfFilter = new JTextField();
		tfFilter.setPreferredSize(new Dimension(200,22));
		topPanel.add(tfFilter);
		
		add(topPanel, BorderLayout.NORTH);
		
		list = new JList();
		JScrollPane spList = new JScrollPane(list);
		add(spList, BorderLayout.CENTER);
		
		lbStatus = new JLabel("Status");
		add(lbStatus, BorderLayout.SOUTH);
		
	}

}
