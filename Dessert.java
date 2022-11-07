package cafe;

public class Dessert extends Product{
	//필드 선언
		int code;
		String name;
		int price;
		
		//기본 생성자
		public Dessert() {
			
		}
		
		//초기화 생성자
		public Dessert(int code, String name, int price) {
			this.code = code;
			this.name = name;
			this.price = price;
		}
		
}
