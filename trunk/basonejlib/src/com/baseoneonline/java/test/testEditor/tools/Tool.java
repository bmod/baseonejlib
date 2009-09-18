package com.baseoneonline.java.test.testEditor.tools;

import com.baseoneonline.java.test.testEditor.core.EditPanel;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;

public abstract class Tool extends PBasicInputEventHandler {

	protected EditPanel editor;

	public Tool(EditPanel canvas) {
		this.editor = canvas;
	}
}
