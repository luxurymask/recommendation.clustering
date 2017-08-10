package clustering;

import java.util.ArrayList;
import java.util.List;

public class RCSTNode {
	/**
	 * 节点所在探索图ID。
	 */
	public String graphId;
	
	/**
	 * 当前节点ID。
	 */
	public String nodeId;
	
	/**
	 * RCST类型。
	 */
	public RCSTType rcstType;
	
	/**
	 * RCST内容。如果当前节点是查询节点，则RCST内容为查询关键词；如果节点是点击节点，则RCST内容为点击的内容标题。
	 */
	public String rcstContent;
	
	/**
	 * 当前节点生成的时间戳。
	 */
	public long timestamp;
	
	/**
	 * 当前节点的父节点。
	 */
	public RCSTNode father;
	
	/**
	 * 当前节点的子节点集合。
	 */
	public List<RCSTNode> childrenList;
	
	/**
	 * 以当前节点对象为根节点的子树宽度。
	 * 当前节点为叶子节点，子树宽度为1。
	 */
	public int subTreeWidth;
	
	/**
	 * 构造函数（不设置父节点以及子节点）。
	 * @param nodeId
	 * @param rcstType
	 * @param rcstContent
	 * @param timestamp
	 */
	public RCSTNode(String nodeId, RCSTType rcstType, String rcstContent, long timestamp){
		this.nodeId = nodeId;
		this.rcstType = rcstType;
		this.rcstContent = rcstContent;
		this.timestamp = timestamp;
		childrenList = new ArrayList<RCSTNode>();
	}
	
	/**
	 * 为当前对象添加子节点。
	 * @param child 子节点。
	 * @return 添加是否成功。如果当前对象已添加过该子节点，则添加失败。
	 */
	public boolean addChild(RCSTNode child){
		if(childrenList.contains(child)){
			return false;
		}
		childrenList.add(child);
		return true;
	}
	
	/**
	 * 为当前对象设置父节点。
	 * @param father 父节点。
	 * @return 设置是否成功。如果当前对象已有父节点，则设置失败。
	 */
	public boolean setFather(RCSTNode father){
		if(this.father == null){
			this.father = father;
			return true;
		}
		return false;
	}
	
	/**
	 * 判断当前对象是否是待判断节点的父节点。
	 * @param rcstNode 待判断节点。
	 * @return 是（当前对象是rcstNode的父节点）／否（当前对象不是rcstNode的父节点）
	 */
	public boolean isFatherOf(RCSTNode rcstNode){
		if(childrenList.contains(rcstNode)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是当前对象的父节点。
	 * @param rcstNode 待判断节点。
	 * @return 是（待判断节点是当前对象的父节点）／否（待判断节点不是当前对象的父节点）
	 */
	public boolean isChildOf(RCSTNode rcstNode){
		if(father == rcstNode){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断当前对象是否是待判断节点的兄弟节点。
	 * @param rcstNode 待判断节点。
	 * @return 是（待判断节点是当前对象的兄弟节点）／否（待判断节点不是当前对象的兄弟节点）
	 */
	public boolean isBrotherOf(RCSTNode rcstNode){
		return this.father.childrenList.contains(rcstNode);
	}
	
	/**
	 * 判断当前对象是否是待判断节点的兄节点。
	 * @param rcstNode 待判断节点。
	 * @return 是（待判断节点是当前对象的兄节点）／否（待判断节点不是当前对象的兄节点）
	 */
	public boolean isOlderBrotherOf(RCSTNode rcstNode){
		if(this.isBrotherOf(rcstNode) && this.timestamp < rcstNode.timestamp){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断当前对象是否是待判断节点的弟节点。
	 * @param rcstNode 待判断节点。
	 * @return 是（待判断节点是当前对象的弟节点）／否（待判断节点不是当前对象的弟节点）
	 */
	public boolean isYoungerBrotherOf(RCSTNode rcstNode){
		if(this.isBrotherOf(rcstNode) && this.timestamp > rcstNode.timestamp){
			return true;
		}
		return false;
	}
	
	public void setSubTreeWidth(int width){
		this.subTreeWidth = width;
	}
	
	public int width(){
		return subTreeWidth;
	}
}
