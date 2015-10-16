
import java.util.ArrayList;

public class GraphNode {
	private String caption;
	private GraphNode parent;
	private ArrayList<GraphNode> children = new ArrayList<GraphNode>();

	private float xPos;
	private float yPos;
	private float radius = 0.0f;
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public GraphNode(String caption) {
		super();
		this.caption = caption;
	}

	public GraphNode getParent() {
		return parent;
	}

	public void setParent(GraphNode parent) {
		this.parent = parent;
	}

	public String getCaption() {
		return caption;
	}

	public ArrayList<GraphNode> getChildren() {
		return children;
	}
	
	public void addChild(GraphNode child) {
		this.children.add(child);
	}
	
	public float getxPos() {
		return xPos;
	}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public float getyPos() {
		return yPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	@Override
	public String toString() {
		return "GraphNode [caption=" + caption + ", parent=" + parent
				+ ", children=" + children.size() + ", xPos=" + xPos + ", yPos=" + yPos
				+ ", radius=" + radius + "]";
	}
}
