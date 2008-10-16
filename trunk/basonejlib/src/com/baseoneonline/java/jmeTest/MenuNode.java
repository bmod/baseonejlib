package com.baseoneonline.java.jmeTest;

import java.util.ArrayList;

import com.baseoneonline.java.jmeTest.text.FontQuad;
import com.jme.scene.Node;

public class MenuNode<T> extends Node {

	/**
	 *
	 */
	private static final long serialVersionUID = 2859016117361315942L;
	private final ArrayList<TextLabel2D> labels = new ArrayList<TextLabel2D>();
	private final MenuModel<T> model;
	private final float itemSpacing = 2;

	public MenuNode(final MenuModel<T> model) {
		this.model = model;
		recreateMenuItems();
	}

	private void recreateMenuItems() {
		labels.clear();
		detachAllChildren();
		for (int i = 0; i < model.size(); i++) {
			final String labelText;
			labelText = model.getLabel(model.get(i));
			final TextLabel2D label = new TextLabel2D(labelText);
			labels.add(label);
			final FontQuad q = label.getQuad(2f);
			q.setLocalTranslation(0, i * -itemSpacing, 0);
			attachChild(q);
		}
		updateMenuSelection();
	}

	public TextLabel2D getSelectedLabel() {
		return labels.get(model.getSelectedIndex());
	}

	public void next() {
		int i = model.getSelectedIndex() + 1;
		if (i > model.size() - 1) {
			i = 0;
		}
		model.setSelectedIndex(i);
		updateMenuSelection();
	}

	public void prev() {
		int i = model.getSelectedIndex() - 1;
		if (i < 0) {
			i = model.size() - 1;
		}
		model.setSelectedIndex(i);
		updateMenuSelection();
	}

	private void updateMenuSelection() {
		for (int i = 0; i < labels.size(); i++) {
			if (model.getSelectedIndex() == i) {
				labels.get(i).getQuad().setLocalScale(1.2f);
			} else {
				labels.get(i).getQuad().setLocalScale(1);
			}
		}

	}

}
