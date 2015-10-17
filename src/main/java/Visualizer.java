import java.util.HashSet;

import processing.core.PApplet;


@SuppressWarnings("serial")
public class Visualizer extends PApplet{

	
	private NodeSetManager mngr;
	
	//all of this is in world units
	private float xCenter = 0.0f;
	private float yCenter = 0.0f;
	private float xSize = 1.0f;
	private float ySize = 1.0f;
	
	private HashSet<GraphNode> toRender;
	private GraphNode follow;
	
	public void setup() {
		size(800, 600);
		background(0);
		stroke(255);
		noFill();
		this.mngr = this.initManager();
	}
	
	public void draw() {
		clear();
		toRender = mngr.getRenderableNodes(this.xCenter, this.yCenter, this.xSize, this.ySize);
		System.out.println("drawing " + toRender.size() + " nodes at " + xCenter + "," + yCenter);
		for(GraphNode x : toRender) {
			ellipse(((x.getxPos()-xCenter)/xSize) * width + width/2.0f,
					((x.getyPos()-yCenter)/ySize) * height + height/2.0f,
					(x.getRadius()/xSize)*width*2.0f,
					(x.getRadius()/xSize)*width*2.0f);
		}
		xSize += 0.01;
		ySize += 0.01;
	}
	
	public NodeSetManager initManager() {
		String errorMsg = "The only valid configuration is of the form \n"
				+ "path/to/DOTfile --root-caption ROOTCAPTION\n"
				+ "Will abort.";
		if(args.length!=3) {
			System.out.println(errorMsg);
			exit();
			return null;
		}
		GraphNode root;
		String rootCaption = "";
		if(args[1].equals("-rc") || args[1].equals("--root-caption")) {
			rootCaption = args[2];
		} else {
			System.out.println(errorMsg);
			System.out.println(args[0]);
			System.out.println(args[1]);
			System.out.println(args[2]);
			exit();
			return null;
		}
		root = SortedGraph.importFile(args[0], rootCaption);
		NodeSetManager mngr = new NodeSetManager(root);
		return mngr;
	}
	
}
