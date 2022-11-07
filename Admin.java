package cafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Admin {
	// 필드
	static int couponAdmin; // 쿠폰당 가격차감 금액

	// 생성자
	public Admin(int couponAdmin) {
		this.couponAdmin = couponAdmin;
	}

	// method
	// 1. 메뉴관리 메서드
	public static void productChange() throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // 입력 받을 준비

		// 변수 선언
		OrderCart oc = new OrderCart(); // OrderCart 객체, 메뉴 불러오기
		int menuSelectNum = 0; // 입력 받을 숫자
		String menuSelectStr = ""; // 입력 받을 코드
		String guidTmp = "============================";
		String guidChange = "U. 가격변경\\nD. 메뉴삭제\\nI. 메뉴 추가\\n(U,D,I)코드를 입력해주세요>>> ";
		String guidErr = "잘못된 입력입니다.";

		while (true) {
			// 안내 출력
			System.out.println("===메뉴 관리===\n1. 커피관리\n2. 음료 관리\n3. 디저트 관리\n(1~3)숫자를 입력해주세요>>>");

			// 메뉴 입력
			menuSelectNum = Integer.parseInt(br.readLine());
			oc.choice.add(menuSelectNum);

			if (menuSelectNum == 1) {// 커피 관리

				while (true) {
					// 커피 메뉴 출력 메소드
					oc.SelectCoffeeAll();

					// 안내 출력
					System.out.println(guidTmp);
					System.out.println(guidChange);

					// 코드 입력받기
					menuSelectStr = br.readLine();

					if (menuSelectStr.equalsIgnoreCase("U")) { // 업데이트
						productUpdate();
					} else if (menuSelectStr.equalsIgnoreCase("D")) { // 삭제
						productDelete();
					} else if (menuSelectStr.equalsIgnoreCase("I")) { // 추가
						productInsert();
					} else {// 코드 다시 입력받기
						System.out.println(guidErr);
						continue;
					} // while.if

					break; // 선택 완료후 빠져나오기
				} // while 커피 메뉴

			} else if (menuSelectNum == 2) {// 음료 관리

				while (true) {
					// 음료 메뉴 출력 메소드
					oc.SelectBeverageAll();

					// 안내 출력
					System.out.println(guidTmp);
					System.out.println(guidChange);

					// 코드 입력받기
					menuSelectStr = br.readLine();

					if (menuSelectStr.equalsIgnoreCase("U")) { // 업데이트
						productUpdate();
					} else if (menuSelectStr.equalsIgnoreCase("D")) { // 삭제
						productDelete();
					} else if (menuSelectStr.equalsIgnoreCase("I")) { // 추가
						productInsert();
					} else {// 코드 다시 입력받기
						System.out.println(guidErr);
						continue;
					} // while.if

					break; // 선택 완료후 빠져나오기
				} // while 커피 메뉴

			} else if (menuSelectNum == 3) {// 디저트 관리

				while (true) {
					// 디저트 메뉴 출력 메소드
					oc.SelectDessertAll();

					// 안내 출력
					System.out.println(guidTmp);
					System.out.println(guidChange);

					// 코드 입력받기
					menuSelectStr = br.readLine();

					if (menuSelectStr.equalsIgnoreCase("U")) { // 업데이트
						productUpdate();
					} else if (menuSelectStr.equalsIgnoreCase("D")) { // 삭제
						productDelete();
					} else if (menuSelectStr.equalsIgnoreCase("I")) { // 추가
						productInsert();
					} else {// 코드 다시 입력받기
						System.out.println(guidErr);
						continue;
					} // while.if

					break; // 선택 완료후 빠져나오기
				} // while 커피 메뉴

			} else {// 번호 다시 입력받기
				System.out.println(guidErr);
				continue;
			} // while.if

			br.close(); // 입력 종료
			break; // 선택 완료후 빠져나오기
		} // while

	}// productChange()

	// 1.1 메뉴 update
	public static void productUpdate() throws IOException {
		// Jdbc 객체 생성
		DB jdbc = new DB();

		// bufferedReader
		jdbc.br = new BufferedReader(new InputStreamReader(System.in));

		// 변수 선언
		OrderCart oc = new OrderCart(); // OrderCart 객체, 메뉴 불러오기
		String sql = ""; // sql문
		int rows = 0; // 변경된 행의 수
		int code = 0; // 코드 명
		int price = 0; // 추가할 금액 명
		int menu = oc.choice.poll(); // 메뉴 선택한거 불러오기

		try {
			// 안내 및 코드 입력
			System.out.println("코드를 입력하세요>>> ");
			code = Integer.parseInt(jdbc.br.readLine());
			System.out.println("변경할 가격을 입력하세요>>> ");
			price = Integer.parseInt(jdbc.br.readLine());

			jdbc.br.close();

			// 메뉴로 나누기
			if (menu == 1) {
				sql = "UPDATE coffee set cprice = ? WHERE ccode = ?";
			} else if (menu == 2) {
				sql = "UPDATE beverage set cprice = ? WHERE ccode = ?";
			} else if (menu == 3) {
				sql = "UPDATE dessert set cprice = ? WHERE ccode = ?";
			} else {
				System.out.println("잘못된 입력입니다.");
			}

			// jdbc 드라이버 등록
			Class.forName(jdbc.driver);

			// db 연결
			jdbc.CN = DriverManager.getConnection(jdbc.url, jdbc.user, jdbc.password);

			// prepareStatement 객체 생성
			jdbc.PS = jdbc.CN.prepareStatement(sql);

			// 실행
			jdbc.PS.setInt(1, price);
			jdbc.PS.setInt(2, code);

			// sql 문장을 실행하고 결과를 리턴
			rows = jdbc.PS.executeUpdate();

			if (rows == 0) {
				System.out.println("변경에 실패했습니다.");
			} else {
				System.out.println("1개 메뉴를 변경했습니다.");
			} // try.if

		} catch (ClassNotFoundException e) { // getConnection(url, user, password);
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (SQLException e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (Exception e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} finally { // 무조건 실행되는 코드
			// 연결이 되어 있으면 연결 끊기

			// 사용순서 반대로 닫기
			if (jdbc.RS != null) {
				try {
					jdbc.RS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.PS != null) {
				try {
					jdbc.PS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.CN != null) {
				try {
					jdbc.CN.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			}

		} // try
	} // productUpdate()

	// 1.2 메뉴 insert
	public static void productInsert() throws IOException {
		// Jdbc 객체 생성
		DB jdbc = new DB();

		// bufferedReader
		jdbc.br = new BufferedReader(new InputStreamReader(System.in));

		// 변수 선언
		OrderCart oc = new OrderCart(); // OrderCart 객체, 메뉴 불러오기
		String sql = ""; // sql문
		int rows = 0; // 추가된 행의 수
		int code = 0; // 추가할 코드 명
		String name = ""; // 추가할 메뉴 명
		int price = 0; // 추가할 금액 명
		int menu = oc.choice.poll(); // 메뉴 선택한거 불러오기

		try {
			// 안내 및 코드 입력
			System.out.println("추가할 코드를 입력하세요>>> ");
			code = Integer.parseInt(jdbc.br.readLine());
			System.out.println("추가할 이름을 입력하세요>>> ");
			name = jdbc.br.readLine();
			System.out.println("추가할 가격을 입력하세요>>> ");
			price = Integer.parseInt(jdbc.br.readLine());

			jdbc.br.close();

			// 메뉴로 나누기
			if (menu == 1) {
				sql = "INSERT INTO coffee(ccode, cname, cprice) VALUES(?, ?, ?)";
			} else if (menu == 2) {
				sql = "INSERT INTO beverage(bcode, bname, bprice) VALUES(?, ?, ?)";
			} else if (menu == 3) {
				sql = "INSERT INTO dessert(dcode, dname, dprice) VALUES(?, ?, ?)";
			} else {
				System.out.println("잘못된 입력입니다.");
			}

			// jdbc 드라이버 등록
			Class.forName(jdbc.driver);

			// db 연결
			jdbc.CN = DriverManager.getConnection(jdbc.url, jdbc.user, jdbc.password);

			// prepareStatement 객체 생성
			jdbc.PS = jdbc.CN.prepareStatement(sql);

			// 실행
			jdbc.PS.setInt(1, code);
			jdbc.PS.setString(2, name);
			jdbc.PS.setInt(3, price);

			// sql 문장을 실행하고 결과를 리턴
			rows = jdbc.PS.executeUpdate();

			if (rows == 0) {
				System.out.println("업로드 실패했습니다.");
			} else {
				System.out.println("1개 메뉴를 추가했습니다.");
			} // try.if

		} catch (ClassNotFoundException e) { // getConnection(url, user, password);
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (SQLException e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (Exception e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} finally { // 무조건 실행되는 코드
			// 연결이 되어 있으면 연결 끊기

			// 사용순서 반대로 닫기
			if (jdbc.RS != null) {
				try {
					jdbc.RS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.PS != null) {
				try {
					jdbc.PS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.CN != null) {
				try {
					jdbc.CN.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			}

		} // try
	}// productInsert()

	// 1.3 메뉴 delete
	public static void productDelete() {
		// Jdbc 객체 생성
		DB jdbc = new DB();

		// bufferedReader
		jdbc.br = new BufferedReader(new InputStreamReader(System.in));

		// 변수 선언
		OrderCart oc = new OrderCart(); // OrderCart 객체, 메뉴 불러오기
		String sql = ""; // sql문
		int rows = 0; // 삭제된 행의 수
		int code = 0; // 삭제할 코드 입력
		int menu = oc.choice.poll(); // 메뉴 선택한거 불러오기

		try {
			// 안내 및 코드 입력
			System.out.println("삭제할 코드를 입력하세요>>> ");
			code = Integer.parseInt(jdbc.br.readLine());
			jdbc.br.close();

			// 메뉴로 나누기
			if (menu == 1) {
				sql = "DELETE FROM coffee WHERE ccode = ?";
			} else if (menu == 2) {
				sql = "DELETE FROM beverage WHERE bcode = ?";
			} else if (menu == 3) {
				sql = "DELETE FROM dessert WHERE dcode = ?";
			} else {
				System.out.println("잘못된 입력입니다.");
			}

			// jdbc 드라이버 등록
			Class.forName(jdbc.driver);

			// db 연결
			jdbc.CN = DriverManager.getConnection(jdbc.url, jdbc.user, jdbc.password);

			// prepareStatement 객체 생성
			jdbc.PS = jdbc.CN.prepareStatement(sql);

			// 실행
			jdbc.PS.setInt(1, code);

			// sql 문장을 실행하고 결과를 리턴
			rows = jdbc.PS.executeUpdate();

			if (rows == 0) {
				System.out.println("삭제할 값이 없습니다");
			} else {
				System.out.println("삭제가 완료되었습니다.");
			} // try.if

		} catch (ClassNotFoundException e) { // getConnection(url, user, password);
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (SQLException e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (Exception e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} finally { // 무조건 실행되는 코드
			// 연결이 되어 있으면 연결 끊기

			// 사용순서 반대로 닫기
			if (jdbc.RS != null) {
				try {
					jdbc.RS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.PS != null) {
				try {
					jdbc.PS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.CN != null) {
				try {
					jdbc.CN.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			}

		} // try

	}// productDelete()

	// 2. 회원정보를 불러오고, 삭제가 가능함
	public static void customerChange() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // 입력 받을 준비

		// 변수선언
		String id = ""; // 아이디 입력
		String code = ""; // 코드 입력

		// 실행
		customerAllSelect(); // 회원 정보 불러오기
		while (true) {
			System.out.println("주문내역을 볼 회원의 아이디를 입력하세요>>> ");
			System.out.println("(종료는 D를 입력하세요)");
			System.out.println();
			id = br.readLine(); // 아이디 입력
			
			if (id.equalsIgnoreCase("d")) {
				break;
			}
			
			selectOrderCart(id); // 회원을 선택하기후 주문내역 출력
			
			System.out.println("D.회원을 삭제");
			System.out.println("(종료는 아무 키나 입력하세요)");
			System.out.println();
			code = br.readLine();

			if (code.equalsIgnoreCase("d")) { 
				customerDelete(id); // 선택 회원을 삭제함
			}
			
			break;
		}
		
		br.close();

	}// customerChange()

	// 2.1 회원 정보 불러오기
	public static void customerAllSelect() {
		// Jdbc 객체 생성
		DB jdbc = new DB();

		// 변수 선언
		String sql = "SELECT * FROM customers"; // 커피
		String id = ""; // 고객 아이디
		String name = ""; // 고객 이름
		String pwd = ""; // 고객 비번
		String pwd1 = ""; // 비번 처리
		int phone = 0; // 고객 휴대폰
		int coupon = 0; // 고객 쿠폰
		int couponcheck = 0; // 쿠폰 체크
		String koFormat = "| %-7s | %-15s | %-15s | %-15s | %6s | %10d |\n"; // 출력
		String enFormat = "| %-10s | %-15s | %-15s | %-15s | %6s | %10s |\n"; // 출력

		try {
			// jdbc 드라이버 등록
			Class.forName(jdbc.driver);

			// db 연결
			jdbc.CN = DriverManager.getConnection(jdbc.url, jdbc.user, jdbc.password);

			// statement 객체 생성
			jdbc.stmt = jdbc.CN.createStatement();

			// sql 문장을 실행하고 결과를 리턴
			jdbc.RS = jdbc.stmt.executeQuery(sql);

			// 출력
			System.out.format(
					"+------------+-----------------+-----------------+-----------------+--------+------------+\n");
			System.out.printf(enFormat, "Name", "ID", "Password", "Phone Number", "Coupon", "CouponCheck");
			System.out.format(
					"+------------+-----------------+-----------------+-----------------+--------+------------+\n");

			while (jdbc.RS.next()) {
				id = jdbc.RS.getString("customer_id"); // 아이디
				name = jdbc.RS.getString("customer_name"); // 이름
				pwd = jdbc.RS.getString("customer_pwd"); // 비번
				phone = jdbc.RS.getInt("customer_phone"); // 폰
				coupon = jdbc.RS.getInt("customer_coupon"); // 쿠폰
				couponcheck = jdbc.RS.getInt("customer_couponcheck"); // 쿠폰체크

				pwd1 = "*".repeat(pwd.length()); // 비번을 *로 출력, 길이만 알 수 있음

				System.out.printf(koFormat, name, id, pwd, phone, coupon, couponcheck);
			}
			System.out.println();

		} catch (ClassNotFoundException e) { // getConnection(url, user, password);
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (SQLException e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (Exception e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} finally { // 무조건 실행되는 코드
			// 연결이 되어 있으면 연결 끊기

			// 사용순서 반대로 닫기
			if (jdbc.RS != null) {
				try {
					jdbc.RS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.stmt != null) {
				try {
					jdbc.stmt.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.CN != null) {
				try {
					jdbc.CN.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			}

		} // try

	}// customerAllSelect()

	// 2.2 회원을 선택하기후 주문내역 출력
	public static void selectOrderCart(String id) { // 날짜선택하면 불러오게 하는거 추후 변경***********************221106
		// Jdbc 객체 생성
		DB jdbc = new DB();

		// 변수 선언
		String sql = "SELECT * FROM orderCart WHERE customer_id = ?"; // sql문
		String name = ""; // 상품 이름 받기
		int price = 0; // 가격받기
		int ocode = 0; // 주문 코드 받기
		int ccode = 0; // 커피 코드 받기
		int bcode = 0; // 음료 코드 받기
		int dcode = 0; // 디저트 코드 받기
		int amount = 0; // 수량 받기
		int allPrice = 0; // 판매금액 합계
		Date date = null; // date 받기

		try {

			// jdbc 드라이버 등록
			Class.forName(jdbc.driver);

			// db 연결
			jdbc.CN = DriverManager.getConnection(jdbc.url, jdbc.user, jdbc.password);

			// prepareStatement 객체 생성
			jdbc.PS = jdbc.CN.prepareStatement(sql);

			// 실행
			jdbc.PS.setString(1, id);

			// sql 문장을 실행하고 결과를 리턴
			jdbc.RS = jdbc.PS.executeQuery();

			// 결과 출력
			String koFormat = "| %-7d | %-7d | %-7d | %-7d | %-15s | %-6d | %-6d | %-3B - %-3tA |\n";
			// 주문, 커피, 음료, 디저트, 이름, 수량, 가격, 날짜(달,요일)
			String enFormat = "| %-7s | %-7s | %-7s | %-7s | %-15s | %-6s | %-6s | %-8s |\n"; // 추후수정*********************
			System.out.format(
					"+---------+---------+---------+---------+-----------------+--------+--------+----------+\n");
			System.out.printf(enFormat, "주문", "커피", "음료", "디저트", "이름", "수량", "가격", "날짜(달,요일)");
			while (jdbc.RS.next()) {
				// 데이터 불러오기
				ocode = jdbc.RS.getInt("ocode");
				id = jdbc.RS.getString("customer_id");
				ccode = jdbc.RS.getInt("ccode");
				bcode = jdbc.RS.getInt("bcode");
				dcode = jdbc.RS.getInt("dcode");
				name = jdbc.RS.getString("oname");
				amount = jdbc.RS.getInt("oamount");
				price = jdbc.RS.getInt("oprice");
				date = jdbc.RS.getDate("odate");

				// 매출 합계
				allPrice += price;

				// 출력
				System.out.printf(koFormat, ocode, ccode, bcode, name, amount, price, date);
			}
			System.out.println();
			System.out.println("총 결제 금액>>> " + allPrice);

		} catch (ClassNotFoundException e) { // getConnection(url, user, password);
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (SQLException e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (Exception e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} finally { // 무조건 실행되는 코드
			// 연결이 되어 있으면 연결 끊기

			// 사용순서 반대로 닫기
			if (jdbc.RS != null) {
				try {
					jdbc.RS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.stmt != null) {
				try {
					jdbc.stmt.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.CN != null) {
				try {
					jdbc.CN.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			}

		} // try
	} // income()

	// 2.3 선택 회원을 삭제함
	public static void customerDelete(String id) {
		// Jdbc 객체 생성
		DB jdbc = new DB();

		// 변수 선언
		String sql = ""; // sql문
		int rows = 0; // 삭제된 행의 수

		try {
			// 메뉴로 나누기
			sql = "DELETE FROM customers WHERE customer_id = ?";

			// jdbc 드라이버 등록
			Class.forName(jdbc.driver);

			// db 연결
			jdbc.CN = DriverManager.getConnection(jdbc.url, jdbc.user, jdbc.password);

			// prepareStatement 객체 생성
			jdbc.PS = jdbc.CN.prepareStatement(sql);

			// 실행
			jdbc.PS.setString(1, id);

			// sql 문장을 실행하고 결과를 리턴
			rows = jdbc.PS.executeUpdate();

			if (rows == 0) {
				System.out.println("삭제할 값이 없습니다");
			} else {
				System.out.println("삭제가 완료되었습니다.");
			} // try.if

		} catch (ClassNotFoundException e) { // getConnection(url, user, password);
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (SQLException e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (Exception e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} finally { // 무조건 실행되는 코드
			// 연결이 되어 있으면 연결 끊기

			// 사용순서 반대로 닫기
			if (jdbc.RS != null) {
				try {
					jdbc.RS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.PS != null) {
				try {
					jdbc.PS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.CN != null) {
				try {
					jdbc.CN.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			}

		} // try

	}// customerDelete()

	// 3. 판매금액 전부 불러오기 메서드
	public static void income() { // 날짜선택하면 불러오게 하는거 추후 변경***********************221106
		// Jdbc 객체 생성
		DB jdbc = new DB();

		// 변수 선언
		String sql = "SELECT * FROM orderCart"; // sql문
		String name = ""; // 상품 이름 받기
		String id = ""; // id 받기
		int price = 0; // 가격받기
		int ocode = 0; // 주문 코드 받기
		int ccode = 0; // 커피 코드 받기
		int bcode = 0; // 음료 코드 받기
		int dcode = 0; // 디저트 코드 받기
		int amount = 0; // 수량 받기
		int allPrice = 0; // 판매금액 합계
		Date date = null; // date 받기

		try {
			// jdbc 드라이버 등록
			Class.forName(jdbc.driver);

			// db 연결
			jdbc.CN = DriverManager.getConnection(jdbc.url, jdbc.user, jdbc.password);

			// statement 객체 생성
			jdbc.stmt = jdbc.CN.createStatement();

			// sql 문장을 실행하고 결과를 리턴
			jdbc.RS = jdbc.stmt.executeQuery(sql);

			// 결과 출력
			String koFormat = "| %-7d | %-7d | %-7d | %-7d | %-15s | %-6d | %-6d | %-3B - %-3tA |\n";
			// 주문, 커피, 음료, 디저트, 이름, 수량, 가격, 날짜(달,요일)
			String enFormat = "| %-7s | %-7s | %-7s | %-7s | %-15s | %-6s | %-6s | %-6s |\n"; // 추후
																								// 수정*********************
			System.out.format("+------------+-----------------+-----------------+-----------------+--------+\n");
			System.out.printf(enFormat, "주문", "커피", "음료", "디저트", "이름", "수량", "가격", "날짜(달,요일)");
			while (jdbc.RS.next()) {
				// 데이터 불러오기
				ocode = jdbc.RS.getInt("ocode");
				id = jdbc.RS.getString("customer_id");
				ccode = jdbc.RS.getInt("ccode");
				bcode = jdbc.RS.getInt("bcode");
				dcode = jdbc.RS.getInt("dcode");
				name = jdbc.RS.getString("oname");
				amount = jdbc.RS.getInt("oamount");
				price = jdbc.RS.getInt("oprice");
				date = jdbc.RS.getDate("odate");

				// 매출 합계
				allPrice += price;

				// 출력
				System.out.printf(koFormat, ocode, ccode, bcode, name, amount, price, date);
			}
			System.out.println();
			System.out.println("총 매출>>> " + allPrice);

		} catch (ClassNotFoundException e) { // getConnection(url, user, password);
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (SQLException e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (Exception e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} finally { // 무조건 실행되는 코드
			// 연결이 되어 있으면 연결 끊기

			// 사용순서 반대로 닫기
			if (jdbc.RS != null) {
				try {
					jdbc.RS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.stmt != null) {
				try {
					jdbc.stmt.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.CN != null) {
				try {
					jdbc.CN.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			}

		} // try
	} // income()

	// 4. 쿠폰 가치 바꾸기
	public static void couponValue() {// 해야할것************************************************
		// Jdbc 객체 생성
		DB jdbc = new DB();

		// bufferedReader
		jdbc.br = new BufferedReader(new InputStreamReader(System.in));

		// 변수 선언
		String sql = "UPDATE customers SET customer_cupon =? WHERE customer id = 'admin'"; // sql문
		int rows = 0; // 실행한 행의 수
		int coupon = 0; // 변경할 쿠폰 가격

		try {
			// 안내 및 입력
			System.out.println("=============결제 관리=============");
			System.out.printf("쿠폰 1개당 할인 금액은 %d원 입니다.\n", couponAdmin);
			System.out.println();

			// jdbc 드라이버 등록
			Class.forName(jdbc.driver);

			// db 연결
			jdbc.CN	 = DriverManager.getConnection(jdbc.url, jdbc.user, jdbc.password);

			// prepareStatement 객체 생성
			jdbc.PS = jdbc.CN.prepareStatement(sql);

			// 변경할 쿠폰 할인 금액 입력
			System.out.println("변경할 쿠폰 1개당 할인 금액>>>");
			coupon = Integer.parseInt(jdbc.br.readLine());
			jdbc.br.close();

			jdbc.PS.setInt(1, coupon);

			// sql 문장을 실행하고 결과를 리턴
			rows = jdbc.PS.executeUpdate();

			if (rows == 0) {
				System.out.println("변경에 실패했습니다.");
			} else {
				System.out.printf("쿠폰 할인 금액을 %d으로 변경했습니다./n", coupon);
			} // if
			System.out.println();

		} catch (ClassNotFoundException e) { // getConnection(url, user, password);
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (SQLException e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} catch (Exception e) {
			e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
		} finally { // 무조건 실행되는 코드
			// 연결이 되어 있으면 연결 끊기

			// 사용순서 반대로 닫기
			if (jdbc.RS != null) {
				try {
					jdbc.RS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.PS != null) {
				try {
					jdbc.PS.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			} // if

			if (jdbc.CN != null) {
				try {
					jdbc.CN.close();
				} catch (SQLException e) {
					e.printStackTrace(); // 프로그램이 완료된 후에 반드시 제거 또는 주석
				}
			}

		} // try
	} // couponValue()

}
