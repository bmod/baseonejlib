package com.baseoneonline.java.swing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ListModelWrapper<T> implements ListModel<T>, List<T>
{

	private final HashSet<ListDataListener> listeners = new HashSet<ListDataListener>();
	private List<T> sourceData;

	public ListModelWrapper(final List<T> source)
	{
		this.sourceData = source;
	}

	public ListModelWrapper()
	{
		sourceData = new ArrayList<T>();
	}

	@Override
	public int getSize()
	{
		return sourceData.size();
	}

	@Override
	public T getElementAt(final int index)
	{
		return sourceData.get(index);
	}

	@Override
	public void addListDataListener(final ListDataListener l)
	{
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(final ListDataListener l)
	{
		listeners.remove(l);
	}

	@Override
	public int size()
	{
		return sourceData.size();
	}

	@Override
	public boolean isEmpty()
	{
		return sourceData.isEmpty();
	}

	@Override
	public boolean contains(final Object o)
	{
		return sourceData.contains(o);
	}

	@Override
	public Iterator<T> iterator()
	{
		return sourceData.iterator();
	}

	@Override
	public Object[] toArray()
	{
		return sourceData.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(final T[] a)
	{
		return sourceData.toArray(a);
	}

	@Override
	public boolean add(final T e)
	{
		final boolean re = sourceData.add(e);
		if (re) {
			final int index = sourceData.size() - 1;
			fireIntervalAdded(index, index);
		}
		return re;
	}

	@Override
	public boolean remove(final Object o)
	{
		final int index = sourceData.indexOf(o);
		if (-1 == index)
			return false;

		sourceData.remove(index);
		fireIntervalRemoved(index, index);
		return true;
	}

	@Override
	public boolean containsAll(final Collection<?> c)
	{
		return sourceData.containsAll(c);
	}

	@Override
	public boolean addAll(final Collection<? extends T> c)
	{
		final boolean re = sourceData.addAll(c);
		if (re)
			fireContentsChanged(0, getSize() - 1);
		return re;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends T> c)
	{
		final boolean re = sourceData.addAll(index, c);
		if (re)
			fireContentsChanged(0, getSize() - 1);
		return re;
	}

	@Override
	public boolean removeAll(final Collection<?> c)
	{
		final boolean re = sourceData.removeAll(c);
		if (re)
			fireContentsChanged(0, getSize() - 1);
		return re;
	}

	@Override
	public boolean retainAll(final Collection<?> c)
	{
		final boolean re = sourceData.retainAll(c);
		if (re)
			fireContentsChanged(0, getSize() - 1);
		return re;
	}

	@Override
	public void clear()
	{
		sourceData.clear();
		fireContentsChanged(0, 0);
	}

	@Override
	public T get(final int index)
	{
		return sourceData.get(index);
	}

	@Override
	public T set(final int index, final T element)
	{
		final T re = sourceData.set(index, element);
		fireContentsChanged(index, index);
		return re;
	}

	@Override
	public void add(final int index, final T element)
	{
		sourceData.add(index, element);
		fireIntervalAdded(index, index);
	}

	@Override
	public T remove(final int index)
	{
		final T re = sourceData.remove(index);
		fireIntervalRemoved(index, index);
		return re;
	}

	@Override
	public int indexOf(final Object o)
	{
		return sourceData.indexOf(o);
	}

	@Override
	public int lastIndexOf(final Object o)
	{
		return sourceData.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return sourceData.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(final int index)
	{
		return sourceData.listIterator(index);
	}

	@Override
	public List<T> subList(final int fromIndex, final int toIndex)
	{
		return sourceData.subList(fromIndex, toIndex);
	}

	/**
	 * Sent after the indices in the index0,index1 interval have been removed
	 * from the data model. The interval includes both index0 and index1.
	 * 
	 * @param index0
	 * @param index1
	 */
	private void fireIntervalRemoved(final int index0, final int index1)
	{
		final ListDataEvent e = new ListDataEvent(this,
				ListDataEvent.INTERVAL_REMOVED, index0, index1);
		for (final ListDataListener l : listeners)
			l.intervalRemoved(e);
	}

	/**
	 * Sent after the indices in the index0,index1 interval have been inserted
	 * in the data model. The new interval includes both index0 and index1.
	 * 
	 */
	private void fireIntervalAdded(final int index0, final int index1)
	{
		final ListDataEvent e = new ListDataEvent(this,
				ListDataEvent.INTERVAL_ADDED, index0, index1);
		for (final ListDataListener l : listeners)
			l.intervalAdded(e);
	}

	/**
	 * Sent when the contents of the list has changed in a way that's too
	 * complex to characterize with the previous methods. For example, this is
	 * sent when an item has been replaced. Index0 and index1 bracket the
	 * change.
	 * 
	 * @param index0
	 * @param index1
	 */
	private void fireContentsChanged(final int index0, final int index1)
	{
		final ListDataEvent e = new ListDataEvent(this,
				ListDataEvent.CONTENTS_CHANGED, index0, index1);
		for (final ListDataListener l : listeners)
			l.contentsChanged(e);
	}

	/**
	 * Replace the current source with the new list.
	 * 
	 * @param newSource
	 */
	public void set(final List<T> newSource)
	{
		this.sourceData = newSource;
		fireContentsChanged(0, sourceData.size() - 1);
	}
}
