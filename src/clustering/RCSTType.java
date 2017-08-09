package clustering;

public class RCSTType {
	private String type;
	public static final String QUERY_TYPE = "1"; 
	public static final String CLICK_TYPE = "2";
	
	public RCSTType(String type){
		this.type = type;
	}
	
	public boolean isQueryType(){
		return type.equals(QUERY_TYPE);
	}
	
	public boolean isClickType(){
		return type.equals(CLICK_TYPE);
	}
}
