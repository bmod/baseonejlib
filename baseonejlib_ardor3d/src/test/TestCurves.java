package test;

import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector2;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.IndexMode;
import com.ardor3d.scenegraph.Line;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.hint.LightCombineMode;
import com.ardor3d.scenegraph.shape.AxisRods;
import com.ardor3d.scenegraph.shape.Box;
import com.ardor3d.util.ReadOnlyTimer;
import com.baseoneonline.java.math.Curve;
import com.baseoneonline.jlib.ardor3d.ArdorUtil;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.controllers.CurveSpatialController;
import com.baseoneonline.jlib.ardor3d.math.BSpline3;
import com.baseoneonline.jlib.ardor3d.math.CatmullRom3;
import com.baseoneonline.jlib.ardor3d.math.Curve3;
import com.baseoneonline.jlib.ardor3d.math.Nurbs3;

public class TestCurves extends GameBase
{
	public static void main(String[] args)
	{
		TestCurves app = new TestCurves();
		app.start(800, 600, false);
	}

	@Override
	protected void init()
	{
		double sx = 1;
		double sy = .5;
		Vector3[] points = { new Vector3(-sx, -sy, 0), new Vector3(-sx, sy, 0),
				new Vector3(0, sy, 0), new Vector3(0, -sy, 0),
				new Vector3(sx, -sy, 0), new Vector3(sx, sy, 0) };

		CurveNode cvNode;
		Curve3 cv;

		// Open

		cv = new BSpline3(points);
		cv.setMode(Curve.OPEN);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(-3, -2, 0);
		root.attachChild(cvNode);

		cv = new Nurbs3(points);
		cv.setMode(Curve.OPEN);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(0, -2, 0);
		root.attachChild(cvNode);

		cv = new CatmullRom3(points);
		cv.setMode(Curve.OPEN);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(3, -2, 0);
		root.attachChild(cvNode);

		// Clamped

		cv = new BSpline3(points);
		cv.setMode(Curve.CLAMP);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(-3, 0, 0);
		root.attachChild(cvNode);

		cv = new Nurbs3(points);
		cv.setMode(Curve.CLAMP);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(0, 0, 0);
		root.attachChild(cvNode);

		cv = new CatmullRom3(points);
		cv.setMode(Curve.CLAMP);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(3, 0, 0);
		root.attachChild(cvNode);

		// Closed

		cv = new BSpline3(points);
		cv.setMode(Curve.LOOP);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(-3, 2, 0);
		root.attachChild(cvNode);

		cv = new Nurbs3(points);
		cv.setMode(Curve.LOOP);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(0, 2, 0);
		root.attachChild(cvNode);

		cv = new CatmullRom3(points);
		cv.setMode(Curve.LOOP);
		cv.setDefaultNormal(Vector3.UNIT_Z);
		cvNode = new CurveNode(points, cv);
		cvNode.setTranslation(3, 2, 0);
		root.attachChild(cvNode);

		camera.setLocation(0, 0, 7);
		camera.setFrustumPerspective(50, getDisplayRatio(), .1, 100);
		camera.lookAt(Vector3.ZERO, Vector3.UNIT_Y);
		root.getSceneHints().setLightCombineMode(LightCombineMode.Off);

	}

	@Override
	protected void update(ReadOnlyTimer timer)
	{

	}

}

class CurveNode extends Node
{
	private final Line line;
	private final Line curveLine;
	private final Spatial[] nodes;
	private final int samples = 40;
	private final Curve<ReadOnlyVector3, Vector3> curve;

	public CurveNode(ReadOnlyVector3[] points, Curve3 curve)
	{
		this.curve = curve;
		line = createLine(points, ColorRGBA.DARK_GRAY);
		line.getMeshData().setIndexMode(IndexMode.LineStrip);

		attachChild(line);

		ReadOnlyVector3[] curvePoints = new ReadOnlyVector3[samples + 1];
		for (int i = 0; i <= samples; i++)
		{
			double t = (float) i / (float) samples;
			Vector3 v = new Vector3();
			curve.getPoint(t, v);
			curvePoints[i] = v;
		}

		curveLine = createLine(curvePoints, ColorRGBA.YELLOW);
		curveLine.getMeshData().setIndexMode(IndexMode.LineStrip);
		attachChild(curveLine);

		nodes = new Spatial[points.length];
		double boxSize = .03;
		for (int i = 0; i < points.length; i++)
		{
			Box b = new Box("Box" + i, Vector3.ZERO, boxSize, boxSize, boxSize);
			b.setDefaultColor(ColorRGBA.LIGHT_GRAY);
			b.setTranslation(points[i]);
			attachChild(b);
			nodes[i] = b;
		}

		boxSize = .06;
		Spatial b = new Box("Dot", Vector3.ZERO, boxSize, boxSize, boxSize);
		b = new AxisRods("Axis", true, .3);
		// b.setDefaultColor(ColorRGBA.ORANGE);
		b.addController(new CurveSpatialController(curve, .1));
		attachChild(b);
	}

	public Curve<ReadOnlyVector3, Vector3> getCurve()
	{
		return curve;
	}

	private Line createLine(ReadOnlyVector3[] points, ReadOnlyColorRGBA col)
	{
		return new Line("Line", points, ArdorUtil.createArray(Vector3.UNIT_Z,
				points.length), ArdorUtil.createArray(col, points.length),
				ArdorUtil.createArray(new Vector2(), points.length));
	}

}