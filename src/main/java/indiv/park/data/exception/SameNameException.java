package indiv.park.data.exception;

public class SameNameException extends RuntimeException {

	private static final long serialVersionUID = 5049009303436600152L;
	
	public SameNameException(String name) {
		super("'" + name + "'와(과) 같은 이름의 데이터베이스가 존재합니다.");
	}
}