

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Importfunctionality for .dot-files which are already sorted
 * and are therefore faster readable. There are only .dot-files with a single graph allowed!
 * @author justin
 *
 */
public class SortedGraph {
	private static HashMap<String, GraphNode> nodemap = new HashMap<String, GraphNode>(4000000, (float) 0.75);
	
	/** import a single (!) TreeGraph (!) from file
	 * @param ifile file location
	 * @param rootcaption caption of the desired rootNode
	 * @return the rootNode
	 */
	public static GraphNode importFile(String ifile, String rootcaption) {
		System.out.println("Starting sorted DOT import of " + rootcaption + " from " + ifile);
		try (BufferedReader br = new BufferedReader(new FileReader(ifile))) {
			String line;
			int i=0;
			while ((line = br.readLine()) != null) {
				createNodeFromLine(line);
				i++;
				if(i%10000 == 0) System.out.println(i + " Nodes imported.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File \"" + ifile + "\" not found. Abort.");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.out.println("IOException. Abort.");
			e.printStackTrace();
			return null;
		}

		System.out.println("Graph imported. There are " + nodemap.size() + " nodes in memory.");
		GraphNode root = nodemap.get(rootcaption);
		if(root==null) return null;
		ArrayList<GraphNode> togo = new ArrayList<GraphNode>();
		ArrayList<GraphNode> togo2 = new ArrayList<GraphNode>();
		togo.add(root);
		while(!togo.isEmpty()) {
			for(GraphNode x : togo) {
				if(x.getRadius()==0.0) {
				}
				togo2.addAll(x.getChildren());
			}
			togo.clear();
			togo.addAll(togo2);
			togo2.clear();
		}
		System.out.println("Import completed.");
		return root;
	}
	
	/** takes a line and adds the accounting relation into the whole graphset (ArrayList nodes)
	 * @param line
	 */
	private static void createNodeFromLine(String line) {
		line = line.replace("\t", "");  //deletes the tab at the beginning
		String[] str = line.split(" <-- ");
		boolean parentGotAttr = false;
		boolean childGotAttr = false;
		int attrTreeSizeParent = 0;
		float attrPosXParent = 0.0f;
		float attrPosYParent = 0.0f;
		float attrRadiusParent = 0.0f;
		String attrCaptionParent = null;
		if(str[0].contains("[")) {
			attrTreeSizeParent = extractAttributeFromString(str[0], "treeSize", 0);
			attrPosXParent = extractAttributeFromString(str[0], "posx", 0.0f);
			attrPosYParent = extractAttributeFromString(str[0], "posy", 0.0f);
			attrRadiusParent = extractAttributeFromString(str[0], "radius", 0.0f);
			attrCaptionParent = extractAttributeFromString(str[0], "caption", null);
			parentGotAttr = true;
			str[0] = str[0].substring(0, str[0].indexOf(" ["));
		}
		if(attrCaptionParent == null) attrCaptionParent = str[0];
		
		int attrTreeSize = 0;
		float attrPosX = 0.0f;
		float attrPosY = 0.0f;
		float attrRadius = 0.0f;
		String attrCaption = null;
		if(str.length!=2) return;
		if(str[1].contains("[")) {  //Attributes are read out from string
			attrTreeSize = extractAttributeFromString(str[1], "treeSize", 0);
			attrPosX = extractAttributeFromString(str[1], "posx", 0.0f);
			attrPosY = extractAttributeFromString(str[1], "posy", 0.0f);
			attrRadius = extractAttributeFromString(str[1], "radius", 0.0f);
			attrCaption = extractAttributeFromString(str[1], "caption", null);
			childGotAttr = true;
			str[1] = str[1].substring(0, str[1].indexOf(" ["));
		}
		if(attrCaption == null) attrCaption = str[1];
		
		GraphNode parent = nodemap.get(str[0]);
		GraphNode child = nodemap.get(str[1]);
		if(child==null) {
			child = new GraphNode(attrCaption);
			nodemap.put(str[1], child);
		}
		if(parent==null) {
			parent = new GraphNode(attrCaptionParent);
			nodemap.put(str[0], parent);
		}
		child.setParent(parent);
		parent.addChild(child);
		if(parentGotAttr) {
			parent.setxPos(attrPosXParent);
			parent.setyPos(attrPosYParent);
			parent.setRadius(attrRadiusParent);
		}
		if(childGotAttr) {
			child.setxPos(attrPosX);
			child.setyPos(attrPosY);
			child.setRadius(attrRadius);
		}
	}
	
	/** 
	 * @param str An attribute String
	 * @param key The key of the desired attribute
	 * @return The desired double value, or defaultVal if no corresponding value could be found
	 */
	private static Float extractAttributeFromString(String str, String key, float defaultVal) {
		String res = extractAttributeFromString(str, key);
		try{
			return (float) Double.parseDouble(res);	
		} catch(NumberFormatException e) {
			return defaultVal;
		} catch(NullPointerException e) {
			return defaultVal;
		}
	}
	
	/** 
	 * @param str An attribute String
	 * @param key The key of the desired attribute
	 * @return The desired double value, or defaultVal if no corresponding value could be found
	 */
	private static Integer extractAttributeFromString(String str, String key, int defaultVal) {
		String res = extractAttributeFromString(str, key);
		try{
			return Integer.parseInt(res);	
		} catch(NumberFormatException e) {
			return defaultVal;
		} catch(NullPointerException e) {
			return defaultVal;
		}
	}
	
	/** 
	 * @param str An attribute String
	 * @param key The key of the desired attribute
	 * @return The desired double value, or defaultVal if no corresponding value could be found
	 */
	@SuppressWarnings("unused")
	private static Boolean extractAttributeFromString(String str, String key, boolean defaultVal) {
		String res = extractAttributeFromString(str, key);
		if(res==null) return defaultVal;
		if(res == "true") return new Boolean(true); //the Boolean.parseBoolean() function is not suited.
		if(res == "false") return new Boolean(false);
		return defaultVal;
	}
	
	/** 
	 * @param str An attribute String
	 * @param key The key of the desired attribute
	 * @return The desired double value, or defaultVal if no corresponding value could be found
	 */
	private static String extractAttributeFromString(String str, String key, String defaultVal) {
		String res = extractAttributeFromString(str, key);
		if(res==null) return defaultVal;
		return res;
	}
	
	/** 
	 * @param str An attribute String
	 * @param key The key of the desired attribute
	 * @return The desired double value, or defaultVal if no corresponding value could be found
	 */
	private static String extractAttributeFromString(String str, String key) {
		Pattern pattern = Pattern.compile(key + "=\"(.+?)\"");
		Matcher matcher = pattern.matcher(str);
		matcher.find();
		try{
			return matcher.group(1);
		} catch (IllegalStateException e) {
			return null;
		}		
	}

}
