package cafe;

public class Product {
	//필드 선언
			int code;
			String name;
			int price;
			
			//기본 생성자
			public Product() {
				
			}
			
			//초기화 생성자
			public Product(int code, String name, int price) {
				this.code = code;
				this.name = name;
				this.price = price;
			}
			
			//toString 오버라이딩
			@Override
			public String toString() {
				int tmp =this.name.length();
				
				while(tmp<13) {
					name +=" ";
					tmp++;
				}
				
				String printFormat = "| %-5d | %-15s | %-6d |";
				return String.format(printFormat, code, name, price);
			}
			
}
