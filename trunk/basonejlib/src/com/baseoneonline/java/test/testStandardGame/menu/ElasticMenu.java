package com.baseoneonline.java.test.testStandardGame.menu;

import java.util.HashMap;

import com.baseoneonline.java.jme.springParticles.Particle;
import com.baseoneonline.java.jme.springParticles.ParticleSystem;
import com.baseoneonline.java.test.testStandardGame.menu.MenuInput.Trigger;
import com.baseoneonline.java.tools.SelectionListModel;
import com.baseoneonline.java.tools.SelectionListModelListener;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

public class ElasticMenu extends Node implements IMenuInputListener,
		SelectionListModelListener {

	float screenWidth = DisplaySystem.getDisplaySystem().getWidth();
	float screenHeight = DisplaySystem.getDisplaySystem().getHeight();

	private final float itemSpacing = 20;

	private final ParticleSystem psys = new ParticleSystem();
	private final BitmapTextFactory fac;
	private SelectionListModel<?> model;
	private final HashMap<Object, MenuItem> items = new HashMap<Object, MenuItem>();
	private final float selectionIndent = 20;

	public ElasticMenu(final SelectionListModel<?> model) {

		fac = new BitmapTextFactory("assets/images/Egyptienne14", 14);
		setModel(model);
		model.addListener(this);
	}

	public void setModel(final SelectionListModel<?> model) {
		if (null != this.model) {
			model.clear();
		}
		this.model = model;

		for (int i = 0; i < model.size(); i++) {
			itemAdded(model.getItem(i), i);
		}
	}

	@Override
	public void updateGeometricState(final float time, final boolean initiator) {
		psys.update(time);
		super.updateGeometricState(time, initiator);
	}

	@Override
	public void onMenuInput(final Trigger trigger) {
		// System.out.println(trigger.name());
		int index;
		switch (trigger) {
		case Up:
			index = model.getSelectedIndex();
			if (index == 0) index = model.size() - 1;
			else
				index--;
			model.setSelectedIndex(index);
			break;
		case Down:
			index = model.getSelectedIndex();
			if (index == model.size() - 1) index = 0;
			else
				index++;
			model.setSelectedIndex(index);
			break;
		case Left:
			break;
		case Right:
			break;
		default:
			break;
		}
	}

	@Override
	public void itemAdded(final Object data, final int index) {
		final MenuItem item = new MenuItem(fac.createText(data.toString()),
				data);
		item.particle.K = .1f;
		item.particle.damp = .68f;
		item.particle.x = screenWidth;
		item.particle.y = screenHeight - index * itemSpacing;
		if (index == model.getSelectedIndex()) {
			item.particle.tx = selectionIndent;
		} else {
			item.particle.tx = 0;
		}
		item.particle.ty = screenHeight - index * itemSpacing;
		psys.addParticle(item.particle);
		attachChild(item);
		items.put(data, item);
	}

	@Override
	public void itemSelected(final Object item, final int index,
			final Object prevItem, final int prevIndex) {
		if (null != prevItem) items.get(prevItem).particle.tx = 0;
		items.get(item).particle.tx = selectionIndent;

	}

	@Override
	public void itemRemoved(final Object data, final int index) {
		final MenuItem item = items.get(data);
		psys.removeParticle(item.particle);
		detachChild(item);
		items.remove(data);
	}
}

class MenuItem extends Node {

	public Particle particle = new Particle();
	public Object data;

	public MenuItem() {}

	public MenuItem(final Spatial spatial, final Object data) {
		attachChild(spatial);
		this.data = data;
	}

	@Override
	public void updateGeometricState(final float time, final boolean initiator) {
		setLocalTranslation(particle.x, particle.y, 0);
		super.updateGeometricState(time, initiator);
	}
}
