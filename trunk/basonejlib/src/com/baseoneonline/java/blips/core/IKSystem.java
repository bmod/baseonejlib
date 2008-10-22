package com.baseoneonline.java.blips.core;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;

public class IKSystem {

	private final List<IKContraint> list = new ArrayList<IKContraint>();

	public void addConstraint(final IKContraint c) {
		list.add(c);
	}

	public void solveConstraints() {
		for (final IKContraint c : list) {
			final Vector3f dif = c.parent.getLocalTranslation().subtract(c.child.getLocalTranslation());
			c.child.setLocalTranslation(c.parent.getLocalTranslation().add(dif.multLocal(c.distance)));
		}
	}


}
