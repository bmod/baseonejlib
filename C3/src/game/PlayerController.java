package game;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.baseoneonline.java.astar.AStar;
import com.baseoneonline.java.math.Vec2i;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;

public class PlayerController extends Controller {

	private final Board board;

	private final Spatial player;

	private float time = 0;
	private final Vector2f pos = new Vector2f();

	private final AStar astar;
	private Vec2i boardPos;
	private ArrayList<Integer> path = new ArrayList<Integer>();

	public PlayerController(final Spatial player, final Board board) {
		this.board = board;
		this.player = player;
		astar = new AStar(board.getGraph());
	}

	public void moveTo(final Vec2i pos) {
		Logger.getLogger(getClass().getName()).info("Moving to: " + pos);
		final NavGraph graph = board.getGraph();
		final int start = graph.getNode(boardPos.x, boardPos.y);
		final int goal = graph.getNode(pos.x, pos.y);
		path = astar.solve(start, goal);
	}

	@Override
	public void update(final float t) {
		time += t;
		final float a = time;
		final float r = 4;
		pos.set(FastMath.cos(a) * r, FastMath.sin(a) * r);
		player.setLocalTranslation(board.getSurfPos(pos));
	}
}
