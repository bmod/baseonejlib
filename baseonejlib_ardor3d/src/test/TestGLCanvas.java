package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import com.baseoneonline.java.swing.config.Config;
import com.baseoneonline.jlib.ardor3d.swing.ArdorManager;
import com.baseoneonline.jlib.ardor3d.swing.ArdorPanel;
import com.baseoneonline.jlib.ardor3d.swing.EditorScene;
import com.baseoneonline.jlib.ardor3d.tools.SceneGraphPanel;

public class TestGLCanvas extends JFrame {

	public static void main(final String[] args) throws Exception {
		Config.setApplicationClass(TestGLCanvas.class);

		final EditorScene scene = new EditorScene();

		ArdorManager.get().getFrameWork().addUpdater(scene);

		final TestGLCanvas frame = new TestGLCanvas();

		JPanel panel = new ArdorPanel(scene);
		frame.setScenePanel(panel);

		// frame.pack();
		frame.setVisible(true);

		scene.init();

		ArdorManager.get().start();

		frame.dispose();
		System.exit(0);
	}

	public TestGLCanvas() {
		initComponents();

		Config.get().persist(this);
	}

	private void initComponents() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(winAdapter);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mnFile.add(action);
		getContentPane().setLayout(new BorderLayout(0, 0));

		SceneGraphPanel sceneGraphPanel = new SceneGraphPanel();
		getContentPane().add(sceneGraphPanel, BorderLayout.WEST);
	}

	public void setScenePanel(JPanel panel) {
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	private final WindowAdapter winAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			ArdorManager.get().shutdown();
			dispose();
		};
	};
	private final Action action = new FileOpenAction();

}

class FileOpenAction extends AbstractAction {

	public FileOpenAction() {
		putValue(NAME, "Open...");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
