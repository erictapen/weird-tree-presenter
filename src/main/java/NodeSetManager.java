
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;


public class NodeSetManager {
	private HashMap<Vector<Integer>, HashSet<GraphNode>> map= new HashMap<Vector<Integer>, HashSet<GraphNode>>();
	
	/** This is the length of a grid cell, in world units.
	 * 
	 */
	private float gridsize = 0.1f;
	private float maxNodesize = 0.1f;

	public NodeSetManager(GraphNode root) {
		this.addNodes(root.getWholeTree());
		System.out.println("Tree cached. There are " + this.map.size() + " Nodes in memory.");
	}

	public HashSet<GraphNode> getRenderableNodes(float xPos, float yPos, float xSize, float ySize) {
		HashSet<GraphNode> res = new HashSet<GraphNode>();
		for (int i = (int)(-xSize*gridsize); i < (int)(xSize*gridsize); i++) {
			for (int j = (int)(-ySize*gridsize); j < (int)(ySize*gridsize); j++) {
				Vector<Integer> vect = new Vector<Integer>(2);
				vect.add(0, new Integer(i));
				vect.add(1, new Integer(j));
				HashSet<GraphNode> nodes = this.map.get(vect);
				if(nodes==null) continue;
				for(GraphNode x : nodes) {
					if(x.getRadius() > xSize*this.maxNodesize) res.add(x);
				}
			}
		}
		return res;
	}
	
	/** Add nodes to the managed Set.
	 * @param addedNodes
	 */
	public void addNodes(HashSet<GraphNode> addedNodes) {
		for(GraphNode node : addedNodes) {
			int radius = (int)Math.ceil(node.getRadius()/this.gridsize); //nodesize in the grids scale
			if(radius==0) System.out.println("Radius ist null!");
			for(int x=-radius-1; x<=radius+1; x++) {
				for(int y=-radius-1; y<=radius+1; y++) {
					if(Math.sqrt(x*x + y*y) > radius+1) continue;
					Vector<Integer> vect = new Vector<Integer>(2);
					vect.add(0, new Integer((int)Math.floor(node.getxPos()/this.gridsize)) + x);
					vect.add(1, new Integer((int)Math.floor(node.getyPos()/this.gridsize)) + y);
					HashSet<GraphNode> temp = this.map.get(vect);
					if(temp!=null) {
						temp.add(node);
					} else {
						HashSet<GraphNode> newList = new HashSet<GraphNode>();
						newList.add(node);
						this.map.put(vect, newList);
					}
				}
			}
		}
	}

	public void setGridsize(float gridsize) {
		this.gridsize = gridsize;
	}

}
