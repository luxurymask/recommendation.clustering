package clustering;

public class RCSTRelation {
	/**
	 * 关系源节点。
	 */
	public RCSTNode sourceNode;
	
	/**
	 * 关系目标节点。
	 */
	public RCSTNode targetNode;
	
	/**
	 * RCST关系类型。
	 */
	public RCSTRelationType relationType;

	/**
	 * 构造函数。
	 * @param sourceNode 关系源节点。
	 * @param targetNode 关系目标节点。
	 */
	public RCSTRelation(RCSTNode sourceNode, RCSTNode targetNode) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		relationType = new RCSTRelationType(sourceNode, targetNode);
	}

	/**
	 * 判断该RCST关系是否为父子关系。
	 * @return
	 */
	public boolean isParentedRelation() {
		return relationType.isParentedType();
	}

	/**
	 * 判断该RCST关系是否为兄弟关系。
	 * @return
	 */
	public boolean isBrotherhoodRelation() {
		return relationType.isBrotherboodType();
	}

	/**
	 * 判断该RCST关系是否为其他关系。
	 * @return
	 */
	public boolean isOtherRelation() {
		return relationType.isOtherType();
	}

	/**
	 * 判断参数中的两节点是否是父子关系。
	 * @param sourceNode 源节点。
	 * @param targetNode 目标节点。
	 * @return
	 */
	public static boolean isParentedRelation(RCSTNode sourceNode, RCSTNode targetNode) {
		return sourceNode.isFatherOf(targetNode);
	}
	
	/**
	 * 判断参数中的两节点是否是兄弟关系。
	 * @param sourceNode 源节点。
	 * @param targetNode 目标节点。
	 * @return
	 */
	public static boolean isBrotherhoodRelation(RCSTNode sourceNode, RCSTNode targetNode) {
		return sourceNode.isBrotherOf(targetNode);
	}
	
	/**判断参数中的两节点是否是其他关系。
	 * @param sourceNode 源节点。
	 * @param targetNode 目标节点。
	 * @return
	 */
	public static boolean isOtherRelation(RCSTNode sourceNode, RCSTNode targetNode) {
		return !RCSTRelation.isParentedRelation(sourceNode, targetNode)
				&& !RCSTRelation.isBrotherhoodRelation(sourceNode, targetNode);
	}
}
