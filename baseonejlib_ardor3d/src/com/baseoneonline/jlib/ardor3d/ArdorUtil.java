package com.baseoneonline.jlib.ardor3d;

import java.nio.FloatBuffer;
import java.util.Random;

import com.ardor3d.math.MathUtils;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Transform;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyVector2;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.util.geom.BufferUtils;

public class ArdorUtil {
	private static Random random = new Random();

	private ArdorUtil() {
	}

	public static double map(double inMin, double inMax, double outMin,
			double outMax, double value) {
		return outMin
				+ ((outMax - outMin) * ((value - inMin) / (inMax - inMin)));
	}

	public static Vector3 randomize(final Vector3 v, final double extents) {
		return v.addLocal(ArdorUtil.randRange(0, extents),
				ArdorUtil.randRange(0, extents),
				ArdorUtil.randRange(0, extents));
	}

	public static double randRange(final double center, final double extents) {
		return center + (random.nextDouble() * 2 - 1) * extents;
	}

	public static double lerp2(final double percentage, final double start,
			final double end) {
		final double tmp = MathUtils.lerp(percentage, start, end);
		return MathUtils.lerp(percentage, tmp, end);
	}

	public static Line createLine(final String name,
			final ReadOnlyVector3[] points, final ReadOnlyColorRGBA color) {
		final ReadOnlyVector3[] normals = ArdorUtil.createArray(Vector3.UNIT_Y,
				points.length);
		final ReadOnlyColorRGBA[] cols = ArdorUtil.createArray(color,
				points.length);
		final ReadOnlyVector2[] tex = ArdorUtil.createArray(Vector2.ZERO,
				points.length);

		final Line line = new Line(name, points, normals, cols, tex,
				IndexMode.LineStrip);
		return line;
	}

	public static ReadOnlyVector3[] createArray(final ReadOnlyVector3 element,
			final int size) {
		final ReadOnlyVector3[] arr = new ReadOnlyVector3[size];
		for (int i = 0; i < size; i++)
			arr[i] = element;
		return arr;
	}

	public static ReadOnlyVector2[] createArray(final ReadOnlyVector2 element,
			final int size) {
		final ReadOnlyVector2[] arr = new ReadOnlyVector2[size];
		for (int i = 0; i < size; i++)
			arr[i] = element;
		return arr;
	}

	public static ReadOnlyColorRGBA[] createArray(
			final ReadOnlyColorRGBA element, final int size) {
		final ReadOnlyColorRGBA[] arr = new ReadOnlyColorRGBA[size];
		for (int i = 0; i < size; i++)
			arr[i] = element;
		return arr;
	}

	public static FloatBuffer createFloatBuffer(final ReadOnlyColorRGBA color,
			final int size) {
		return BufferUtils
				.createFloatBuffer(ArdorUtil.createArray(color, size));
	}

	public static Quaternion lerp(final Quaternion q1, final Quaternion q2,
			final double t, final Quaternion store) {
		final Quaternion c = Quaternion.fetchTempInstance();
		final Quaternion d = Quaternion.fetchTempInstance();

		q1.multiply(1 - t, c);
		q2.multiply(t, d);

		q1.add(q2, store);
		store.normalizeLocal();

		Quaternion.releaseTempInstance(c);
		Quaternion.releaseTempInstance(d);

		return store;
	}

	public static Quaternion slerpNoInvert(final Quaternion q1,
			final Quaternion q2, final double t, final Quaternion store) {

		final Quaternion c = Quaternion.fetchTempInstance();
		final Quaternion d = Quaternion.fetchTempInstance();

		final double dot = q1.dot(q2);

		if (dot > -0.95f && dot < 0.95f) {
			final double angle = MathUtils.acos(dot);
			q1.multiply(MathUtils.sin(angle * (1 - t)), c);
			q2.multiply(MathUtils.sin(angle * t), d);
			c.add(d, store);

			ArdorUtil.divide(store, MathUtils.sin(angle), store);
			store.normalizeLocal();
		} else {
			ArdorUtil.lerp(q1, q2, t, store);
		}

		Quaternion.releaseTempInstance(c);
		Quaternion.releaseTempInstance(d);

		return store;
	}

	public static Quaternion divide(final Quaternion a, final double n,
			final Quaternion store) {
		if (0 == n)
			throw new ArithmeticException("Divide by zero!");
		return store.set(store.getX() / n, store.getY() / n, store.getZ() / n,
				store.getW() / n);

	}

	public static Quaternion squad(final Quaternion q1, final Quaternion q2,
			final Quaternion a, final Quaternion b, final double t,
			final Quaternion store) {
		final Quaternion c = Quaternion.fetchTempInstance();
		final Quaternion d = Quaternion.fetchTempInstance();

		ArdorUtil.slerpNoInvert(q1, q2, t, c);

		ArdorUtil.slerpNoInvert(a, b, t, d);

		ArdorUtil.slerpNoInvert(c, d, 2 * t * (1 - t), store);

		Quaternion.releaseTempInstance(c);
		Quaternion.releaseTempInstance(d);

		return store;
	}

	/**
	 * @return {float newValue, float currentVelocity};
	 */
	public static float[] smoothDamp(float current, float target,
			float currentVelocity, float smoothTime, float maxSpeed,
			float deltaTime) {
		smoothTime = Math.max(0.0001f, smoothTime);
		float num = 2f / smoothTime;
		float num2 = num * deltaTime;
		float num3 = 1f / (1f + num2 + 0.48f * num2 * num2 + 0.235f * num2
				* num2 * num2);
		float num4 = current - target;
		float num5 = target;
		float num6 = maxSpeed * smoothTime;
		num4 = MathUtils.clamp(num4, -num6, num6);
		target = current - num4;
		float num7 = (currentVelocity + num * num4) * deltaTime;
		currentVelocity = (currentVelocity - num * num7) * num3;
		float num8 = target + (num4 + num7) * num3;
		if (num5 - current > 0f == num8 > num5) {
			num8 = num5;
			currentVelocity = (num8 - num5) / deltaTime;
		}
		return new float[] { num8, currentVelocity };
	}

	/**
	 * @return {float newValue, float currentVelocity};
	 */
	public static double[] smoothDamp(double current, double target,
			double currentVelocity, double smoothTime, double maxSpeed,
			double deltaTime) {
		smoothTime = Math.max(0.0001f, smoothTime);
		double num = 2f / smoothTime;
		double num2 = num * deltaTime;
		double num3 = 1f / (1f + num2 + 0.48f * num2 * num2 + 0.235f * num2
				* num2 * num2);
		double num4 = current - target;
		double num5 = target;
		double num6 = maxSpeed * smoothTime;
		num4 = MathUtils.clamp(num4, -num6, num6);
		target = current - num4;
		double num7 = (currentVelocity + num * num4) * deltaTime;
		currentVelocity = (currentVelocity - num * num7) * num3;
		double num8 = target + (num4 + num7) * num3;
		if (num5 - current > 0f == num8 > num5) {
			num8 = num5;
			currentVelocity = (num8 - num5) / deltaTime;
		}
		return new double[] { num8, currentVelocity };
	}

	public static void smoothDamp(Vector3 current, ReadOnlyVector3 target,
			Vector3 currentVelocity, double smoothTime, double maxSpeed,
			double deltaTime) {
		double[] x = smoothDamp(current.getX(), target.getX(),
				currentVelocity.getX(), smoothTime, maxSpeed, deltaTime);
		double[] y = smoothDamp(current.getY(), target.getY(),
				currentVelocity.getY(), smoothTime, maxSpeed, deltaTime);
		double[] z = smoothDamp(current.getZ(), target.getZ(),
				currentVelocity.getZ(), smoothTime, maxSpeed, deltaTime);
		current.set(x[0], y[0], z[0]);
		currentVelocity.set(x[1], y[1], z[1]);
	}

	public static void lerp(Transform xa, Transform xb, double t,
			Transform store) {
		// Rotation
		Quaternion rotA = Quaternion.fetchTempInstance();
		Quaternion rotB = Quaternion.fetchTempInstance();

		rotA.fromRotationMatrix(xa.getMatrix());
		rotB.fromRotationMatrix(xb.getMatrix());

		Quaternion rot = Quaternion.fetchTempInstance();
		Quaternion.slerp(rotA, rotB, t, rot);
		store.setRotation(rot);

		Quaternion.releaseTempInstance(rotA);
		Quaternion.releaseTempInstance(rotB);

		// Translation
		Vector3 pos = Vector3.fetchTempInstance();

		Vector3.lerp(xa.getTranslation(), xb.getTranslation(), t, pos);
		store.setTranslation(pos);

		Vector3.releaseTempInstance(pos);

	}

}
