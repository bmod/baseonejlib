package com.baseoneonline.java.slickTest;

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.InputAdapter;

public class SlickTest implements Game {

	public static void main(final String[] args) {
		try {
			final AppGameContainer container = new AppGameContainer(
					new SlickTest());
			container.setMinimumLogicUpdateInterval(20);
			container.setDisplayMode(800, 600, false);
			container.start();
		} catch (final SlickException e) {
			e.printStackTrace();
		}
	}

	public boolean closeRequested() {
		return true;
	}

	Font font;
	TrueTypeFont ttf;
	Menu<MenuItem> menu = new Menu<MenuItem>();

	public void init(final GameContainer gc) throws SlickException {

		setDirectory(new File(System.getProperty("user.home")));
		font = new Font("Verdana", Font.BOLD, 20);
		ttf = new TrueTypeFont(font, true);

		gc.getInput().addListener(new InputAdapter() {
			@Override
			public void keyPressed(final int key, final char c) {
				switch (key) {
					case Input.KEY_ESCAPE:
						System.exit(0);
						break;
					case Input.KEY_UP:
						menu.prevItem();
						break;
					case Input.KEY_DOWN:
						menu.nextItem();
						break;
					case Input.KEY_LEFT:

						break;
					case Input.KEY_RIGHT:

						break;
					case Input.KEY_ENTER:
						activateItem(menu.getSelectedItem());
						break;
					default:
				}
			}
		});

	}

	private void activateItem(final MenuItem item) {
		if (ItemType.DIR == item.getType()) {
			setDirectory(item.getFile());
		} else {
			Logger.getLogger(getClass().getName()).info(
					"Opening " + item.getFile());
		}
	}

	private void setDirectory(final File dir) {
		menu.clear();
		final File parent = dir.getParentFile();
		if (null != parent) {
			menu.addItem(new MenuItem(parent, ".."));
		}
		for (final File f : dir.listFiles()) {
			menu.addItem(new MenuItem(f));
		}
		if ((null != parent) && (menu.size() > 1)) {
			menu.setSelectedIndex(1);
		} else {
			menu.setSelectedIndex(0);
		}
	}

	public void render(final GameContainer gc, final Graphics g)
			throws SlickException {
		final float spacing = 30;
		Color col = Color.white;
		String tab = "";
		for (int i = 0; i < menu.size(); i++) {
			final MenuItem item = menu.getItem(i);
			final String n = item.toString();

			if (ItemType.DIR == item.getType()) {
				col = new Color(.7f, .8f, .9f);
			} else {
				col = new Color(.9f, .8f, .7f);
			}
			if (i == menu.getSelectedIndex()) {
				col.a = 1.0f;
				tab = " ";
			} else {
				col.a = 0.7f;
				tab = "";
			}

			ttf.drawString(30f, 30f + (spacing * i), tab + n, col);
		}
	}

	public void update(final GameContainer gc, final int t)
			throws SlickException {}

	public String getTitle() {
		
		return "Title";
	}

}

enum ItemType {
	FILE, DIR
}

class MenuItem {

	private final File file;
	private final String name;
	private final ItemType type;

	public MenuItem(final File f, final String name) {
		file = f;
		this.name = name;
		if (file.isDirectory()) {
			type = ItemType.DIR;
		} else {
			type = ItemType.FILE;
		}
	}

	public ItemType getType() {
		return type;
	}

	public MenuItem(final File f) {
		this(f, f.getName());
	}

	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return name;
	}
}

class Menu<T> {

	private final ArrayList<T> items = new ArrayList<T>();
	private int selectedIndex;
	private boolean wrapSelection = true;

	public Menu() {

	}

	public void nextItem() {
		if (wrapSelection) {
			selectedIndex++;
			if (selectedIndex > items.size() - 1) {
				selectedIndex = 0;
			}
		} else {
			if (selectedIndex < items.size() - 1) {
				selectedIndex++;
			}
		}
	}

	public void prevItem() {
		if (wrapSelection) {
		selectedIndex--;
		if (selectedIndex < 0) {
			selectedIndex = items.size()-1;
		}
		} else {
		if (selectedIndex > 0) {
			selectedIndex--;
		}
		}
	}

	public void clear() {
		items.clear();
	}

	public T getItem(final int index) {
		return items.get(index);
	}

	public void addItem(final T item) {
		items.add(item);
	}

	public void setSelectedIndex(final int index) {
		selectedIndex = index;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public T getSelectedItem() {
		return items.get(selectedIndex);
	}

	public int size() {
		return items.size();
	}

	public void setWrapSelection(final boolean wrapSelection) {
		this.wrapSelection = wrapSelection;
	}

	public boolean isWrapSelection() {
		return wrapSelection;
	}

}