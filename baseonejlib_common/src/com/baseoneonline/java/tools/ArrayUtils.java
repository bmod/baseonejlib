package com.baseoneonline.java.tools;

public class ArrayUtils
{
	public static <T> void shiftLeft(T[] array, int amount)
	{
		for (int j = 0; j < amount; j++)
		{
			T a = array[0];
			int i;
			for (i = 0; i < array.length - 1; i++)
				array[i] = array[i + 1];
			array[i] = a;
		}
	}

	public static <T> void shiftRight(T[] array, int amount)
	{
		for (int j = 0; j < amount; j++)
		{
			T a = array[array.length - 1];
			int i;
			for (i = array.length - 1; i > 0; i--)
				array[i] = array[i - 1];
			array[i] = a;
		}
	}
}
