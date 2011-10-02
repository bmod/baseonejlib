package com.baseoneonline.java.tools;

public interface SelectionListModelListener {

	public void itemSelected(Object item, int index, Object prevItem,
			int prevIndex);

	public void itemAdded(final Object item, int index);

	public void itemRemoved(final Object item, int index);
}
