package test;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
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
	private final FileSelectionField fileSelectionField;

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
		fileSelectionField = new FileSelectionField();
		fileSelectionField.addListener(directoryListener);
		fileSelectionField.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 165, 416, 0 };
		gridBagLayout.rowHeights = new int[] { 430, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		GridBagConstraints gbc_fileSelectionField = new GridBagConstraints();
		gbc_fileSelectionField.insets = new Insets(0, 0, 0, 5);
		gbc_fileSelectionField.gridx = 0;
		gbc_fileSelectionField.gridy = 0;
		getContentPane().add(fileSelectionField, gbc_fileSelectionField);

		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);

		panel.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panel.add(scrollPane);

		list = new JList<File>();
		scrollPane.setViewportView(list);
		list.setModel(new EventListModel<File>(emptyDirs));

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		JButton btnScan = new JButton("Scan");
		panel_1.add(btnScan);

		lblStatus = new JLabel("Status");
		panel_1.add(lblStatus);

		JButton btnDeleteThoseEmpty = new JButton("Delete those empty dirs!");
		panel_1.add(btnDeleteThoseEmpty);
		progressBar = new JProgressBar();
		panel_1.add(progressBar);

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
	}

	private final DisposableFilter filter = new DisposableFilter()
	{

		@Override
		public boolean isDisposable(File f)
		{
			return false;
		}
	};
	private final JList<File> list;
	private final JProgressBar progressBar;
	private final JScrollPane scrollPane;
	private final JLabel lblStatus;
	private final JPanel panel_1;
	private final JPanel panel;
}
