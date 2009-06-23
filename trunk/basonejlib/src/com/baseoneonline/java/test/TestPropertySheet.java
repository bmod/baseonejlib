package com.baseoneonline.java.test;

import javax.swing.JFrame;

import com.l2fprod.common.propertysheet.AbstractProperty;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

public class TestPropertySheet extends JFrame {

	public static void main(String[] args) {
		TestPropertySheet app = new TestPropertySheet();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(200, 400);
		app.setVisible(true);
	}

	public TestPropertySheet() {
		PropertySheetPanel propSheet = new PropertySheetPanel();

		

		add(propSheet);
	}

}

abstract class SimpleAbstractProperty<T> extends AbstractProperty {

	private final String name;
	private final String category;


	private final boolean editable = true;

	public SimpleAbstractProperty(String name, String category) {
		this.name = name;
		this.category = category;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getShortDescription() {
		return null;
	}

	@Override
	public Class<?> getType() {
		return readValue().getClass();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	public abstract T readValue();

	public abstract void writeValue(T value);


	@Override
	public void readFromObject(Object object) {

	}

	@Override
	public void writeToObject(Object object) {

	}
}
