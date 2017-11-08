package clustering2;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Tree {
	TreeNode root;

	Map<String, TreeNode> idNodeMap = new HashMap<>();
	
	public Tree(File graphMLFile) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(graphMLFile);
			// 读取节点信息。
			NodeList nodeList = doc.getElementsByTagName("node");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					String nodeType = ((Element) node).getAttribute("type");
					String type;
					String nodeContent;
					if (nodeType.equals("1")) {
						type = "query";
						nodeContent = ((Element) node).getElementsByTagName("queryText").item(0).getTextContent();
					} else {
						type = "click";
						nodeContent = ((Element) node).getElementsByTagName("title").item(0).getTextContent();
					}

					String nodeId = ((Element) node).getAttribute("id");
					String time = ((Element) node).getElementsByTagName("time").item(0).getTextContent();
					long timestamp = Long.parseLong(time);

					idNodeMap.put(nodeId, new TreeNode(nodeId, type, nodeContent, timestamp));
				}
			}

			// 读取边信息。
			NodeList edgeList = doc.getElementsByTagName("edge");
			for (int i = 0; i < edgeList.getLength(); i++) {
				Node edge = edgeList.item(i);
				if (edge instanceof Element) {
					String sourceId = ((Element) edge).getAttribute("source");
					String targetId = ((Element) edge).getAttribute("target");
					TreeNode sourceNode = idNodeMap.get(sourceId);
					TreeNode targetNode = idNodeMap.get(targetId);
					if (targetNode.setFather(sourceNode) != true)
						throw new Exception(targetId + "(" + targetNode.getContent() + ") already has a father : "
								+ sourceId + "(" + sourceNode.getContent() + ").");
					if (sourceNode.addChild(targetNode) != true)
						throw new Exception(sourceId + "(" + sourceNode.getContent() + ") already has a child : "
								+ targetId + "(" + targetNode.getContent() + ").");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		for (Map.Entry<String, TreeNode> entry : idNodeMap.entrySet()) {
			TreeNode node = entry.getValue();
			List<TreeNode> childrenList = node.getChildrenList();
			if(childrenList != null){
				Collections.sort(childrenList, new Comparator<TreeNode>() {
					@Override
					public int compare(TreeNode node1, TreeNode node2) {
						if (node1.getTimestamp() >= node2.getTimestamp())
							return 1;
						else
							return -1;
					}
				});
				childrenList.get(0).setToBeFirstChild();
			}
		}

		root = idNodeMap.get("0");
		
		Map<Integer, Integer> layerCurrentLastMap = new HashMap<>();
		dfsSetRowAndColumn(root, 0, 0, layerCurrentLastMap);
		
		//TODO 调整
		
	}
	
	public void dfsSetRowAndColumn(TreeNode root, int row, int column, Map<Integer, Integer> layerCurrentLastMap){
		if(!root.hasChild()){
			root.setRow(row);
			root.setColumn(column);
			layerCurrentLastMap.put(column, row);
		}else{
			int nextLayerCurrentLast;
			if(!layerCurrentLastMap.containsKey(column + 1) || (nextLayerCurrentLast = layerCurrentLastMap.get(column + 1)) < row){
				root.setRow(row);
				root.setColumn(column);
				layerCurrentLastMap.put(column, row);
			}else{
				row = nextLayerCurrentLast + 1;
				root.setRow(row);
				root.setColumn(column);
				layerCurrentLastMap.put(column, row);
				resetFathers(root, row, layerCurrentLastMap);
			}
			List<TreeNode> childrenList = root.getChildrenList();
			for(int i = 0;i < childrenList.size();i++){
				TreeNode child = childrenList.get(i);
				int newRow;
				if(!layerCurrentLastMap.containsKey(column + 1)){
					newRow = row;
				}else{
					newRow = layerCurrentLastMap.get(column + 1) + 1;
				}
				dfsSetRowAndColumn(child, newRow, column + 1, layerCurrentLastMap);
			}
		}
	}
	
	public void resetFathers(TreeNode child, int row, Map<Integer, Integer> layerCurrentLastMap){
		while(child.isFirstChildOfFather()){
			TreeNode father = child.getFather();
			int fatherColumn = father.getColumn();
			father.setRow(row);
			layerCurrentLastMap.put(fatherColumn, row);
			child = father;
		}
	}
}
