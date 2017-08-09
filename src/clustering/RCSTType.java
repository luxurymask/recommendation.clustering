package clustering;

public class RCSTType {
	/**
	 * RCST节点类型字符串。
	 */
	private String type;
	
	/**
	 * 查询类型常量字符串。
	 */
	public static final String QUERY_TYPE = "1";
	
	/**
	 * 点击类型常量字符串。
	 */
	public static final String CLICK_TYPE = "2";
	
	/**
	 * 构造函数，设置RCST节点类型字符串。
	 * @param type RCST节点类型字符串。
	 */
	public RCSTType(String type){
		this.type = type;
	}
	
	/**
	 * 判断当前RCST节点类型对象是否是查询类型。
	 * @return
	 */
	public boolean isQueryType(){
		return type.equals(QUERY_TYPE);
	}
	
	/**
	 * 判断当前RCST节点类型对象是否是点击类型。
	 * @return
	 */
	public boolean isClickType(){
		return type.equals(CLICK_TYPE);
	}
}
