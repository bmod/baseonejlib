package com.baseoneonline.java.math;

import java.util.ArrayList;

public class SimplifyDouglasPeucker
{

	// adapted from
	// http://geometryalgorithms.com/Archive/algorithm_0205/algorithm_0205.htm
	// original copyright message follows

	// Copyright 2002, softSurfer (www.softsurfer.com)
	// This code may be freely used and modified for any purpose
	// providing that this copyright notice is included with it.
	// SoftSurfer makes no warranty for this code, and cannot be held
	// liable for any real or imagined damage resulting from its use.
	// Users of this code must verify correctness for their application.

	/**
	 * This method will reduce a 2D complex polyline.
	 * 
	 * @param tol
	 *            the tolerance of the reduction algorithm. Higher numbers will
	 *            simplify the line more.
	 * @param V
	 *            the array of Vec2fs to be simplified
	 * @return an array of Vec2f representing the simplified polyline
	 */
	public static Vec2f[] simplifyLine2D(float tol, Vec2f[] V)
	{

		int n = V.length;

		int i, k, m, pv;
		float tol2 = tol * tol;
		Vec2f[] vt = new Vec2f[n];
		int[] mk = new int[n];

		ArrayList<Vec2f> sV = new ArrayList<>();

		for (int b = 0; b < n; b++)
		{
			mk[b] = 0;
		}

		// STAGE 1 simple vertex reduction
		vt[0] = V[0];

		for (i = k = 1, pv = 0; i < n; i++)
		{
			if (V[i].distanceSquared(V[pv]) < tol2)
				continue;
			vt[k++] = V[i];
			pv = i;
		}

		if (pv < n - 1)
			vt[k++] = V[n - 1];

		// STAGE 2 Douglas-Peucker polyline simplify
		// mark the first and last vertices
		mk[0] = mk[k - 1] = 1;
		simplifyDP2D(tol, vt, 0, k - 1, mk);

		// copy marked vertices to output
		for (i = m = 0; i < k; i++)
		{
			if (mk[i] == 1)
				sV.add(vt[i]);
		}

		return sV.toArray(new Vec2f[sV.size()]);

	}

	private static void simplifyDP2D(float tol, Vec2f[] v, int j, int k,
			int[] mk)
	{

		if (k <= j + 1)
			return; // nothing to simplify

		int maxi = j;
		float maxd2 = 0;
		float tol2 = tol * tol;
		// Seg S = new Seg(v[j], v[k]);

		Vec2f u = v[k].minus(v[j]);
		float cu = u.dot(u);

		Vec2f w;
		Vec2f Pb;
		float b, cw, dv2;

		for (int i = j + 1; i < k; i++)
		{
			w = v[i].minus(v[j]);
			cw = w.dot(u);
			if (cw <= 0)
				dv2 = v[i].distanceSquared(v[j]);
			else if (cu <= cw)
				dv2 = v[i].distanceSquared(v[k]);
			else
			{
				b = cw / cu;
				Pb = v[j].minus(u.mult(-b));
				dv2 = v[i].distanceSquared(Pb);

			}

			if (dv2 <= maxd2)
				continue;
			maxi = i;
			maxd2 = dv2;
		}
		if (maxd2 > tol2)
		{
			mk[maxi] = 1;
			simplifyDP2D(tol, v, j, maxi, mk);
			simplifyDP2D(tol, v, maxi, k, mk);

		}
		return;

	}

	/**
	 * This method will reduce a 3D complex polyline.
	 * 
	 * @param tol
	 *            the tolerance of the reduction algorithm. Higher numbers will
	 *            simplify the line more.
	 * @param V
	 *            the array of Vec3f to be simplified
	 * @return an array of Vec3f representing the simplified polyline
	 */
	public static Vec3f[] simplifyLine3D(float tol, Vec3f[] V)
	{

		int n = V.length;

		int i, k, m, pv;
		float tol2 = tol * tol;
		Vec3f[] vt = new Vec3f[n];
		int[] mk = new int[n];

		ArrayList<Vec3f> sV = new ArrayList<>();

		for (int b = 0; b < n; b++)
		{
			mk[b] = 0;
		}

		// STAGE 1 simple vertex reduction
		vt[0] = V[0];

		for (i = k = 1, pv = 0; i < n; i++)
		{
			if (V[i].distSquared(V[pv]) < tol2)
				continue;
			vt[k++] = V[i];
			pv = i;
		}

		if (pv < n - 1)
			vt[k++] = V[n - 1];

		// STAGE 2 Douglas-Peucker polyline simplify
		// mark the first and last vertices
		mk[0] = mk[k - 1] = 1;
		simplifyDP3D(tol, vt, 0, k - 1, mk);

		// copy marked vertices to output
		for (i = m = 0; i < k; i++)
		{
			if (mk[i] == 1)
				sV.add(vt[i]);
		}

		return sV.toArray(new Vec3f[sV.size()]);
	}

	private static void simplifyDP3D(float tol, Vec3f[] v, int j, int k,
			int[] mk)
	{

		if (k <= j + 1)
			return; // nothing to simplify

		int maxi = j;
		float maxd2 = 0;
		float tol2 = tol * tol;
		// Seg S = new Seg(v[j], v[k]);

		Vec3f u = v[k].subtract(v[j]);
		float cu = u.dot(u);

		Vec3f w;
		Vec3f Pb;
		float b, cw, dv2;

		for (int i = j + 1; i < k; i++)
		{
			w = v[i].subtract(v[j]);
			cw = w.dot(u);
			if (cw <= 0)
				dv2 = v[i].distSquared(v[j]);
			else if (cu <= cw)
				dv2 = v[i].distSquared(v[k]);
			else
			{
				b = cw / cu;
				Pb = v[j].subtract(u.mult(-b));
				dv2 = v[i].distSquared(Pb);

			}

			if (dv2 <= maxd2)
				continue;
			maxi = i;
			maxd2 = dv2;
		}
		if (maxd2 > tol2)
		{
			mk[maxi] = 1;
			simplifyDP3D(tol, v, j, maxi, mk);
			simplifyDP3D(tol, v, maxi, k, mk);

		}
		return;

	}
}
