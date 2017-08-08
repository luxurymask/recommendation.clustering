package clustering;

import java.util.List;

public class RCSTNode {
	public String graphId;
	public String nodeId;
	public RCSTType rcstType;
	public String rcstContent;
	public String timestamp;
	public RCSTNode father;
	public List<RCSTNode> childrenList;
}
