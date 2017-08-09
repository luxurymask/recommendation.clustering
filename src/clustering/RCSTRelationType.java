package clustering;

public class RCSTRelationType {
	/**
	 * RCST关系类型字符串。
	 */
	public String type;
	
	/**
	 * 父子关系常量字符串。
	 */
	public static final String PARENTED_TYPE = "parented";
	
	/**
	 * 兄弟关系常量字符串。
	 */
	public static final String BROTHERHOOD_TYPE = "brotherhood";
	
	/**
	 * 其他关系常量字符串。
	 */
	public static final String OTHER_TYPE = "other"; 
	
	/**
	 * 构造函数。
	 * @param source
	 * @param target
	 */
	public RCSTRelationType(RCSTNode source, RCSTNode target){
		if(source.isFatherOf(target)){
			type = RCSTRelationType.PARENTED_TYPE;
		}else if(source.isOlderBrotherOf(target)){
			type = RCSTRelationType.BROTHERHOOD_TYPE;
		}else{
			type = RCSTRelationType.OTHER_TYPE;
		}
	}
	
	/**
	 * 判断当前关系是否是父子关系。
	 * @return
	 */
	public boolean isParentedType(){
		return type.equals(RCSTRelationType.PARENTED_TYPE);
	}
	
	/**
	 * 判断当前关系是否是兄弟关系。
	 * @return
	 */
	public boolean isBrotherboodType(){
		return type.equals(RCSTRelationType.BROTHERHOOD_TYPE);
	}
	
	/**
	 * 判断当前关系是否是其他关系。
	 * @return
	 */
	public boolean isOtherType(){
		return type.equals(RCSTRelationType.OTHER_TYPE);
	}
}
