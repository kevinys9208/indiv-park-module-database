package indiv.park.data.exception;

public class NameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5076364678797797342L;
	
	public NameNotFoundException(String name) {
		super("'" + name + "' 와(과) 일치하는 이름의 데이터베이스가 존재하지 않습니다.");
	}
}
