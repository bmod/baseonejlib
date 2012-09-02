package test;

import java.util.ArrayList;
import java.util.Comparator;

import com.baseoneonline.java.tools.ListUtils;

public class TestSortedList
{
	public static void main(final String[] args)
	{
		final ArrayList<String> sortedList = new ArrayList<String>();

		final Comparator<String> stringComparator = new Comparator<String>()
		{

			@Override
			public int compare(final String o1, final String o2)
			{
				return o1.toLowerCase().compareTo(o2.toLowerCase());
			}
		};

		ListUtils.addSorted(sortedList, "Adele", stringComparator);
		ListUtils.addSorted(sortedList, "Coen", stringComparator);
		ListUtils.addSorted(sortedList, "Derek", stringComparator);
		ListUtils.addSorted(sortedList, "ZePhyr", stringComparator);
		ListUtils.addSorted(sortedList, "Bart", stringComparator);
		ListUtils.addSorted(sortedList, "Angela", stringComparator);
		ListUtils.addSorted(sortedList, "Aa", stringComparator);
		ListUtils.addSorted(sortedList, "Zephyr", stringComparator);
		final int re = ListUtils.addSorted(sortedList, "Xantippe",
				stringComparator);

		for (final String element : sortedList)
		{
			System.out.println(element);
		}

	}
}
