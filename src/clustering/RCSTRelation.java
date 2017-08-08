package clustering;

public class RCSTRelation {
	public RCSTNode sourceNode;
	public RCSTNode targetNode;
	public RCSTRelationType relationType;
	
	public RCSTRelation(RCSTNode sourceNode, RCSTNode targetNode){
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		relationType = new RCSTRelationType(sourceNode, targetNode);
	}
}
