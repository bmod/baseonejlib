package test;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.framework.Canvas;
import com.ardor3d.image.Texture.MinificationFilter;
import com.ardor3d.input.Key;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.functions.SimplexNoise;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.shape.Sphere;
import com.ardor3d.util.ReadOnlyTimer;
import com.ardor3d.util.TextureManager;
import com.baseoneonline.jlib.ardor3d.GameBase;
import com.baseoneonline.jlib.ardor3d.controllers.EditorCameraController;
import com.baseoneonline.jlib.ardor3d.math.BSplineSurface3;
import com.baseoneonline.jlib.ardor3d.spatials.SplineSurfaceMesh;

public class TestSplinePatch extends GameBase {
	public static void main(final String[] args) {
		new TestSplinePatch().start();
	}

	private final SimplexNoise simplex = new SimplexNoise();

	int w = 7;
	int h = 7;
	private Vector3[][] vtc;
	private Spatial[][] dots;
	private double time = 0;

	private BSplineSurface3 patch;
	private SplineSurfaceMesh patchMesh;

	@Override
	protected void init() {

		final EditorCameraController ctrl = new EditorCameraController(
				logicalLayer);
		ctrl.update(camera);

		createPoints();

		patch = new BSplineSurface3(vtc);

		patchMesh = new SplineSurfaceMesh(patch, 32, 32);
		patchMesh.setTextureScale(.5, .5);
		patchMesh.setTextureOffset(.25, .25);

		TextureState ts = new TextureState();
		ts.setTexture(TextureManager.load("assets/checker.png",
				MinificationFilter.Trilinear, false));
		patchMesh.setRenderState(ts);

		final WireframeState ws = new WireframeState();
		ws.setEnabled(false);
		patchMesh.setRenderState(ws);
		// patchMesh.getSceneHints().setLightCombineMode(LightCombineMode.Off);
		patchMesh.setDefaultColor(ColorRGBA.CYAN);

		root.attachChild(patchMesh);

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.W), new TriggerAction() {

			@Override
			@MainThread
			public void perform(Canvas source, TwoInputStates inputStates,
					double tpf) {
				ws.setEnabled(!ws.isEnabled());
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.N), new TriggerAction() {

			@Override
			@MainThread
			public void perform(Canvas source, TwoInputStates inputStates,
					double tpf) {
				showNormals = !showNormals;
			}
		}));

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.B), new TriggerAction() {

			@Override
			@MainThread
			public void perform(Canvas source, TwoInputStates inputStates,
					double tpf) {
				showBounds = !showBounds;
			}
		}));
	}

	private void createPoints() {
		vtc = new Vector3[w][];
		dots = new Spatial[w][];
		for (int x = 0; x < w; x++) {
			vtc[x] = new Vector3[h];
			dots[x] = new Spatial[h];
			for (int y = 0; y < h; y++) {
				final Vector3 v = new Vector3(x - ((double) w - 1) / 2, 0, y
						- ((double) h - 1) / 2);
				v.multiplyLocal(2);
				vtc[x][y] = v;

				final Spatial dot = new Sphere("Sphere" + x + "-" + y, 5, 5,
						.1f);
				dots[x][y] = dot;

				dot.setTranslation(v);
				root.attachChild(dot);
			}
		}
	}

	@Override
	protected void update(final ReadOnlyTimer timer) {
		final double speed = .2;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				final Vector3 dot = vtc[x][y];
				final double vx = dot.getX();
				final double vy = dot.getZ();

				final double d = simplex.noise(vx, vy + time * speed);
				dot.set(vx, d, vy);
				dots[x][y].setTranslation(dot);
			}
		}
		patchMesh.rebuild();
		time += timer.getTimePerFrame();
	}

}