package clustering;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 子任务划分。
 * 
 * @author liuxl
 */
public class TimeTreeNodeClusteror {
	/**
	 * RCST节点id-RCST节点Map（所有节点）
	 */
	Map<String, RCSTNode> idNodeMap = new HashMap<String, RCSTNode>();
	
	/**
	 * RCST节点id-RCST节点Map（查询节点）
	 */
	Map<String, RCSTNode> queryIdNodeMap = new HashMap<String, RCSTNode>();
	
	/**
	 * RCST节点id-RCST节点Map（点击节点）
	 */
	Map<String, RCSTNode> clickIdNodeMap = new HashMap<String, RCSTNode>();

	List<RCSTRelation> parentedRelationList = new ArrayList<RCSTRelation>();
	List<RCSTRelation> brotherhoodRelationList = new ArrayList<RCSTRelation>();

	public TimeTreeNodeClusteror(String graphMLFilePath) {
		File graphMLFile = new File(graphMLFilePath);

	}

	/**
	 * 从graphML中读取所有节点。
	 * 
	 * @param graphMLFile
	 *            探索图文件。
	 * @return 所有节点个数。
	 */
	public int getNodes(File graphMLFile) {
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
					RCSTType rcstType = new RCSTType(nodeType);
					String nodeId = ((Element) node).getAttribute("id");
					long timestamp = Long.parseLong(((Element) node).getAttribute("time"));
					String nodeContent;
					if(rcstType.isQueryType()){
						nodeContent = ((Element) node).getElementsByTagName("queryText").item(0).getTextContent();
						queryIdNodeMap.put(nodeId, new RCSTNode(nodeId, rcstType, nodeContent, timestamp));
					}else if(rcstType.isClickType()){
						nodeContent = ((Element) node).getElementsByTagName("title").item(0).getTextContent();
						clickIdNodeMap.put(nodeId, new RCSTNode(nodeId, rcstType, nodeContent, timestamp));
					}else{
						
					}
				}
			}

			// 读取边信息。
			NodeList edgeList = doc.getElementsByTagName("edge");
			for (int i = 0; i < edgeList.getLength(); i++) {
				Node edge = edgeList.item(i);
				if (edge instanceof Element) {
					String sourceId = ((Element) edge).getAttribute("source");
					String targetId = ((Element) edge).getAttribute("target");
					RCSTNode sourceNode = idNodeMap.get(sourceId);
					RCSTNode targetNode = idNodeMap.get(targetId);
					targetNode.setFather(sourceNode);
					sourceNode.addChild(targetNode);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return idNodeMap.size();
	}
}
