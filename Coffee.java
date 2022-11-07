package cafe;

public class Coffee extends Product{
	//필드 선언
	int code;
	String name;
	int price;
	
	//기본 생성자
	public Coffee() {
		
	}
	
	//초기화 생성자
	public Coffee(int code, String name, int price) {
		this.code = code;
		this.name = name;
		this.price = price;
	}
	
	
	
}
