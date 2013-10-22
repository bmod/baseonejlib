package test;

import java.util.logging.Logger;

import com.ardor3d.annotation.MainThread;
import com.ardor3d.framework.Canvas;
import com.ardor3d.image.Texture;
import com.ardor3d.image.Texture.MagnificationFilter;
import com.ardor3d.image.Texture.MinificationFilter;
import com.ardor3d.input.Key;
import com.ardor3d.input.logical.InputTrigger;
import com.ardor3d.input.logical.KeyPressedCondition;
import com.ardor3d.input.logical.TriggerAction;
import com.ardor3d.input.logical.TwoInputStates;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyVector3;
import com.ardor3d.renderer.state.TextureState;
import com.ardor3d.renderer.state.WireframeState;
import com.ardor3d.scenegraph.Mesh;
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

	int w = 7;
	int h = 7;

	private Patch patch;

	@Override
	protected void init() {
		final EditorCameraController ctrl = new EditorCameraController(
				logicalLayer);
		ctrl.update(camera);

		final Vector3[] pts = { new Vector3(0, 0, -1), new Vector3(0, .3, 0),
				new Vector3(0, -.3, 1), new Vector3(0, 0, 2) };

		patch = new Patch(1, pts);
		final Mesh patchMesh = patch.getMesh();

		final TextureState ts = new TextureState();
		final Texture checkerTex = TextureManager.load("assets/checker.png",
				MinificationFilter.Trilinear, false);
		checkerTex.setMagnificationFilter(MagnificationFilter.NearestNeighbor);
		ts.setTexture(checkerTex);
		patchMesh.setRenderState(ts);

		final WireframeState ws = new WireframeState();
		ws.setEnabled(true);
		patchMesh.setRenderState(ws);
		// patchMesh.getSceneHints().setLightCombineMode(LightCombineMode.Off);
		patchMesh.setDefaultColor(ColorRGBA.CYAN);

		root.attachChild(patchMesh);

		logicalLayer.registerTrigger(new InputTrigger(new KeyPressedCondition(
				Key.W), new TriggerAction() {

			@Override
			@MainThread
			public void perform(final Canvas source,
					final TwoInputStates inputStates, final double tpf) {
				ws.setEnabled(!ws.isEnabled());
				Logger.getLogger(getClass().getName()).info(
						"Wireframe: " + ws.isEnabled());
			}
		}));

	}

	@Override
	protected void update(final ReadOnlyTimer timer) {
	}

}

class Patch {

	private final int width;
	private final ReadOnlyVector3[] centers;

	private SplineSurfaceMesh mesh;
	private BSplineSurface3 surf;

	public Patch(final int width, final ReadOnlyVector3[] centers) {
		this.width = width;
		this.centers = centers;
		createSurface();
	}

	public Mesh getMesh() {
		if (null == mesh) {
			final int subDivs = 4;
			mesh = new SplineSurfaceMesh(surf, width * subDivs, centers.length
					* subDivs);
			mesh.setTextureRepeat(width, centers.length);
		}
		return mesh;
	}

	private void createSurface() {
		final int numVertsU = width + 3;
		final int numVertsV = centers.length + 3;

		final Vector3 left = new Vector3();

		final Vector3[][] vtc = new Vector3[numVertsU][];
		for (int ku = 0; ku < numVertsU; ku++) {
			vtc[ku] = new Vector3[numVertsV];
			for (int kv = 0; kv < numVertsV; kv++) {
				final Vector3 pt = new Vector3(ku, 0, kv);
				vtc[ku][kv] = pt;
			}
		}

		surf = new BSplineSurface3(vtc);

	}
}