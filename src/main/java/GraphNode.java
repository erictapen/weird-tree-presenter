import java.util.ArrayList;


public class GraphNode {
	private String caption;
	private GraphNode parent;
	private ArrayList<GraphNode> children;
	private float size;
	private float posx;
	private float posy;
	
	public String getCaption() {
		return caption;
	}
	public GraphNode getParent() {
		return parent;
	}
	public ArrayList<GraphNode> getChildren() {
		return children;
	}
	public float getSize() {
		return size;
	}
	public float getPosx() {
		return posx;
	}
	public float getPosy() {
		return posy;
	}
	
	
}
