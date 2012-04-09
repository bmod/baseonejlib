package com.baseoneonline.jlib.ardor3d.math;

import java.util.ArrayList;
import java.util.List;

public abstract class Pool<T>
{
	private final ThreadLocal<List<T>> _pool = new ThreadLocal<List<T>>()
	{
		@Override
		protected List<T> initialValue()
		{
			return new ArrayList<T>(_maxSize);
		}
	};

	private final int _maxSize;

	protected Pool(final int maxSize)
	{
		_maxSize = maxSize;
	}

	protected abstract T newInstance();

	public final T fetch()
	{
		final List<T> objects = _pool.get();
		return objects.isEmpty() ? newInstance() : objects.remove(objects
				.size() - 1);
	}

	public final void release(final T object)
	{
		if (object == null)
		{
			throw new RuntimeException(
					"Should not release null objects into ObjectPool.");
		}

		final List<T> objects = _pool.get();
		if (objects.size() < _maxSize)
		{
			objects.add(object);
		}
	}

	public static <T> Pool<T> create(final Class<T> clazz, final int maxSize)
	{
		return new Pool<T>(maxSize)
		{
			@Override
			protected T newInstance()
			{
				try
				{
					return clazz.newInstance();
				} catch (final Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		};
	}
}
