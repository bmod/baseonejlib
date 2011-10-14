package com.baseoneonline.java.tools;

import java.util.Comparator;
import java.util.List;

public class ListUtils
{
	private ListUtils()
	{

	}

	/**
	 * Will insert an item in the list, ordered by the provided comparator. When
	 * the provided list was not sorted, it just finds the first match.
	 * 
	 * @param list
	 * @param element
	 * @param comp
	 * @return
	 */
	public static <E> int addSorted(final List<E> list, final E element,
			final Comparator<E> comp)
	{
		int index = 0;
		final int len = list.size();

		for (; index < len; index++)
		{
			final E listElement = list.get(index);
			if (comp.compare(element, listElement) <= 0)
				break;
		}
		list.add(index, element);

		return index;
	}
}
