package test;

import java.awt.event.WindowAdapter;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.EventListModel;

import com.baseoneonline.java.swing.FileSelectionField;
import com.baseoneonline.java.swing.FileSelectionField.Listener;
import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.java.tools.FileUtils;
import com.baseoneonline.java.tools.FileUtils.DisposableFilter;

public class TestDeleteEmptyDirectories extends JFrame
{
	private FileSelectionField fileSelectionField;

	private final EventList<File> emptyDirs = GlazedLists
			.threadSafeList(new BasicEventList<File>());

	private File rootDir = null;

	private SwingWorker<Void, File> worker;

	public static void main(String[] args)
	{
		Config.setApplicationClass(TestDeleteEmptyDirectories.class);
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		TestDeleteEmptyDirectories app = new TestDeleteEmptyDirectories();
		Config.get().persist(app);
		app.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public TestDeleteEmptyDirectories()
	{

		initComponents();

		fileSelectionField.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		list.setModel(new EventListModel<File>(emptyDirs));

		emptyDirs.addListEventListener(new ListEventListener<File>()
		{

			@Override
			public void listChanged(ListEvent<File> arg0)
			{
				updateStatus();
			}
		});

		addWindowListener(winAdapter);
	}

	private void updateStatus()
	{
		if (null != worker)
		{
			lblStatus
					.setText(String.format(
							"Scanning for empty directories... (%s)",
							emptyDirs.size()));
		} else
		{
			lblStatus.setText(String.format("Empty directories found: %s",
					emptyDirs.size()));
		}
	}

	public void setRootDir(final File rootDir)
	{
		this.rootDir = rootDir;
		updateEmptyDirectories();
	}

	private void updateEmptyDirectories()
	{
		if (null != worker)
			worker.cancel(true);

		setProgressEnabled(true);
		emptyDirs.clear();

		worker = new SwingWorker<Void, File>()
		{

			@Override
			protected Void doInBackground() throws Exception
			{
				FileUtils.findEmpty(rootDir, emptyDirs, filter);
				return null;
			}

			@Override
			protected void done()
			{
				worker = null;
				setProgressEnabled(false);
				updateStatus();
			}
		};
		worker.execute();
	}

	private void setProgressEnabled(boolean b)
	{
		progressBar.setMaximum(b ? 1 : 0);
		progressBar.setValue(b ? 0 : 1);
		progressBar.setIndeterminate(b);
		progressBar.setVisible(b);

	}

	private final FileSelectionField.Listener directoryListener = new Listener()
	{

		@Override
		public void fileChanged(File f)
		{
			setRootDir(f);
		}
	};

	private final WindowAdapter winAdapter = new WindowAdapter()
	{
		@Override
		public void windowClosing(java.awt.event.WindowEvent e)
		{
			if (null != worker)
				worker.cancel(true);

			Config.get().flush();
			e.getWindow().dispose();
		}
	};

	private void initComponents()
	{
		setTitle("TestFindEmptyDirectories");

		fileSelectionField = new FileSelectionField();

		scrollPane = new JScrollPane();

		list = new JList<File>();
		scrollPane.setViewportView(list);
		fileSelectionField.addListener(directoryListener);
		progressBar = new JProgressBar();

		lblStatus = new JLabel("Status");

		JButton btnDeleteThoseEmpty = new JButton("Delete those empty dirs!");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																scrollPane,
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																562,
																Short.MAX_VALUE)
														.addComponent(
																fileSelectionField,
																Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE,
																562,
																Short.MAX_VALUE)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				lblStatus,
																				GroupLayout.DEFAULT_SIZE,
																				412,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				progressBar,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
														.addComponent(
																btnDeleteThoseEmpty))
										.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(fileSelectionField,
								GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
								330, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnDeleteThoseEmpty)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								groupLayout
										.createParallelGroup(Alignment.LEADING)
										.addComponent(progressBar,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblStatus))
						.addContainerGap()));
		getContentPane().setLayout(groupLayout);
	}

	private final DisposableFilter filter = new DisposableFilter()
	{

		@Override
		public boolean isDisposable(File f)
		{
			return false;
		}
	};
	private JList<File> list;
	private JProgressBar progressBar;
	private JScrollPane scrollPane;
	private JLabel lblStatus;
}
