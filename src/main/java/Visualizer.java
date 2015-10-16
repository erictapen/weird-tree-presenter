import java.util.HashSet;

import processing.core.PApplet;


@SuppressWarnings("serial")
public class Visualizer extends PApplet{

	
	private NodeSetManager mngr;
	private float xCenter = 0;
	private float yCenter = 0;
	private float xSize = 1024;
	private float ySize = 1024;
	
	public void setup() {
		size(1024, 1024);
		this.mngr = this.initManager();
	}
	
	public void draw() {
		System.out.print("drawing ");
		HashSet<GraphNode> toRender = mngr.getRenderableNodes(this.xCenter, this.yCenter, this.xSize, this.ySize);
		System.out.println(toRender.size() + " nodes.");
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
