package game;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class Level {

	public Board board;

	public Node node = new Node("LevelNode");

	public Spatial surface;

	public NavGraph navGraph;

	public LevelResource resource;

	public Level() {}

}
