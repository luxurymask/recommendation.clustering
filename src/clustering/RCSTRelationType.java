package clustering;

public class RCSTRelationType {
	public String type;
	public static final String PARENTED_TYPE = "parented";
	public static final String BROTHERHOOD_TYPE = "brotherhood";
	public static final String OTHER_TYPE = "other"; 
	
	
	public RCSTRelationType(RCSTNode source, RCSTNode target){
		if(source.isFatherOf(target)){
			type = RCSTRelationType.PARENTED_TYPE;
		}else if(source.isOlderBrotherOf(target)){
			type = RCSTRelationType.BROTHERHOOD_TYPE;
		}else{
			type = RCSTRelationType.OTHER_TYPE;
		}
	}
}
