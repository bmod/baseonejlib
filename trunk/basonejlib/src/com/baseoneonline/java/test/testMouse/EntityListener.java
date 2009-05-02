package com.baseoneonline.java.test.testMouse;

public interface EntityListener {
	public void onRollOver(EntityEvent ev);

	public void onRollOut(EntityEvent ev);

	public void onPress(EntityEvent ev);

	public void onRelease(EntityEvent ev);
}
