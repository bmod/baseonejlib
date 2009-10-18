package game;

import game.util.Util;

import com.baseoneonline.java.astar.TileGraph;
import com.baseoneonline.java.math.Vec2i;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * Represents a 2d board
 */
public class Board {

	private final Spatial surface;

	public float tileSize = 1;

	private final TileGraph graph;

	public Board(final Spatial surface, final TileGraph graph) {
		this.surface = surface;
		this.graph = graph;
	}

	public TileGraph getGraph() {
		return graph;
	}

	/**
	 * Place {@link Spatial} on board at center of tile
	 * 
	 * @param spatial
	 * @param pos
	 */
	public Vector3f getSurfPos(final Vec2i pos) {
		final Vector2f pos2d = toReal(pos);
		final Ray ray = new Ray(new Vector3f(pos2d.x, 100, pos2d.y),
				new Vector3f(0, -1, 0));
		final Vector3f surfPos = new Vector3f();
		if (Util.findSurfPos(ray, surface, surfPos)) return surfPos;
		return new Vector3f(pos2d.x, 0, pos2d.y);
	}

	public Vector3f getSurfPos(final Vector2f pos) {
		final Ray ray = new Ray(new Vector3f(pos.x, 100, pos.y), new Vector3f(
				0, -1, 0));
		final Vector3f surfPos = new Vector3f();
		if (Util.findSurfPos(ray, surface, surfPos)) { return surfPos; }
		return new Vector3f(pos.x, 0, pos.y);
	}

	public Vector2f toReal(final Vec2i v) {
		return new Vector2f(v.x * tileSize + tileSize / 2, v.y * tileSize
				+ tileSize / 2);
	}

	public Vec2i toBoard(final Vector3f vec) {
		return new Vec2i((int) Math.floor(vec.x / tileSize), (int) Math
				.floor(vec.z / tileSize));
	}

}
