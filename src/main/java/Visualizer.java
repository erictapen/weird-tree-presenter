import java.util.HashSet;

import processing.core.PApplet;


@SuppressWarnings("serial")
public class Visualizer extends PApplet{

	
	private NodeSetManager mngr;
	private float xCenter;
	private float yCenter;
	private float xSize;
	private float ySize;
	
	public void setup() {
		this.mngr = this.initManager();
	}
	
	public void draw() {
		HashSet<GraphNode> toRender = mngr.getRenderableNodes(this.xCenter, this.yCenter, this.xSize, this.ySize);
		for(GraphNode x : toRender) {
			ellipse(x.getxPos()*xSize*width,
					x.getyPos()*ySize*height,
					x.getRadius()*xSize,
					x.getRadius()*xSize);
		}
	}
	
	public NodeSetManager initManager() {
		String errorMsg = "The only valid configuration is of the form \n"
				+ "path/to/DOTfile --root-caption ROOTCAPTION\n"
				+ "Will abort.";
		if(args.length!=2) {
			System.out.println(errorMsg);
			exit();
			return null;
		}
		GraphNode root;
		String rootCaption = "";
		if(args[1] == "-rc" || args[1] == "--root-caption") {
			rootCaption = args[2];
		} else {
			System.out.println(errorMsg);
			exit();
			return null;
		}
		root = SortedGraph.importFile(args[0], rootCaption);
		NodeSetManager mngr = new NodeSetManager(root);
		return mngr;
	}
	
}
