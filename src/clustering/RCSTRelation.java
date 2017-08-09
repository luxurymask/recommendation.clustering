package clustering;

public class RCSTRelation {
	public RCSTNode sourceNode;
	public RCSTNode targetNode;
	public RCSTRelationType relationType;

	public RCSTRelation(RCSTNode sourceNode, RCSTNode targetNode) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		relationType = new RCSTRelationType(sourceNode, targetNode);
	}

	public boolean isParentedRelation() {
		return relationType.isParentedType();
	}

	public boolean isBrotherhoodRelation() {
		return relationType.isBrotherboodType();
	}

	public boolean isOtherRelation() {
		return relationType.isOtherType();
	}

	public static boolean isParentedRelation(RCSTNode sourceNode, RCSTNode targetNode) {
		return sourceNode.isFatherOf(targetNode);
	}

	public static boolean isBrotherhoodRelation(RCSTNode sourceNode, RCSTNode targetNode) {
		return sourceNode.isYoungerBrotherOf(targetNode);
	}

	public static boolean isOtherRelation(RCSTNode sourceNode, RCSTNode targetNode) {
		return !RCSTRelation.isParentedRelation(sourceNode, targetNode)
				&& !RCSTRelation.isBrotherhoodRelation(sourceNode, targetNode);
	}
}
