package indiv.park.data.exception;

public class TypeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 321089638213014750L;
	
	public TypeNotFoundException(String type) {
		super("'" + type + "' 와(과) 일치하는 타입의 데이터소스가 존재하지 않습니다.");
	}
}