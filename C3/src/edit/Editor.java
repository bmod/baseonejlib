package edit;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.WindowConstants;

import game.Level;
import game.ResourceManager;

public class Editor {

	public static void main(final String[] args) {
		Editor.get().init();
	}

	public void init() {
		final EditorGUI frame = new EditorGUI();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(800, 500);

		frame.setVisible(true);
	}

	private Editor() {}

	public Level loadLevel(final String string) {

		final Level level = ResourceManager.get().getLevel(string);
		return level;
	}

	public ComboBoxModel levelListModel() {
		final DefaultComboBoxModel mdl = new DefaultComboBoxModel();
		for (final String handle : ResourceManager.get().getLevelHandles()) {
			mdl.addElement(handle);
		}
		return mdl;
	}

	private static Editor instance;

	public static Editor get() {
		if (null == instance) {
			instance = new Editor();
		}
		return instance;
	}

}