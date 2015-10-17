import java.util.HashSet;

import processing.core.PApplet;


@SuppressWarnings("serial")
public class Visualizer extends PApplet{

	private final float movSpeed = 0.002f;
	private final float followUntilDistance = 0.002f;
	private final float nodeSize = 2.0f;
	
	private NodeSetManager mngr;
	
	//all of this is in world units
	private float xCenter = 0.0f;
	private float yCenter = 0.0f;
	private float xSize = 1.0f;
	private float ySize = 1.0f;
	private float xGoal = 0.0f;
	private float yGoal = 0.0f;
	private float sizeGoal = 0.0f;
	
	private HashSet<GraphNode> toRender;
	private GraphNode follow;
	private int timer;
	
	public void setup() {
		size(800, 600);
		background(0);
		stroke(255);
		noFill();
		this.mngr = this.initManager();
		this.follow = this.mngr.getRandomNode();
		this.xCenter = this.follow.getxPos();
		this.yCenter = this.follow.getyPos();
	}
	
	public void draw() {
		clear();
		handlePosition();
		toRender = mngr.getRenderableNodes(this.xCenter, this.yCenter, this.xSize, this.ySize);
		System.out.println("drawing " + toRender.size() + " nodes at " + xCenter + "," + yCenter);
		System.out.println("Going to " + this.follow.getCaption());
		for(GraphNode x : toRender) {
			ellipse(((x.getxPos()-xCenter)/xSize) * width + width/2.0f,
					((x.getyPos()-yCenter)/ySize) * height + height/2.0f,
					(x.getRadius()/xSize)*width*2.0f,
					(x.getRadius()/xSize)*width*2.0f);
		}
	}
	
	private NodeSetManager initManager() {
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
	
	private void handlePosition() {
		if(this.follow == null && timer>100) {
			this.follow = this.mngr.getRandomNode();
			timer = 0;
		}
		if(dist(this.follow.getxPos(), this.follow.getyPos(), this.xCenter, this.yCenter) < this.followUntilDistance) {
			this.follow = this.follow.getParent();
			if(this.follow!=null) return;
			this.xGoal = this.follow.getxPos();
			this.yGoal = this.follow.getyPos();
			this.sizeGoal = this.follow.getRadius() * this.nodeSize;
		}
		float rad = atan2(this.xCenter - this.xGoal, this.yCenter - this.yGoal);
		this.xCenter += sin(rad)*this.movSpeed;
		this.yCenter += cos(rad)*this.movSpeed;
		timer++;
	}
	
}
