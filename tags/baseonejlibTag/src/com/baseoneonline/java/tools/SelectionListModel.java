package com.baseoneonline.java.tools;

import java.util.ArrayList;

public class SelectionListModel<T> {

	private final ArrayList<T> items = new ArrayList<T>();
	private T selectedItem;
	private int selectedIndex = -1;

	private final ArrayList<SelectionListModelListener> listeners = new ArrayList<SelectionListModelListener>();

	public SelectionListModel() {}

	public void addListener(final SelectionListModelListener l) {
		listeners.add(l);
	}

	public void removeListener(final SelectionListModelListener l) {
		listeners.remove(l);
	}

	public void addItem(final T item) {
		items.add(item);
		fireItemAdded(item, items.indexOf(item));
	}

	public void removeItem(final T item) {
		final int index = items.indexOf(item);
		items.remove(item);
		fireItemRemoved(item, index);
	}

	public void setSelectedItem(final T item) {
		final int prevIndex = this.selectedIndex;
		final T prevItem = this.selectedItem;
		selectedItem = item;
		selectedIndex = items.indexOf(item);
		fireItemSelected(item, selectedIndex, prevItem, prevIndex);
	}

	public T getSelectedItem() {
		return selectedItem;
	}

	public int size() {
		return items.size();
	}

	public void setSelectedIndex(final int index) {
		final int prevIndex = this.selectedIndex;
		final T prevItem = this.selectedItem;
		this.selectedIndex = index;
		selectedItem = items.get(index);
		fireItemSelected(selectedItem, index, prevItem, prevIndex);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	private void fireItemAdded(final T item, final int index) {
		for (final SelectionListModelListener l : listeners) {
			l.itemAdded(item, index);
		}
	}

	private void fireItemRemoved(final T item, final int index) {
		for (final SelectionListModelListener l : listeners) {
			l.itemRemoved(item, index);
		}
	}

	private void fireItemSelected(final T item, final int index,
			final T prevItem, final int prevIndex) {
		for (final SelectionListModelListener l : listeners) {
			l.itemSelected(item, index, prevItem, prevIndex);
		}
	}

	public void clear() {
		for (int i = items.size() - 1; i >= 0; i--) {
			removeItem(items.get(i));
		}
	}

	public T getItem(final int i) {
		return items.get(i);
	}
}
