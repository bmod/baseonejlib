package game;


import com.baseoneonline.java.math.Vec2i;
import com.jme.input.InputHandler;
import com.jme.input.MouseInput;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

public class SurfMouseInputHandler extends InputHandler {

	private final Spatial surface;
	// Mouse position on grid
	private final Vec2i gridPos = new Vec2i();
	// Mouse grid position on plane
	private final Vector3f surfPos = new Vector3f();
	// Ray from mouse into scene
	private final Ray screenRay = new Ray();

	private final Board board;
	private boolean mouseOnSurface = false;

	private final Vector2f screenPos = new Vector2f();

	protected TrianglePickResults results = new TrianglePickResults();

	public SurfMouseInputHandler(final Spatial surf, final Board board) {
		surface = surf;
		this.board = board;
	}

	public Vec2i getGridPos() {
		return gridPos;
	}

	@Override
	public void update(final float t) {
		if (!isEnabled()) return;

		final MouseInput mouse = MouseInput.get();

		screenPos.x = mouse.getXAbsolute();
		screenPos.y = mouse.getYAbsolute();

		// Update ray from mouse position to surface
		screenRay.origin = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(screenPos, 0);
		screenRay.direction = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(screenPos, 1).subtractLocal(
						screenRay.origin).normalizeLocal();
		// Find and store 3d point under mouse cursor
		mouseOnSurface = Util.findSurfPos(screenRay, surface, surfPos);
		// Store grid position
		if (mouseOnSurface) {
			gridPos.set(board.toBoard(surfPos));
		}

		super.update(t);
	}

	public boolean isMouseOnSurface() {
		return mouseOnSurface;
	}

}
