package com.baseoneonline.java.swing.config;

import java.awt.Rectangle;
import java.awt.Window;

public class WindowPersistenceFactory implements PersistenceFactory<Window> {

    @Override
    public void store(final Config conf, final String key, final Window value) {
	conf.putRectangle(key + "Bounds", (value).getBounds());
    }

    @Override
    public void restore(final Config conf, final String key, final Window value) {
	final Rectangle rect = conf.getRectangle(key + "Bounds",
		(value).getBounds());
	if (rect.getWidth() == 0)
	    rect.setSize(500, (int) rect.getHeight());
	if (rect.getHeight() == 0)
	    rect.setSize((int) rect.getWidth(), 500);

	if (null != rect)
	    (value).setBounds(rect);
    }

    @Override
    public Class<? extends Window> getType() {
	return Window.class;
    }

}
