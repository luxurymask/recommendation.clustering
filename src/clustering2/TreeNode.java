package clustering2;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	private String nodeId;
	
	private String type;
	
	private String content;
	
	private long timestamp;
	
	private TreeNode father;

	private List<TreeNode> childrenList;
	
	private int row;
	
	private int column;
	
	private boolean isFirstChildOfFather;
	
	public TreeNode(String nodeId, String type, String content, long timestamp){
		this.nodeId = nodeId;
		this.type = type;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	public String getNodeId(){
		return nodeId;
	}
	
	public String getType(){
		return type;
	}
	
	public String getContent(){
		return content;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
	
	public TreeNode getFather(){
		return father;
	}
	
	public boolean setFather(TreeNode father){
		if(getFather() == null){
			this.father = father;
			return true;
		}
		return false;
	}
	
	public List<TreeNode> getChildrenList(){
		return childrenList;
	}
	
	public boolean addChild(TreeNode child){
		if(childrenList == null){
			childrenList = new ArrayList<>();
		}else if(childrenList.contains(child)){
			return false;
		}
		childrenList.add(child);
		return true;
	}
	
	public int getRow(){
		return row;
	}
	
	public void setRow(int row){
		this.row = row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public void setColumn(int column){
		this.column = column;
	}
	
	public boolean hasChild(){
		return childrenList != null;
	}
	
	public void setToBeFirstChild(){
		isFirstChildOfFather = true;
	}
	
	public boolean isFirstChildOfFather(){
		return isFirstChildOfFather;
	}
}
